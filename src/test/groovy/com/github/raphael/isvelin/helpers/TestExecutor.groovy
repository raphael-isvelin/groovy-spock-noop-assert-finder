package com.github.raphael.isvelin.helpers

import com.github.raphael.isvelin.ATL
import com.github.raphael.isvelin.Helpers
import org.spockframework.util.Pair

import java.nio.file.Path

class TestExecutor {

    private static TestCaseFileParser parser = new TestCaseFileParser()

    static def runTestCase(String path) {
        def testCase = parser.parseGroovyFile(path)

        println "----- SOURCE CODE -----"
        def lines = testCase.code.split("\n")
        int maxLineNumberLength = (lines.size() - 1).toString().length()
        lines.eachWithIndex { line, index ->
            def paddedLineNumber = (index + 1).toString().padLeft(maxLineNumberLength, ' ')
            println "${paddedLineNumber}: ${line}"
        }
        println "-----------------------"

        Helpers.initReportFile()
        ATL.parseSourceCode(Pair.of(Path.of(path), testCase.code))

        def report = Helpers.closeAndReadReport()
        println "----- REPORT -----"
        println report
        println "------------------"
        return [
            result: report.trim(),
            expectedResult: testCase.expectedResult.trim()
        ]
    }
}
