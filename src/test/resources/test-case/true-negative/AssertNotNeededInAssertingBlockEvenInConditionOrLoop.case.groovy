//#name=assert is not needed in asserting block (e.g. verifyAll) even in condition or loop
//#template=INSERT_IN_CLASS
//<code>
def "assert is not needed in asserting block (e.g. verifyAll) even in condition or loop"() {
    given:
        def a = 1
        def b = [1, 2, 3]
    when:
        println 'test'
    then:
        verifyAll {
            if (true) {
                println "NOT NEEDED HERE"
                a == 4
                b.each {
                    println "ASSERT IS NEEDED HERE"
                    a == 67
                }
                for (int i = 0; i < 10; ++i) {
                    println "NOT NEEDED HERE"
                    a == 1234 + i
                }
            }
        }
}
//</code>
/*<expected>
!!!FILE_OPEN,AssertNotNeededInAssertingBlockEvenInConditionOrLoop.case.groovy,test-case/true-negative/AssertNotNeededInAssertingBlockEvenInConditionOrLoop.case.groovy
!!!VISIT_METHOD,1833966856:AssertNotNeededInAssertingBlockEvenInConditionOrLoop.case.groovy:assert is not needed in asserting block (e.g. verifyAll) even in condition or loop
>>>WARN,AssertNotNeededInAssertingBlockEvenInConditionOrLoop.case.groovy,1833966856:AssertNotNeededInAssertingBlockEvenInConditionOrLoop.case.groovy:assert is not needed in asserting block (e.g. verifyAll) even in condition or loop,19,OTHER_METHOD,BinaryExpression,(a == 67)
</expected>*/
