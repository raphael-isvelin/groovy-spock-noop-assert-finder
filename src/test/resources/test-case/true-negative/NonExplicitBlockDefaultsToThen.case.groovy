//#name=boolean-returning methods, assertion not needed at top-level then block
//#template=INSERT_IN_CLASS
//<code>
def "boolean-returning methods - true negatives"() {
    given:
        def a = 1

    when:
        def b = a * a

    then:
        println "///boolean-returning method called in then block - TRUE NEGATIVE"
        a.find(it -> it > 15)
        a.findAll(it -> it > 15)
        a.every(it -> it == 7)
        a.find { it > 15 }
        a.findAll { it > 15 }
        a.every { it == 7 }
        every {
            1 == 1
            5 == 6
        }
        any {
            1 == 4
            2 == 5
        }
}
//</code>
/*<expected>
!!!FILE_OPEN,NonExplicitBlockDefaultsToThen.case.groovy,test-case/true-negative/NonExplicitBlockDefaultsToThen.case.groovy
!!!VISIT_METHOD,-721985453:NonExplicitBlockDefaultsToThen.case.groovy:boolean-returning methods - true negatives
</expected>*/
