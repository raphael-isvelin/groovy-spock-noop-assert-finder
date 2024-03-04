//#name=mock without explicit invocation count is a smell in a then block
//#template=INSERT_IN_CLASS
//<code>
def "mock without explicit count"() {
    given:
        def a = 1
    expect:
        println "///mock return value without explicit invocation count in then block (SMELL) - TRUE POSITIVE"
        aa.bb() >> "cc"
        println "///mock return value with explicit invocation count in then block - TRUE NEGATIVE"
        1 * aa.bb() >> "cc"
}
//</code>
/*<expected>
!!!FILE_OPEN,MockWithExplicitInvocationCount_SmellInThenBlock.case.groovy,test-case/true-positive/MockWithExplicitInvocationCount_SmellInThenBlock.case.groovy
!!!VISIT_METHOD,-1604929603:MockWithExplicitInvocationCount_SmellInThenBlock.case.groovy:mock without explicit count
>>>SMELL,MockWithExplicitInvocationCount_SmellInThenBlock.case.groovy,-1604929603:MockWithExplicitInvocationCount_SmellInThenBlock.case.groovy:mock without explicit count,11,root:then,,Not explicitly checking the number of invocations (e.g. 1*) can lead to hidden errors<!COMMA!> esp. if no return value is checked in the test; and if it's only setting up values<!COMMA!> it belongs to a 'given' block
</expected>*/
