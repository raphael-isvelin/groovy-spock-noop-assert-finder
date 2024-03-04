//#name=mock without explicit invocation count is acceptable outside of a then block
//#template=INSERT_IN_CLASS
//<code>
def "mock without explicit count"() {
    given:
        println "///mock return value without explicit invocation count in given block - TRUE NEGATIVE"
        aa.bb() >> "cc"
        println "///mock return value with explicit invocation count in given block - TRUE NEGATIVE"
        1 * aa.bb() >> "cc"
    expect:
        1 == 1
}
//</code>
/*<expected>
!!!FILE_OPEN,MockWithExplicitInvocationCount_OkayOutsideOfThen.case.groovy,test-case/true-negative/MockWithExplicitInvocationCount_OkayOutsideOfThen.case.groovy
!!!VISIT_METHOD,-1470387008:MockWithExplicitInvocationCount_OkayOutsideOfThen.case.groovy:mock without explicit count
</expected>*/
