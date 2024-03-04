package com.github.raphael.isvelin;

import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;

public class ReturnedExpressionStatement extends ExpressionStatement {

    public ReturnedExpressionStatement(Expression expression) {
        super(expression);
    }
}
