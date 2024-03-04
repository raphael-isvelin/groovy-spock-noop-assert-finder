//#name=thrown(Exception) is too generic
//#template=INSERT_IN_CLASS
//<code>
def "smell: thrown(Exception)"() {
    given:
    def a = 1
    when:
    def b = a / 0
    then:
    println "///thrown(Exception)'s bad (SMELL) - TRUE POSITIVE"
    thrown(Exception)
}
//</code>
/*<expected>
!!!FILE_OPEN,ThrownExceptionIsTooGeneric.case.groovy,test-case/true-positive/ThrownExceptionIsTooGeneric.case.groovy
!!!VISIT_METHOD,963133815:ThrownExceptionIsTooGeneric.case.groovy:smell: thrown(Exception)
>>>SMELL,ThrownExceptionIsTooGeneric.case.groovy,963133815:ThrownExceptionIsTooGeneric.case.groovy:smell: thrown(Exception),13,root:then,,thrown(Exception) is too generic and can hide other exceptions
</expected>*/
