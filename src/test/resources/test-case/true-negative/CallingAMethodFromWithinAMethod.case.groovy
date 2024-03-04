//#name=calling a method from within a method
//#template=INSERT_IN_CLASS
//<code>
void nestedMethod() {
    println "///should be fine - TRUE NEGATIVE"
    assertVoid()
}
//</code>
/*<expected>
!!!FILE_OPEN,CallingAMethodFromWithinAMethod.case.groovy,test-case/true-negative/CallingAMethodFromWithinAMethod.case.groovy
!!!VISIT_METHOD,1274275342:CallingAMethodFromWithinAMethod.case.groovy:nestedMethod
</expected>*/
