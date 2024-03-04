//#name=boolean-returning methods starting by assert* is a smell
//#template=INSERT_IN_CLASS
//<code>
boolean assertBoolean() {
    println "///(above^) returning a bool in a method called assert* is laying the ground for future mistakes - TRUE POSITIVE"
    println "///returning a binary expression might indicate an issue (MAYBE) - TRUE POSITIVE"
    return 1 == 2
}
//</code>
/*<expected>
!!!FILE_OPEN,MethodsReturningBooleanAndStartingByAssertIsASmell.case.groovy,test-case/true-positive/MethodsReturningBooleanAndStartingByAssertIsASmell.case.groovy
>>>SMELL,MethodsReturningBooleanAndStartingByAssertIsASmell.case.groovy,-241355207:MethodsReturningBooleanAndStartingByAssertIsASmell.case.groovy:-,6,OTHER_METHOD,,method 'assertBoolean' starts by assert* but returns a boolean<!COMMA!> this is laying the ground for future mistakes
!!!VISIT_METHOD,-241355207:MethodsReturningBooleanAndStartingByAssertIsASmell.case.groovy:assertBoolean
>>>MAYBE,MethodsReturningBooleanAndStartingByAssertIsASmell.case.groovy,-241355207:MethodsReturningBooleanAndStartingByAssertIsASmell.case.groovy:assertBoolean,9,OTHER_METHOD,ReturnedBinaryExpression,(1 == 2)
</expected>*/
