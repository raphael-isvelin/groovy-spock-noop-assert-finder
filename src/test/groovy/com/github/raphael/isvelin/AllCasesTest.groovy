package com.github.raphael.isvelin

import spock.lang.Specification

import static com.github.raphael.isvelin.helpers.TestExecutor.runTestCase

class AllCasesTest extends Specification {

    def "true positive cases"() {
        when:
            def testCase = runTestCase("test-case/true-positive/" + casePath)

        then:
            testCase.expectedResult == testCase.result

        where:
            casePath << [
                "GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy",
                "NeedsAtLeastOneBlockToMakeItATestCase.case.groovy",
                "ThrownExceptionIsTooGeneric.case.groovy",
                "MockWithExplicitInvocationCount_SmellInThenBlock.case.groovy",
                "ImplicitReturnsFun.case.groovy",
                "NeedsAssertInRegularMethod.case.groovy",
                "MethodsReturningBooleanAndStartingByAssertIsASmell.case.groovy",
                "DontSkipAssertJMethodsIfNotImported.case.groovy",
                "NonAssertedInEventuallyReturnsSpecificWarning.case.groovy",
            ]
    }

    def "true negative cases"() {
        when:
            def testCase = runTestCase("test-case/true-negative/" + casePath)

        then:
            testCase.expectedResult == testCase.result

        where:
            casePath << [
                "GroovyBooleanReturningMethods_ThenBlock.case.groovy",
                "NonExplicitBlockDefaultsToThen.case.groovy",
                "ThrownExceptionProbablyOkayIfStored.case.groovy",
                "MockWithExplicitInvocationCount_OkayOutsideOfThen.case.groovy",
                "WeirdlyFormattedTestNamesAreAccepted.case.groovy",
                "CallingAMethodFromWithinAMethod.case.groovy",
                "SkipAssertJMethodsIfHasImport.case.groovy",
                "GroovyBooleanReturningMethods_VerifyAndWithBlocks.case.groovy",
                "FilterAndMapImplicitReturnIsAllowed.case.groovy",
                "MockCaptureParamIsAsserting.case.groovy",
                "AwaitilityAllowImplicitReturnBinaryExpression.case.groovy",
                "ClosureParamsCheckOfMocksWithNoInvocationCountCheckAreIgnored.case.groovy",
            ]
    }

    def "false positive cases"() {
        when:
            def testCase = runTestCase("test-case/false-positive/" + casePath)

        then:
            testCase.expectedResult == testCase.result

        where:
            casePath << [
                "MocksReturningPropertyAreDetected.case.groovy",
                "ImplicitReturnsShouldBeAcceptedForCollect.case.groovy",
            ]
    }

    def "false negative cases"() {
        when:
            def testCase = runTestCase("test-case/false-negative/" + casePath)

        then:
            testCase.expectedResult == testCase.result

        where:
            casePath << [
                "CamelCaseNamedTestsArentDetectedWithImplicitThenBlock.case.groovy",
                "NonAssertedPropertiesInEventuallyReturnsSpecificWarning.case.groovy",
            ]
    }

    def "other cases"() {
        when:
            def testCase = runTestCase("test-case/misc/" + casePath)

        then:
            testCase.expectedResult == testCase.result

        where:
            casePath << [
                "GivenThenEtcBlocks.case.groovy",
                "Combined.case.groovy",
                "MoreStuff.case.groovy",
            ]
    }
}
