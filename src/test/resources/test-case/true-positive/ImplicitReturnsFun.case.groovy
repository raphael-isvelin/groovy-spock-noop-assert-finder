//#name=implicit returns fun
//#template=INSERT_IN_CLASS
//<code>
def "sixth circle `of Hell: implicit returns"() {
    given:
        AdditionalInformation.values().someMethod {
            println "///lonely PropertyExpression :'( - TRUE POSITIVE"
            it.fa

            println "///lonely PropertyExpression is actually an implicit return statement (MAYBE) - TRUE POSITIVE"
            it.fieldName
        }
        AdditionalInformation.values().someMethod {
            println "///lonely !PropertyExpression :'( - TRUE POSITIVE"
            !it.fa

            println "///lonely !PropertyExpression is actually an implicit return statement (MAYBE) - TRUE POSITIVE"
            !it.fieldName
        }

        AdditionalInformation.values().someMethod {
            println "///lonely BinaryExpression :'( - TRUE POSITIVE"
            1 == 2

            println "///lonely BinaryExpression is actually an implicit return statement (WARN) - TRUE POSITIVE"
            5 == 1
        }

    when:
        println "///implicit return statement lambda edition (MAYBE) - TRUE POSITIVE"
        account << getDescriptiveErrorMessageParameters().someMethod(it -> it.account)

        account << getDescriptiveErrorMessageParameters().someMethod(it -> {
            println "///another lonely PropertyExpression :'( - TRUE POSITIVE"
            it.nope

            println "///implicit return statement (MAYBE) - TRUE POSITIVE"
            it.account
        })

        println "///implicit ! return statement lambda edition (MAYBE) - TRUE POSITIVE"
        account << getDescriptiveErrorMessageParameters().someMethod(it -> !it.account)

        account << getDescriptiveErrorMessageParameters().someMethod(it -> {
            println "///another lonely !PropertyExpression :'( - TRUE POSITIVE"
            !it.nope

            println "///implicit ! return statement (MAYBE) - TRUE POSITIVE"
            !it.account
        })

        println "///implicit return statement (BinaryExpression) lambda edition (WARN) - TRUE POSITIVE"
        account << getDescriptiveErrorMessageParameters().someMethod(it -> it.account == 345)

        account << getDescriptiveErrorMessageParameters().someMethod(it -> {
            println "///another lonely BinaryExpression :'( - TRUE POSITIVE"
            6 == 7

            println "///another implicit return statement (BinaryExpression) (WARN) - TRUE POSITIVE"
            7 == 8
        })

    then:
        println "yay"
}
//</code>
/*<expected>
!!!FILE_OPEN,ImplicitReturnsFun.case.groovy,test-case/true-positive/ImplicitReturnsFun.case.groovy
!!!VISIT_METHOD,420297946:ImplicitReturnsFun.case.groovy:sixth circle <!BACKTICK!>of Hell: implicit returns
>>>ERROR,ImplicitReturnsFun.case.groovy,420297946:ImplicitReturnsFun.case.groovy:sixth circle <!BACKTICK!>of Hell: implicit returns,10,OTHER_METHOD,PropertyExpression,it.fa
>>>MAYBE,ImplicitReturnsFun.case.groovy,420297946:ImplicitReturnsFun.case.groovy:sixth circle <!BACKTICK!>of Hell: implicit returns,13,OTHER_METHOD,PropertyExpression,it.fieldName
>>>ERROR,ImplicitReturnsFun.case.groovy,420297946:ImplicitReturnsFun.case.groovy:sixth circle <!BACKTICK!>of Hell: implicit returns,17,OTHER_METHOD,NotPropertyExpression,!it.fa
>>>MAYBE,ImplicitReturnsFun.case.groovy,420297946:ImplicitReturnsFun.case.groovy:sixth circle <!BACKTICK!>of Hell: implicit returns,20,OTHER_METHOD,NotPropertyExpression,!it.fieldName
>>>ERROR,ImplicitReturnsFun.case.groovy,420297946:ImplicitReturnsFun.case.groovy:sixth circle <!BACKTICK!>of Hell: implicit returns,25,OTHER_METHOD,BinaryExpression,(1 == 2)
>>>WARN,ImplicitReturnsFun.case.groovy,420297946:ImplicitReturnsFun.case.groovy:sixth circle <!BACKTICK!>of Hell: implicit returns,28,OTHER_METHOD,BinaryExpression,(5 == 1)
>>>MAYBE,ImplicitReturnsFun.case.groovy,420297946:ImplicitReturnsFun.case.groovy:sixth circle <!BACKTICK!>of Hell: implicit returns,33,OTHER_METHOD,PropertyExpression,it.account
>>>ERROR,ImplicitReturnsFun.case.groovy,420297946:ImplicitReturnsFun.case.groovy:sixth circle <!BACKTICK!>of Hell: implicit returns,37,OTHER_METHOD,PropertyExpression,it.nope
>>>MAYBE,ImplicitReturnsFun.case.groovy,420297946:ImplicitReturnsFun.case.groovy:sixth circle <!BACKTICK!>of Hell: implicit returns,40,OTHER_METHOD,PropertyExpression,it.account
>>>MAYBE,ImplicitReturnsFun.case.groovy,420297946:ImplicitReturnsFun.case.groovy:sixth circle <!BACKTICK!>of Hell: implicit returns,44,OTHER_METHOD,NotExpression,it.account
>>>ERROR,ImplicitReturnsFun.case.groovy,420297946:ImplicitReturnsFun.case.groovy:sixth circle <!BACKTICK!>of Hell: implicit returns,48,OTHER_METHOD,NotPropertyExpression,!it.nope
>>>MAYBE,ImplicitReturnsFun.case.groovy,420297946:ImplicitReturnsFun.case.groovy:sixth circle <!BACKTICK!>of Hell: implicit returns,51,OTHER_METHOD,NotPropertyExpression,!it.account
>>>WARN,ImplicitReturnsFun.case.groovy,420297946:ImplicitReturnsFun.case.groovy:sixth circle <!BACKTICK!>of Hell: implicit returns,55,OTHER_METHOD,BinaryExpression,(it.account == 345)
>>>ERROR,ImplicitReturnsFun.case.groovy,420297946:ImplicitReturnsFun.case.groovy:sixth circle <!BACKTICK!>of Hell: implicit returns,59,OTHER_METHOD,BinaryExpression,(6 == 7)
>>>WARN,ImplicitReturnsFun.case.groovy,420297946:ImplicitReturnsFun.case.groovy:sixth circle <!BACKTICK!>of Hell: implicit returns,62,OTHER_METHOD,BinaryExpression,(7 == 8)
</expected>*/
