//#name=non-asserted condition in eventually returns specific warn message
//#template=INSERT_IN_CLASS
//<code>
def "non-asserted condition in eventually returns specific warn message"() {
    given:
        def a = 1
    and:
        pollingConditions.eventually {
            things.any {thing -> thing == "SOMETHING" }
        }
    then:
        new PollingConditions(timeout: 10).eventually {
            a == 4567
            a == 123
        }
        new PollingConditions(timeout: 10).eventually {
            arr.contains(a)
            arr.contains(a)
        }
        new PollingConditions(timeout: 10).within {
            a == 4567
            a == 123
        }
        new PollingConditions(timeout: 10).within {
            arr.contains(a)
            arr.contains(a)
        }
}
//</code>
/*<expected>
!!!FILE_OPEN,NonAssertedInEventuallyReturnsSpecificWarning.case.groovy,test-case/true-positive/NonAssertedInEventuallyReturnsSpecificWarning.case.groovy
!!!VISIT_METHOD,740359540:NonAssertedInEventuallyReturnsSpecificWarning.case.groovy:non-asserted condition in eventually returns specific warn message
>>>SMELL,NonAssertedInEventuallyReturnsSpecificWarning.case.groovy,740359540:NonAssertedInEventuallyReturnsSpecificWarning.case.groovy:non-asserted condition in eventually returns specific warn message,11,OTHER_METHOD,,Warning! Avoiding assert keyword in the clojure is only possible if the conditions object type is known during compilation (no 'def' on the left side)<!COMMA!> and only from Spock 2.0. Consider always using assert for safety. Expression: things.any({ java.lang.Object thing -> ... })
>>>MAYBE,NonAssertedInEventuallyReturnsSpecificWarning.case.groovy,740359540:NonAssertedInEventuallyReturnsSpecificWarning.case.groovy:non-asserted condition in eventually returns specific warn message,11,OTHER_METHOD,BinaryExpression,(thing == SOMETHING)
>>>SMELL,NonAssertedInEventuallyReturnsSpecificWarning.case.groovy,740359540:NonAssertedInEventuallyReturnsSpecificWarning.case.groovy:non-asserted condition in eventually returns specific warn message,15,OTHER_METHOD,,Warning! Avoiding assert keyword in the clojure is only possible if the conditions object type is known during compilation (no 'def' on the left side)<!COMMA!> and only from Spock 2.0. Consider always using assert for safety. Expression: (a == 4567)
>>>SMELL,NonAssertedInEventuallyReturnsSpecificWarning.case.groovy,740359540:NonAssertedInEventuallyReturnsSpecificWarning.case.groovy:non-asserted condition in eventually returns specific warn message,16,OTHER_METHOD,,Warning! Avoiding assert keyword in the clojure is only possible if the conditions object type is known during compilation (no 'def' on the left side)<!COMMA!> and only from Spock 2.0. Consider always using assert for safety. Expression: (a == 123)
>>>SMELL,NonAssertedInEventuallyReturnsSpecificWarning.case.groovy,740359540:NonAssertedInEventuallyReturnsSpecificWarning.case.groovy:non-asserted condition in eventually returns specific warn message,19,OTHER_METHOD,,Warning! Avoiding assert keyword in the clojure is only possible if the conditions object type is known during compilation (no 'def' on the left side)<!COMMA!> and only from Spock 2.0. Consider always using assert for safety. Expression: arr.contains(a)
>>>SMELL,NonAssertedInEventuallyReturnsSpecificWarning.case.groovy,740359540:NonAssertedInEventuallyReturnsSpecificWarning.case.groovy:non-asserted condition in eventually returns specific warn message,20,OTHER_METHOD,,Warning! Avoiding assert keyword in the clojure is only possible if the conditions object type is known during compilation (no 'def' on the left side)<!COMMA!> and only from Spock 2.0. Consider always using assert for safety. Expression: arr.contains(a)
>>>SMELL,NonAssertedInEventuallyReturnsSpecificWarning.case.groovy,740359540:NonAssertedInEventuallyReturnsSpecificWarning.case.groovy:non-asserted condition in eventually returns specific warn message,23,OTHER_METHOD,,Warning! Avoiding assert keyword in the clojure is only possible if the conditions object type is known during compilation (no 'def' on the left side)<!COMMA!> and only from Spock 2.0. Consider always using assert for safety. Expression: (a == 4567)
>>>SMELL,NonAssertedInEventuallyReturnsSpecificWarning.case.groovy,740359540:NonAssertedInEventuallyReturnsSpecificWarning.case.groovy:non-asserted condition in eventually returns specific warn message,24,OTHER_METHOD,,Warning! Avoiding assert keyword in the clojure is only possible if the conditions object type is known during compilation (no 'def' on the left side)<!COMMA!> and only from Spock 2.0. Consider always using assert for safety. Expression: (a == 123)
>>>SMELL,NonAssertedInEventuallyReturnsSpecificWarning.case.groovy,740359540:NonAssertedInEventuallyReturnsSpecificWarning.case.groovy:non-asserted condition in eventually returns specific warn message,27,OTHER_METHOD,,Warning! Avoiding assert keyword in the clojure is only possible if the conditions object type is known during compilation (no 'def' on the left side)<!COMMA!> and only from Spock 2.0. Consider always using assert for safety. Expression: arr.contains(a)
>>>SMELL,NonAssertedInEventuallyReturnsSpecificWarning.case.groovy,740359540:NonAssertedInEventuallyReturnsSpecificWarning.case.groovy:non-asserted condition in eventually returns specific warn message,28,OTHER_METHOD,,Warning! Avoiding assert keyword in the clojure is only possible if the conditions object type is known during compilation (no 'def' on the left side)<!COMMA!> and only from Spock 2.0. Consider always using assert for safety. Expression: arr.contains(a)
</expected>*/
