//#name=more stuff
//#template=INSERT_IN_CLASS
//<code>
def "some stuff"() {
    given:
        1 * client.placeOrder(order.reference, order.assetId, order.amount) >> {
            orderPlaced.set(true)
            println "///returned property (MAYBE) - TRUE POSITIVE"
            return order.reference
        }

        println "///find should be ignored - FALSE POSITIVE"
        verifyAll(result.find {it.currency == HUF }) {
            println "///verifyAll is asserting - TRUE NEGATIVE"
            value == priceValue.amount.value
            currency == HUF
        }

        println "///needs assert outside of then/expect - TRUE POSITIVE"
        assessments == [SOME_ASSESSMENT]

    when: "there are no entries in the knowledge assessment entry repository"
        def assessments = getAssessments.execute(PROFILE_ID)

    then: "challenge the customer"
        println "///assert not needed in then block - TRUE NEGATIVE"
        assessments == [ASSESSMENT]
}
//</code>
/*<expected>
!!!FILE_OPEN,MoreStuff.case.groovy,test-case/misc/MoreStuff.case.groovy
!!!VISIT_METHOD,-635127618:MoreStuff.case.groovy:some stuff
>>>MAYBE,MoreStuff.case.groovy,-635127618:MoreStuff.case.groovy:some stuff,11,MOCK_CLOSURE,ReturnedPropertyExpression,order.reference
>>>MAYBE,MoreStuff.case.groovy,-635127618:MoreStuff.case.groovy:some stuff,15,OTHER_METHOD,BinaryExpression,(it.currency == HUF)
>>>ERROR,MoreStuff.case.groovy,-635127618:MoreStuff.case.groovy:some stuff,22,root:given,BinaryExpression,(assessments == [SOME_ASSESSMENT])
</expected>*/
