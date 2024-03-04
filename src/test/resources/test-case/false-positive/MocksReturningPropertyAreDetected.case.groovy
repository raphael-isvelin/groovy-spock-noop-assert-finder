//#name=mocks returning property are detected
//#template=INSERT_IN_CLASS
//<code>
class Thing { String state }

def "mocks returning property are detected"() {
    given:
        def a = 1
    expect:
        1 * calculateState.execute(_) >> { Thing ib -> ib.state }
}
//</code>
/*<expected>
!!!FILE_OPEN,MocksReturningPropertyAreDetected.case.groovy,test-case/false-positive/MocksReturningPropertyAreDetected.case.groovy
!!!VISIT_METHOD,896500846:MocksReturningPropertyAreDetected.case.groovy:mocks returning property are detected
>>>ERROR,MocksReturningPropertyAreDetected.case.groovy,896500846:MocksReturningPropertyAreDetected.case.groovy:mocks returning property are detected,12,MOCK_CLOSURE,PropertyExpression,ib.state
</expected>*/
