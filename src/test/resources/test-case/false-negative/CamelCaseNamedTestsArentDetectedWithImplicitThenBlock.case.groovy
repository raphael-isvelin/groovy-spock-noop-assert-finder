//#name=CamelCase-named tests aren't detected
//#template=INSERT_IN_CLASS
//<code>
def         moreTest() {
    println "///will match - method NOT understood as test case"
    1 == 1

    where:
    a << [1, 4]
}

void         moreTestVoid() {
    println "///will match - method NOT understood as test case"
        1 == 1

    where:
        a << [1, 4]
}
//</code>
/*<expected>
!!!FILE_OPEN,CamelCaseNamedTestsArentDetectedWithImplicitThenBlock.case.groovy,test-case/false-negative/CamelCaseNamedTestsArentDetectedWithImplicitThenBlock.case.groovy
!!!VISIT_METHOD,1097753801:CamelCaseNamedTestsArentDetectedWithImplicitThenBlock.case.groovy:moreTest
>>>ERROR,CamelCaseNamedTestsArentDetectedWithImplicitThenBlock.case.groovy,1097753801:CamelCaseNamedTestsArentDetectedWithImplicitThenBlock.case.groovy:moreTest,8,OTHER_METHOD,BinaryExpression,(1 == 1)
!!!VISIT_METHOD,1097753801:CamelCaseNamedTestsArentDetectedWithImplicitThenBlock.case.groovy:moreTestVoid
>>>ERROR,CamelCaseNamedTestsArentDetectedWithImplicitThenBlock.case.groovy,1097753801:CamelCaseNamedTestsArentDetectedWithImplicitThenBlock.case.groovy:moreTestVoid,16,OTHER_METHOD,BinaryExpression,(1 == 1)
</expected>*/
