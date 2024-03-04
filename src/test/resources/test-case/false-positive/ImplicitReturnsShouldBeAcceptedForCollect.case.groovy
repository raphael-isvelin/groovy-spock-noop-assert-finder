//#name=implicit returns of properties should be accepted for collect
//#template=INSERT_IN_CLASS
//<code>
def "collecting params"() {
    given:
        Information.values().collect {
            println "///lonely PropertyExpression is actually an implicit return statement (MAYBE) - FALSE POSITIVE"
            it.fieldName
        }
        Information.values().collect {
            println "///lonely !PropertyExpression is actually an implicit return statement (MAYBE) - FALSE POSITIVE"
            !it.fieldName
        }

    when:
        println "///implicit return statement lambda edition (MAYBE) - FALSE POSITIVE"
        account << getParameters().collect(it -> it.account)

        account << getParameters().collect(it -> {
            println "///implicit return statement (MAYBE) - FALSE POSITIVE"
            it.account
        })

        println "///implicit ! return statement lambda edition (MAYBE) - FALSE POSITIVE"
        account << getParameters().collect(it -> !it.account)

        account << getParameters().collect(it -> {
            println "///implicit ! return statement (MAYBE) - FALSE POSITIVE"
            !it.account
        })

    then:
        println "yay"
}
//</code>
/*<expected>
!!!FILE_OPEN,ImplicitReturnsShouldBeAcceptedForCollect.case.groovy,test-case/false-positive/ImplicitReturnsShouldBeAcceptedForCollect.case.groovy
!!!VISIT_METHOD,-1965511374:ImplicitReturnsShouldBeAcceptedForCollect.case.groovy:collecting params
>>>MAYBE,ImplicitReturnsShouldBeAcceptedForCollect.case.groovy,-1965511374:ImplicitReturnsShouldBeAcceptedForCollect.case.groovy:collecting params,10,OTHER_METHOD,PropertyExpression,it.fieldName
>>>MAYBE,ImplicitReturnsShouldBeAcceptedForCollect.case.groovy,-1965511374:ImplicitReturnsShouldBeAcceptedForCollect.case.groovy:collecting params,14,OTHER_METHOD,NotPropertyExpression,!it.fieldName
>>>MAYBE,ImplicitReturnsShouldBeAcceptedForCollect.case.groovy,-1965511374:ImplicitReturnsShouldBeAcceptedForCollect.case.groovy:collecting params,19,OTHER_METHOD,PropertyExpression,it.account
>>>MAYBE,ImplicitReturnsShouldBeAcceptedForCollect.case.groovy,-1965511374:ImplicitReturnsShouldBeAcceptedForCollect.case.groovy:collecting params,23,OTHER_METHOD,PropertyExpression,it.account
>>>MAYBE,ImplicitReturnsShouldBeAcceptedForCollect.case.groovy,-1965511374:ImplicitReturnsShouldBeAcceptedForCollect.case.groovy:collecting params,27,OTHER_METHOD,NotExpression,it.account
>>>MAYBE,ImplicitReturnsShouldBeAcceptedForCollect.case.groovy,-1965511374:ImplicitReturnsShouldBeAcceptedForCollect.case.groovy:collecting params,31,OTHER_METHOD,NotPropertyExpression,!it.account
</expected>*/
