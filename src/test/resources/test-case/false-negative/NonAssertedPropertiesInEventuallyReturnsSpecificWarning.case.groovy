//#name=non-asserted properties in eventually returns specific warn message
//#template=INSERT_IN_CLASS
//<code>
def "non-asserted properties in eventually returns specific warn message"() {
    given:
        def a = 1
    then:
        new PollingConditions(timeout: 10).eventually {
            a
            a
        }
        new PollingConditions(timeout: 10).eventually {
            !a
            !a
        }
        new PollingConditions(timeout: 10).within {
            a
            a
        }
        new PollingConditions(timeout: 10).within {
            !a
            !a
        }
}
//</code>
/*<expected>
!!!FILE_OPEN,NonAssertedPropertiesInEventuallyReturnsSpecificWarning.case.groovy,test-case/false-negative/NonAssertedPropertiesInEventuallyReturnsSpecificWarning.case.groovy
!!!VISIT_METHOD,-288595434:NonAssertedPropertiesInEventuallyReturnsSpecificWarning.case.groovy:non-asserted properties in eventually returns specific warn message
</expected>*/
