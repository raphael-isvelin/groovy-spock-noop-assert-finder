//#name=thrown(Exception) is probably okay if stored in a variable
//#template=INSERT_IN_CLASS
//<code>
def "probably okay: def e = thrown(Exception)"() {
    given:
        def a = 1
    when:
        def b = a / 0
    then:
        println "///def e = thrown(Exception)'s probably okay - TRUE NEGATIVE"
        def e = thrown(Exception)
}
//</code>
/*<expected>
!!!FILE_OPEN,ThrownExceptionProbablyOkayIfStored.case.groovy,test-case/true-negative/ThrownExceptionProbablyOkayIfStored.case.groovy
!!!VISIT_METHOD,-862933443:ThrownExceptionProbablyOkayIfStored.case.groovy:probably okay: def e = thrown(Exception)
</expected>*/
