package com.github.raphael.isvelin;

import static com.github.raphael.isvelin.Helpers.addMetaDataToSourceCode;
import static com.github.raphael.isvelin.Helpers.closeReportFile;
import static com.github.raphael.isvelin.Helpers.getFilesFromArgs;
import static com.github.raphael.isvelin.Helpers.initReportFile;
import static com.github.raphael.isvelin.Helpers.readSourceCode;
import static com.github.raphael.isvelin.Helpers.writeMainBranchToReportFile;
import static com.github.raphael.isvelin.Helpers.writeSystemInfo;

import groovy.lang.GroovyClassLoader;
import java.nio.file.Path;
import java.util.List;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.Phases;
import org.spockframework.util.Pair;

public class ATL {

    public static void main(String[] args) {
        initReportFile();
        writeMainBranchToReportFile();

        List<Path> files = getFilesFromArgs(args);

        var fileWithCode = files.stream()
            .map(file -> Pair.of(file, addMetaDataToSourceCode(readSourceCode(file.toString()))))
            .filter(pair -> !pair.second().isEmpty()).toList();

        for (var pair : fileWithCode) {
            parseSourceCode(pair);
        }

        closeReportFile();
        System.out.println("done, wrote report to " + Helpers.REPORT_PATH);
    }

    public static void parseSourceCode(Pair<Path, String> fileWithSourceCode) {
        final var fileName = fileWithSourceCode.first().getFileName().toString();
        final var sourceCode = fileWithSourceCode.second();
        writeSystemInfo(SystemInfoType.FILE_OPEN, fileName + "," + fileWithSourceCode.first().toString());

        final var config = new CompilerConfiguration();
        config.setTargetDirectory("");

        try (final var classLoader = new GroovyClassLoader()) {
            final var compilationUnit = new CompilationUnit(config, null, classLoader);
            compilationUnit.addSource(fileName, sourceCode);
            compilationUnit.compile(Phases.CONVERSION);

            for (final var classNode : compilationUnit.getAST().getClasses()) {
                final var visitor = new SpockVisitor(fileWithSourceCode.first(), fileWithSourceCode.second());
                classNode.visitContents(visitor);
            }
        } catch (Exception e) {
            e.printStackTrace();
            final var message = e.getMessage() != null ? e.getMessage().replaceAll("\n", "<!NEWLINE!>").replaceAll("\r", "<!NEWLINE!>") : "NULL";
            writeSystemInfo(SystemInfoType.ERROR, message);
        }
    }
}
