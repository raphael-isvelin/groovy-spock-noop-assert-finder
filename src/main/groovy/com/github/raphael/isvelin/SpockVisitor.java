package com.github.raphael.isvelin;

import static com.github.raphael.isvelin.Constants.ASSERTING_METHODS;
import static com.github.raphael.isvelin.Constants.ASSERTJ_METHODS;
import static com.github.raphael.isvelin.Constants.AWAITILITY_METHOD_NAME;
import static com.github.raphael.isvelin.Constants.CLEANUP_BLOCK_TAG;
import static com.github.raphael.isvelin.Constants.POLLING_CONDITIONS_METHODS;
import static com.github.raphael.isvelin.Constants.EXPECT_BLOCK_TAG;
import static com.github.raphael.isvelin.Constants.GIVEN_BLOCK_TAG;
import static com.github.raphael.isvelin.Constants.GROOVY_METHOD_WITH_NON_ASSERTING_CLOSURE_PARAMS;
import static com.github.raphael.isvelin.Constants.GROOVY_METHOD_WITH_NON_ASSERTING_CLOSURE_PARAMS_RETURNING_BOOLEAN;
import static com.github.raphael.isvelin.Constants.METHODS_IMPLICIT_RETURN_ALLOWED;
import static com.github.raphael.isvelin.Constants.SETUP_BLOCK_TAG;
import static com.github.raphael.isvelin.Constants.TEST_CASE_BLOCK_TAG;
import static com.github.raphael.isvelin.Constants.THEN_BLOCK_TAG;
import static com.github.raphael.isvelin.Constants.WHEN_BLOCK_TAG;
import static com.github.raphael.isvelin.Constants.WHERE_BLOCK_TAG;
import static com.github.raphael.isvelin.Helpers.cleanMethodName;
import static com.github.raphael.isvelin.Helpers.writeMatch;
import static com.github.raphael.isvelin.Helpers.writeSystemInfo;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.NotExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.AssertStatement;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.DoWhileStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.WhileStatement;
import org.codehaus.groovy.control.SourceUnit;

/**
 * WARNING: this is a quick and **dirty** parser made for a specific use-case, and not really built to be amended.
 *   If you wish to make a quick modification to include or exclude a specific pattern,
 *   I may suggest adding it to `visitExpressionStatement`,
 *   this is the top-level expression, and has the most context to make a decision;
 *   it will participate in adding to the pile of dirt, but will likely be the quickest way to include your small patch.
 */
public class SpockVisitor extends ClassCodeVisitorSupport {

    public final Path filePath;
    private final String sourceCode;

    public int nestingLevel = 0;

    public Stack<Type> blockStack = new Stack<>();
    public Stack<List<Statement>> closureStatements = new Stack<>();
    public Set<String> visitedMethods = new HashSet<>();

    public String currentMethod = "-"; // the test case or outer method
    public String currentAssertingMethod = "-"; // only shown when blockStack.peek() == ASSERTING_METHOD
    public String lastCalledMethod = "-";

    private int blocksInTestCase = 0;
    private String currentNonAssertingMethodWithClosureParam = null;
    private boolean inMockClosure = false;

    public SpockVisitor(final Path filePath, final String sourceCode) {
        this.filePath = filePath;
        this.sourceCode = sourceCode; // should get the imports in a cleaner way
        blockStack.push(Type.CLASS);
    }

    @Override
    protected SourceUnit getSourceUnit() {
        return null;
    }

    @Override
    public void visitMethodCallExpression(MethodCallExpression call) {
        ++nestingLevel;
        if (call.getMethodAsString() != null && ASSERTING_METHODS.contains(call.getMethodAsString())) {
            blockStack.push(Type.ASSERTING_METHOD);
            currentAssertingMethod = call.getMethodAsString();
            System.out.println(
                    formatLogPrefix() + "ASSERTING METHOD (" + call.getMethodAsString() + " - " + call.getText());
            super.visitMethodCallExpression(call);
            blockStack.pop();
            --nestingLevel;
            return;
        }
        if (call.getText().endsWith("thrown(Exception)")) {
            writeMatch(this, Severity.SMELL, call.getLineNumber(), "thrown(Exception) is too generic and can hide other exceptions");
            --nestingLevel;
            return;
        }
        blockStack.push(Type.OTHER_METHOD);
        System.out.println(
                formatLogPrefix() + "METHOD CALL - " + call.getMethod() + " - " + call.getText() + " - " + call.getMethodAsString());
        lastCalledMethod = call.getMethodAsString();
        if (call.getMethodAsString() != null && METHODS_IMPLICIT_RETURN_ALLOWED.contains(call.getMethodAsString())) {
            System.out.println("(ignoring method: " + call.getMethodAsString() + ")");
        } else if (call.getArguments() instanceof ArgumentListExpression) {
            call.getObjectExpression().visit(this);
            call.getMethod().visit(this);
            visitArgumentListExpression((ArgumentListExpression) call.getArguments(), call.getMethodAsString());
        } else {
            super.visitMethodCallExpression(call);
        }

        blockStack.pop();
        --nestingLevel;
    }

    public void visitArgumentListExpression(ArgumentListExpression expression, String methodName) {
        for (var e : expression.getExpressions()) {
            if (e instanceof ClosureExpression
                        || (e instanceof CastExpression ce && ce.getExpression() instanceof ClosureExpression)) {
                if (GROOVY_METHOD_WITH_NON_ASSERTING_CLOSURE_PARAMS.contains(methodName)) {
                    System.out.println("(visiting closure param of non-asserting method: " + methodName + ")");
                    currentNonAssertingMethodWithClosureParam = methodName;
                    e.visit(this);
                    currentNonAssertingMethodWithClosureParam = null;
                } else {
                    // way to avoid matching stuff like "1*myMock.method( {int i -> i == 4} )"
                    if (!inMockClosure) {
                        e.visit(this);
                    }
                }
            } else {
                System.out.println("is not a closure - " + e.getClass().getSimpleName());
                e.visit(this);
            }
        }
    }

    @Override
    public void visitClosureExpression(ClosureExpression expression) {
        ++nestingLevel;
        System.out.println(formatLogPrefix() + "CLOSURE - " + expression.getText());
        if (expression.getCode() instanceof BlockStatement bs) {
            closureStatements.push(bs.getStatements());
            for (Statement s : bs.getStatements()) {
                s.visit(this);
            }
            closureStatements.pop();
        } else if (expression.getCode() instanceof ExpressionStatement es
                           && es.getExpression() instanceof PropertyExpression pe) {
            System.out.println("~ looks like another implicit return property ~");
            writeMatch(this, Severity.MAYBE, pe);
        } else if (expression.getCode() instanceof ExpressionStatement es
                           && es.getExpression() instanceof BinaryExpression be) {
            if ((currentNonAssertingMethodWithClosureParam != null && GROOVY_METHOD_WITH_NON_ASSERTING_CLOSURE_PARAMS_RETURNING_BOOLEAN.contains(currentNonAssertingMethodWithClosureParam))
                        || lastCalledMethod.equals(AWAITILITY_METHOD_NAME)) {
                System.out.println("(acceptable implicit return property - " + currentNonAssertingMethodWithClosureParam + " - " + lastCalledMethod + " - " + be.getText() + ")");
                writeMatch(this, Severity.MAYBE, be);
            } else {
                System.out.println("~ looks like another BinaryExpression implicit return property ~");
                writeMatch(this, Severity.WARN, be);
            }
        } else if (expression.getCode() instanceof ExpressionStatement es
                           && es.getExpression() instanceof NotExpression ne) {
            System.out.println("~ looks like another implicit ! return statement ~");
            writeMatch(this, Severity.MAYBE, ne);
        } else {
            System.out.println("UNEXPECTED: code of closure isn't BlockStatement: " + expression);
            super.visitClosureExpression(expression);
        }
        --nestingLevel;
    }

    private boolean findCallToGroovyMethodWithClosureParamsReturningBoolean(ExpressionStatement expression) {
        MethodCallExpression mce = null;
        if (expression.getExpression() instanceof MethodCallExpression mcex) mce = mcex;
        else if (expression.getExpression() instanceof NotExpression ne && ne.getExpression() instanceof MethodCallExpression mcex) mce = mcex;
        if (mce != null && mce.getMethodAsString() != null && GROOVY_METHOD_WITH_NON_ASSERTING_CLOSURE_PARAMS_RETURNING_BOOLEAN.contains(mce.getMethodAsString())) {
            if (blockStack.peek() == Type.ROOT_THEN || blockStack.peek() == Type.ASSERTING_METHOD) {
                System.out.println("(Groovy method '" + mce.getMethodAsString() + "' called in then block or verify/with block, looks safe - not jumping in method)");
                return true;
            } else {
                writeMatch(this, Severity.WARN, mce.getLineNumber(), "boolean-returning Groovy method called outside of then block: " + expression.getText());
                return true;
            }
        }
        return false;
    }

    private boolean ifEventuallyLogAndReturnTrue(Expression e) {
        // TODO there's possibly other closures behaving like `eventually`, make this more generic and cleanup... everything
        if (lastCalledMethod != null && POLLING_CONDITIONS_METHODS.contains(lastCalledMethod)) {
            final var msg = "Warning! Avoiding assert keyword in the clojure is only possible if the conditions object type is "
                + "known during compilation (no 'def' on the left side), and only from Spock 2.0. "
                + "Consider always using assert for safety.";
            writeMatch(this, Severity.SMELL, e.getLineNumber(), msg);
            return true;
        }
        return false;
    }

    @Override
    public void visitExpressionStatement(ExpressionStatement expression) {
        System.out.println("[EXPR] " + expression.getText());
        if (findCallToGroovyMethodWithClosureParamsReturningBoolean(expression)) {
            return;
        }

        if (sourceCode.contains("org.assertj.core.api.Assertions")) {
            // TODO cleanup + get imports in a cleaner way
            for (final var method : ASSERTJ_METHODS) {
                if (expression.getText().startsWith("this." + method + "(")) {
                    System.out.println("(skipping AssertJ method)");
                    return;
                }
            }
        }

        // TODO detect it in a nicer way, will break if "given:" is included in a comment or string (for whichever reason..)
        if (expression.toString().contains(GIVEN_BLOCK_TAG) || expression.toString().contains(WHEN_BLOCK_TAG) || expression.toString().contains(WHERE_BLOCK_TAG) || expression.toString().contains(SETUP_BLOCK_TAG) || expression.toString().contains(CLEANUP_BLOCK_TAG)) {
            ++blocksInTestCase;
            if (blockStack.peek() == Type.ROOT_THEN) {
                System.out.println("<from block 'then' to block 'given'>");
            } else if (blockStack.peek() != Type.ROOT_GIVEN) {
                // likely because the test case doesn't match the pattern in Helpers::addMetaDataToSourceCode; need a more robust way
                System.out.println("UNEXPECTED: found 'given' block, but stack.peek is not root: " + blockStack.peek().name() + " - assuming it's a test (" + filePath.getFileName() + " - " + currentMethod + ")");
            }
            blockStack.pop();
            blockStack.push(Type.ROOT_GIVEN);
            return;
        } else if (expression.toString().contains(THEN_BLOCK_TAG) || expression.toString().contains(EXPECT_BLOCK_TAG)) {
            ++blocksInTestCase;
            if (blockStack.peek() == Type.ROOT_GIVEN) {
                System.out.println("<from block 'given' to block 'then'>");
            } else if (blockStack.peek() != Type.ROOT_THEN) {
                // likely because the test case doesn't match the pattern in Helpers::addMetaDataToSourceCode; need a more robust way
                System.out.println("UNEXPECTED: found 'then' block, but stack.peek is not root: " + blockStack.peek().name() + " - assuming it's a test (" + filePath.getFileName() + " - " + currentMethod + ")");
            }
            blockStack.pop();
            blockStack.push(Type.ROOT_THEN);
            return;
        }
        ++nestingLevel;
        if (expression.getExpression() instanceof BinaryExpression be
                    && List.of("==", "<", ">", "<=", ">=", "!=").contains(be.getOperation().getText())) {
            if (blockStack.peek().requiresAssert) {
                if (expression instanceof ReturnedExpressionStatement) {
                    writeMatch(this, Severity.MAYBE, be, "Returned", "");
                } else {
                    if (!closureStatements.isEmpty() && !closureStatements.peek().isEmpty()
                            && closureStatements.peek().get(closureStatements.peek().size()-1).toString().contains(be.toString())) {
                        System.out.println("~ looks like an implicit return statement (binary expression) ~ " + currentNonAssertingMethodWithClosureParam + " ~ " + currentMethod + " ~ lmc " + lastCalledMethod + " ~");
                        if ((currentNonAssertingMethodWithClosureParam != null && GROOVY_METHOD_WITH_NON_ASSERTING_CLOSURE_PARAMS_RETURNING_BOOLEAN.contains(currentNonAssertingMethodWithClosureParam))
                                || lastCalledMethod.equals(AWAITILITY_METHOD_NAME)) {
                            writeMatch(this, Severity.MAYBE, be);
                        } else {
                            if (!ifEventuallyLogAndReturnTrue(be)) {
                                writeMatch(this, Severity.WARN, be);
                            }
                        }
                    } else {
                        if (!ifEventuallyLogAndReturnTrue(be)) {
                            writeMatch(this, Severity.ERROR, be);
                        }
                    }
                }
            } else {
                System.out.println("(safe, in " + blockStack.peek().value.trim() + " block - " + expression.getText() + ")");
            }
            --nestingLevel;
            return;
        } else if (expression.getExpression() instanceof PropertyExpression pe) {
            if (blockStack.peek().requiresAssert) {
                if (expression instanceof ReturnedExpressionStatement) {
                    writeMatch(this, Severity.MAYBE, pe, "Returned", "");
                } else {
                    if (!closureStatements.isEmpty() && !closureStatements.peek().isEmpty()
                            && closureStatements.peek().get(closureStatements.peek().size()-1).toString().contains(pe.toString())) {
                        System.out.println("~ looks like an implicit return statement ~");
                        writeMatch(this, Severity.MAYBE, pe);
                    } else {
                        writeMatch(this, Severity.ERROR, pe);
                    }
                }
            } else {
                System.out.println("(safe, in " + blockStack.peek().value.trim() + " block - " + pe.getText() + ")");
            }
            --nestingLevel;
            return;
        } else if (expression.getExpression() instanceof NotExpression ne
                && ne.getExpression() instanceof PropertyExpression pe) {
            if (blockStack.peek().requiresAssert) {
                if (expression instanceof ReturnedExpressionStatement) {
                    writeMatch(this, Severity.MAYBE, pe, "ReturnedNot", "!");
                } else {
                    if (!closureStatements.isEmpty() && !closureStatements.peek().isEmpty()
                            && closureStatements.peek().get(closureStatements.peek().size()-1).toString().contains(ne.toString())) {
                        System.out.println("~ looks like an implicit ! return statement ~");
                        writeMatch(this, Severity.MAYBE, pe, "Not", "!");
                    } else {
                        writeMatch(this, Severity.ERROR, pe, "Not", "!");
                    }
                }
            } else {
                System.out.println("(safe, in " + blockStack.peek().value.trim() + " block - !" + pe.getText() + ")");
            }
            --nestingLevel;
            return;
        } else {
            System.out.println(formatLogPrefix() + "\033[47;1mEXPRESSION STATEMENT\033[0m: " + (expression.getText().contains("println") ? "\033[34;1m" : "") + expression.getText() + "\033[0m");
        }
        if (expression.getExpression() instanceof BinaryExpression be
                && be.getOperation().getText().equals(">>")
                && be.getLeftExpression() instanceof MethodCallExpression) {
            if (blockStack.peek() == Type.ROOT_THEN) {
                // one could argue that checking invocation count is restricting the impl and not treating it as a black box,
                //   but it's a problem with overuse of mock in general, so if using them, better use them cleanly
                writeMatch(this, Severity.SMELL, be.getLineNumber(), "Not explicitly checking the number of invocations (e.g. 1*) can lead to hidden errors, esp. if no return value is checked in the test; and if it's only setting up values, it belongs to a 'given' block");
            } else if (blockStack.peek() == Type.ROOT_GIVEN) {
                // IMO not checking explicitly mock invocation count is also bad practice in 'given' blocks as it can lead to dead code,
                //   but not as bad as in 'then' blocks, and it would lead to a ton of matches
                //   (and I think it's fair game when e.g. making a mock data object)
            }
        }
        if (expression.getExpression() instanceof BinaryExpression be
                && be.getOperation().getText().equals(">>")
                && be.getLeftExpression() instanceof MethodCallExpression
                && (be.getRightExpression() instanceof MethodCallExpression || be.getRightExpression() instanceof VariableExpression || be.getRightExpression() instanceof ConstantExpression)) {
            // catches the mocks without explicit invocation count; if not there, could return false positive when using closures to validate parameters; see ClosureParamsCheckOfMocksWithNoInvocationCountCheckAreIgnored.case.groovy
            System.out.println("(looks like a closure-less mock, skipping)");
            --nestingLevel;
            return;
        }
        if (expression.getText().contains("this.println")) {
            // skip
        } else if (blockStack.peek() == Type.ROOT_THEN
                && expression.getExpression() instanceof BinaryExpression be
                && be.getOperation().getText().equals("*")
                && be.getLeftExpression() instanceof VariableExpression
                && be.getRightExpression() instanceof MethodCallExpression) {
            System.out.println("(looks like a mock - ignoring)");
        } else if (expression.getExpression() instanceof BinaryExpression be
                && be.getOperation().getText().equals("*")
                && (be.getLeftExpression() instanceof ConstantExpression || be.getLeftExpression() instanceof VariableExpression)) {
            // TODO refactor this mess - too many ways to enter a mock closure
            if (be.getRightExpression() instanceof MethodCallExpression mce) {
                System.out.println("(entering mock closure x)");
                inMockClosure = true;
                blockStack.push(Type.MOCK_CLOSURE);
                mce.visit(this);
                blockStack.pop();
                inMockClosure = false;
            } else {
                System.out.println("OOPS 1");
            }
        } else if (expression.getExpression() instanceof BinaryExpression be
                && be.getOperation().getText().equals(">>")
                && be.getLeftExpression() instanceof BinaryExpression beLeft
                && beLeft.getOperation().getText().equals("*")) {
            // TODO refactor this mess - too many ways to enter a mock closure
            if (be.getRightExpression() instanceof ClosureExpression ce) {
                System.out.println("(entering mock closure y)");
                inMockClosure = true;
                blockStack.push(Type.MOCK_CLOSURE);
                ce.getCode().visit(this);
                blockStack.pop();
                inMockClosure = false;
            } else {
                System.out.println("(ignoring mock closure)");
            }
        } else if (expression.getExpression() instanceof BinaryExpression be
                && be.getOperation().getText().equals(">>")
                && be.getLeftExpression() instanceof MethodCallExpression
                && be.getRightExpression() instanceof ClosureExpression ce) {
            // TODO refactor this mess - too many ways to enter a mock closure
            System.out.println("(entering mock closure z)");
            inMockClosure = true;
            blockStack.push(Type.MOCK_CLOSURE);
            ce.getCode().visit(this);
            blockStack.pop();
            inMockClosure = false;
        } else if (expression.getExpression() instanceof MethodCallExpression mc
                && mc.getMethodAsString() != null
                && (mc.getMethodAsString().startsWith("equals")
                    || mc.getMethodAsString().startsWith("contains")
                    || mc.getMethodAsString().startsWith("startsWith")
                    || mc.getMethodAsString().startsWith("endsWith")
                    || mc.getMethodAsString().startsWith("isEmpty")
                    || mc.getMethodAsString().startsWith("isPresent"))) {
            if (blockStack.peek().requiresAssert) {
                if (!ifEventuallyLogAndReturnTrue(mc)) {
                    writeMatch(this, Severity.WARN, mc);
                }
            } else {
                System.out.println("(safe, in " + blockStack.peek().value.trim() + " block - " + expression.getText() + ")");
            }
        } else {
            super.visitExpressionStatement(expression);
        }
        --nestingLevel;
    }

    @Override
    public void visitIfElse(IfStatement statement) {
        ++nestingLevel;
        blockStack.push(Type.CONDITION_OR_LOOP);
        System.out.println("\033[30;2m(if/else: " + statement.getBooleanExpression().getText() + ")\033[0m");
        statement.getIfBlock().visit(this);
        statement.getElseBlock().visit(this);
        blockStack.pop();
        --nestingLevel;
    }

    @Override
    public void visitDoWhileLoop(DoWhileStatement statement) {
        ++nestingLevel;
        blockStack.push(Type.CONDITION_OR_LOOP);
        System.out.println("\033[30;2m(do/while: " + statement.getBooleanExpression().getText() + ")\033[0m");
        statement.getLoopBlock().visit(this);
        blockStack.pop();
        --nestingLevel;
    }

    @Override
    public void visitForLoop(ForStatement statement) {
        ++nestingLevel;
        blockStack.push(Type.CONDITION_OR_LOOP);
        System.out.println("\033[30;2m(for: " + statement.getCollectionExpression().getText() + ")\033[0m");
        statement.getLoopBlock().visit(this);
        blockStack.pop();
        --nestingLevel;
    }

    @Override
    public void visitWhileLoop(WhileStatement statement) {
        ++nestingLevel;
        blockStack.push(Type.CONDITION_OR_LOOP);
        System.out.println("\033[30;2m(while: " + statement.getBooleanExpression().getText() + ")\033[0m");
        statement.getLoopBlock().visit(this);
        blockStack.pop();
        --nestingLevel;
    }

    @Override
    public void visitAssertStatement(AssertStatement statement) {
        System.out.println("(skipping assert)");
    }

    @Override
    public void visitListOfExpressions(List<? extends Expression> list) {
        System.out.println(formatLogPrefix() + "\033[1mList of expressions\033[0m: " + list);
        super.visitListOfExpressions(list);
    }

    @Override
    protected void visitConstructorOrMethod(MethodNode node, boolean isConstructor) {
        ++nestingLevel;
        boolean isTestCase = false;
        if (node.getName().contains(TEST_CASE_BLOCK_TAG)) {
            System.out.println("(found test case tag)");
            isTestCase = true;
            blocksInTestCase = 0;
            blockStack.push(Type.ROOT_THEN);
        } else {
            blockStack.push(Type.OTHER_METHOD);
        }
        if (node.getName().startsWith("assert") && node.getReturnType().getName().equals("boolean")) {
            writeMatch(this, Severity.SMELL, node.getLineNumber(), "method '" + node.getName() + "' starts by assert* but returns a boolean, this is laying the ground for future mistakes");
        }
        currentMethod = node.getName().replaceAll(TEST_CASE_BLOCK_TAG, "");
        if (!visitedMethods.contains(currentMethod)) {
            visitedMethods.add(currentMethod);
            writeSystemInfo(SystemInfoType.VISIT_METHOD, cleanMethodName(this));
        }
        System.out.println(formatLogPrefix() + "Visiting method: " + node.getName());
        super.visitConstructorOrMethod(node, isConstructor);
        blockStack.pop();
        if (isTestCase && blocksInTestCase == 0) {
            writeMatch(this, Severity.ERROR, node.getLineNumber(), "Test case has no block (given/then/etc.)");
        }
        --nestingLevel;
    }

    @Override
    public void visitReturnStatement(ReturnStatement statement) {
        System.out.println("return:");
        final var es = new ReturnedExpressionStatement(statement.getExpression());
        visitExpressionStatement(es);
    }

    @Override
    public void visitBinaryExpression(BinaryExpression expression) {
        ++nestingLevel;
        if (expression.getOperation().getText().equals("=")) {
            System.out.println("(skipping assignment: " + expression.getText() + ")");
        } else {
            super.visitBinaryExpression(expression);
        }
        --nestingLevel;
    }

    private String formatLogPrefix() {
        final var l = new StringBuilder();
        l.append("-".repeat(Math.max(0, nestingLevel)));

        final var scope = blockStack.peek();
        return "\033[1m" + currentMethod + "\033[0m"
            + " \033[35m" + scope.name()
            + (scope == Type.ASSERTING_METHOD ? ":" + currentAssertingMethod : "")
            + "\033[0m " + l;
    }
}
