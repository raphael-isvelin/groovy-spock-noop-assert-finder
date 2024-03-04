//#name=assert is needed in a regular method
//#template=INSERT_IN_CLASS
//<code>
void assertVoid() {
    println "///assert needed in method - TRUE POSITIVE"
    1 == 2
    println "///assert is used as it should - TRUE NEGATIVE"
    assert 1 == 2
}
//</code>
/*<expected>
!!!FILE_OPEN,NeedsAssertInRegularMethod.case.groovy,test-case/true-positive/NeedsAssertInRegularMethod.case.groovy
!!!VISIT_METHOD,1236761230:NeedsAssertInRegularMethod.case.groovy:assertVoid
>>>ERROR,NeedsAssertInRegularMethod.case.groovy,1236761230:NeedsAssertInRegularMethod.case.groovy:assertVoid,8,OTHER_METHOD,BinaryExpression,(1 == 2)
</expected>*/
