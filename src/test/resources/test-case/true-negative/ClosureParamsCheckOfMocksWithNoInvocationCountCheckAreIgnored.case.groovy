//#name=closure params check of mocks with no invocation count checks are ignored
//#template=INSERT_IN_CLASS
//<code>
class SomeRequest { int something }
def 'closure params check of mocks with no invocation count checks are ignored'() {
    given:
        def a = 1

    when:
        def result = operation.operate()

    then:
        myMock.call({SomeRequest request ->
            request.something == 1
        }) >> something.do()
        myMock.call({SomeRequest request ->
            request.something == 1
        }) >> someVar
        myMock.call({SomeRequest request ->
            request.something == 1
        }) >> "oi"

        //and re-testing it with invocation count check
        1 * Mock.call({SomeRequest request ->
            request.something == 1
        }) >> something.do()
        1 * myMock.call({SomeRequest request ->
            request.something == 1
        }) >> someVar
        1 * myMock.call({SomeRequest request ->
            request.something == 1
        }) >> "oi"
}
//</code>
/*<expected>
!!!FILE_OPEN,ClosureParamsCheckOfMocksWithNoInvocationCountCheckAreIgnored.case.groovy,test-case/true-negative/ClosureParamsCheckOfMocksWithNoInvocationCountCheckAreIgnored.case.groovy
!!!VISIT_METHOD,172229048:ClosureParamsCheckOfMocksWithNoInvocationCountCheckAreIgnored.case.groovy:closure params check of mocks with no invocation count checks are ignored
>>>SMELL,ClosureParamsCheckOfMocksWithNoInvocationCountCheckAreIgnored.case.groovy,172229048:ClosureParamsCheckOfMocksWithNoInvocationCountCheckAreIgnored.case.groovy:closure params check of mocks with no invocation count checks are ignored,15,root:then,,Not explicitly checking the number of invocations (e.g. 1*) can lead to hidden errors<!COMMA!> esp. if no return value is checked in the test; and if it's only setting up values<!COMMA!> it belongs to a 'given' block
>>>SMELL,ClosureParamsCheckOfMocksWithNoInvocationCountCheckAreIgnored.case.groovy,172229048:ClosureParamsCheckOfMocksWithNoInvocationCountCheckAreIgnored.case.groovy:closure params check of mocks with no invocation count checks are ignored,18,root:then,,Not explicitly checking the number of invocations (e.g. 1*) can lead to hidden errors<!COMMA!> esp. if no return value is checked in the test; and if it's only setting up values<!COMMA!> it belongs to a 'given' block
>>>SMELL,ClosureParamsCheckOfMocksWithNoInvocationCountCheckAreIgnored.case.groovy,172229048:ClosureParamsCheckOfMocksWithNoInvocationCountCheckAreIgnored.case.groovy:closure params check of mocks with no invocation count checks are ignored,21,root:then,,Not explicitly checking the number of invocations (e.g. 1*) can lead to hidden errors<!COMMA!> esp. if no return value is checked in the test; and if it's only setting up values<!COMMA!> it belongs to a 'given' block
</expected>*/
