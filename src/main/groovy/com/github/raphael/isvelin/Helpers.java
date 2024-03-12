package com.github.raphael.isvelin;

import static com.github.raphael.isvelin.Constants.EXPECT_BLOCK_TAG;
import static com.github.raphael.isvelin.Constants.GIVEN_BLOCK_TAG;
import static com.github.raphael.isvelin.Constants.TEST_CASE_BLOCK_TAG;
import static com.github.raphael.isvelin.Constants.THEN_BLOCK_TAG;
import static com.github.raphael.isvelin.Constants.WHEN_BLOCK_TAG;
import static com.github.raphael.isvelin.Constants.WHERE_BLOCK_TAG;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.groovy.ast.expr.Expression;

public class Helpers {

    public static final Path REPORT_PATH = Path.of("noop-report.csv");
    public static BufferedWriter reportFile;

    public static void initReportFile() {
        try {
            reportFile = new BufferedWriter(new FileWriter(REPORT_PATH.toString(), false));
            if (REPORT_PATH.getFileName().endsWith(".js")) {
                reportFile.write("const csv = `\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeReportFile() {
        try {
            if (REPORT_PATH.getFileName().endsWith(".js")) {
                reportFile.write("`");
            }
            reportFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String closeAndReadReport() {
        try {
            Helpers.closeReportFile();
            return new String(Files.readAllBytes(REPORT_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeMainBranchToReportFile() {
        final var envMainBranch = System.getenv("MAIN_BRANCH");
        final var mainBranch = envMainBranch != null ? envMainBranch : "master";
        System.out.println("main branch: " + mainBranch + " (" + envMainBranch + " from env MAIN_BRANCH)");
        writeSystemInfo(SystemInfoType.MAIN_BRANCH, mainBranch);
    }

    public static void writeSystemInfo(SystemInfoType type, String message) {
        try {
            String msg = "!!!%s,%s\n".formatted(type, message);
            System.out.println("(WRITING TO REPORT) " + msg);
            reportFile.write(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeMatch(SpockVisitor visitor, Severity severity, Expression expression, String expressionNamePrefix, String expressionTextPrefix) {
        try {
            final var scopeType = visitor.blockStack.peek();
            final var msg = ">>>%s,%s,%s,%d,%s,%s%s,%s%s\n".formatted(
                    severity.name(),
                    escapeString(visitor.filePath.getFileName().toString()),
                    cleanMethodName(visitor),
                    expression.getLineNumber(),
                    scopeType.value.trim(),
                    escapeString(expressionNamePrefix),
                    expression.getClass().getSimpleName(),
                    escapeString(expressionTextPrefix),
                    escapeString(expression.getText())
            );
            System.out.println("(WRITING TO REPORT) " + msg);
            reportFile.write(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeMatch(SpockVisitor visitor, Severity severity, Expression expression) {
        writeMatch(visitor, severity, expression, "", "");
    }

    public static void writeMatch(SpockVisitor visitor, Severity severity, int lineNumber, String reason) {
        try {
            final var scopeType = visitor.blockStack.peek();
            final var msg = ">>>%s,%s,%s,%d,%s,,%s\n".formatted(
                    severity.name(),
                    escapeString(visitor.filePath.getFileName().toString()),
                    cleanMethodName(visitor),
                    lineNumber,
                    scopeType.value.trim(),
                    escapeString(reason)
            );
            System.out.println("(WRITING TO REPORT) " + msg);
            reportFile.write(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readSourceCode(String filePath) {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            writeSystemInfo(SystemInfoType.ERROR, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static String addMetaDataToSourceCode(String sourceCode) {
        return sourceCode
            .replaceAll("\\$", "<!DOLLAR!>")
            .replaceAll("\n([^\n\"]*)given:", "\nprintln \"" + GIVEN_BLOCK_TAG + "\"; given:")
            .replaceAll("\n([^\n\"]*)setup:", "\nprintln \"" + GIVEN_BLOCK_TAG + "\"; setup:")
            .replaceAll("\n([^\n\"]*)cleanup:", "\nprintln \"" + GIVEN_BLOCK_TAG + "\"; cleanup:")
            .replaceAll("\n([^\n\"]*)when:", "\nprintln \"" + WHEN_BLOCK_TAG + "\"; when:")
            .replaceAll("\n([^\n\"]*)then:", "\nprintln \"" + THEN_BLOCK_TAG + "\"; then:")
            .replaceAll("\n([^\n\"]*)expect:", "\nprintln \"" + EXPECT_BLOCK_TAG + "\"; expect:")
            .replaceAll("\n([^\n\"]*)where:", "\nprintln \"" + WHERE_BLOCK_TAG + "\"; where:")
            .replaceAll("\n([^\n\"]*)def[ \t]+\"setup\"", "\n$1def setup")
            .replaceAll("\n([^\n\"]*)def[ \t]+'setup'", "\n$1def setup")
            .replaceAll("\n([^\n\"]*)def[ \t]+\"cleanup\"", "\n$1def cleanup")
            .replaceAll("\n([^\n\"]*)def[ \t]+'cleanup'", "\n$1def cleanup")
            .replaceAll("\n([^\n\"]*)void[ \t]+['\"]?setup['\"]?", "\n$1void setup")
            .replaceAll("\n([^\n\"]*)void[ \t]+['\"]?cleanup['\"]?", "\n$1void cleanup")
            .replaceAll("\n([^\n\"]*)def[ \t]+\"", "\n$1def \"" + TEST_CASE_BLOCK_TAG)
            .replaceAll("\n([^\n\"]*)def[ \t]+'", "\n$1def '" + TEST_CASE_BLOCK_TAG)
            .replaceAll("\n([^\n\"]*)void[ \t]+\"", "\n$1void \"" + TEST_CASE_BLOCK_TAG)
            .replaceAll("\n([^\n\"]*)void[ \t]+'", "\n$1void '" + TEST_CASE_BLOCK_TAG)
        ;
    }

    public static List<Path> getFilesFromArgs(String[] args) {
        List<Path> files;
        if (args.length == 0) {
            var cwd = Paths.get("").toAbsolutePath();
            writeSystemInfo(SystemInfoType.CWD, cwd.toString());
            writeSystemInfo(SystemInfoType.REPO, cwd.getFileName().toString());
            System.out.println("no arg - using current dir: " + cwd);
            files = findGroovyFiles(cwd);
        } else {
            System.out.println("got " + args.length + " args: " + List.of(args));
            files = new ArrayList<>();
            for (int i = 1; i < args.length; i++) {
                files.add(Path.of(args[i]));
            }
        }
        System.out.println("files: " + files);
        return files;
    }

    private static List<Path> findGroovyFiles(Path startDir) {
        System.out.println("looking for Groovy files in " + startDir);
        List<Path> groovyFiles = new ArrayList<>();
        try {
            Files.walkFileTree(startDir, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.toString().endsWith(".groovy")) {
                        groovyFiles.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    System.err.println("Failed to access: " + file.toString());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return groovyFiles;
    }

    public static String escapeString(String str) {
        return str
            .replaceAll(",", "<!COMMA!>")
            .replaceAll("`", "<!BACKTICK!>");
    }

    public static String cleanMethodName(SpockVisitor visitor) {
        final var currentMethod = escapeString(visitor.currentMethod);
        return visitor.filePath.hashCode() + ":" + visitor.filePath.getFileName() + ":" + currentMethod;
    }

}
