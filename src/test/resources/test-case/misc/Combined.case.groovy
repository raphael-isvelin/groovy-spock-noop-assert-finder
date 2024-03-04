//#name=combined test
//#template=INSERT_IN_CLASS
//<code>
class ThingProvider2 { String name() {} }
class Thing2 {}

def "combined"() {
    given:
        def someVar = false
        def logsList = [ [level: "ERROR", isTrue: true] ]

    when:
        def results = [
                [
                        "a": caller.callOtherClass(),
                        "b": caller.callOtherClass() * 2
                ],
                [
                        "a": caller.callOtherClass(),
                        "b": caller.callOtherClass() * 2
                ]
        ]

    then:
        1 * methods.doSomething() >> {
            println "///eq, assert needed in mock closure - TRUE POSITIVE"
            1 == 2
            println "///gt, assert needed in mock closure - TRUE POSITIVE"
            1 > 2
            println "///lt, assert needed in mock closure - TRUE POSITIVE"
            2 < 1
            println "///gte, assert needed in mock closure - TRUE POSITIVE"
            1 >= 2
            println "///lte, assert needed in mock closure - TRUE POSITIVE"
            2 <= 1
            println "///ne, assert needed in mock closure - TRUE POSITIVE"
            1 != 1
            println "///not, assert needed in mock closure - FALSE NEGATIVE"
            !true
            println "///bool, assert needed in mock closure - FALSE NEGATIVE"
            false
            println "///not var, assert needed in mock closure - FALSE NEGATIVE"
            !someVar
            println "///bool var, assert needed in mock closure - FALSE NEGATIVE"
            someVar
            println "///method call should ideally be contextual [ignore bool==BAD] - FALSE NEGATIVE"
            logsList.get(0)
            println "///another var, assert needed in mock closure - TRUE POSITIVE"
            logsList.get(0).isTrue
            println "///not another var, assert needed in mock closure - TRUE POSITIVE"
            !logsList.get(0).isTrue
            println "///assert is needed, no error - TRUE NEGATIVE"
            assert 1 == 1
            println "<<ret>>"
        }

        methods.doSomething() >> {
            println "///also detected in mock without 1* - TRUE POSITIVE"
            1 == 2
        }

        if (true) {
            println "///assert needed in a condition - TRUE POSITIVE"
            1 == 2
            println "///assert is there, no error - TRUE NEGATIVE"
            assert 2 == 2
        }

        if (true) {
            println "///some methods known to return true, likely need to be asserted (WARN) - TRUE POSITIVE"
            results.equals(888)
            results.contains(999)
            Optional.empty().isPresent()
            Optional.of(results).isEmpty()
            new ArrayList<>(List.of(1L)).isEmpty()
            println "///true-returning method is asserted - TRUE NEGATIVE"
            assert results.equals(888)
        }

        if (true) {
            println "///multi-line object works too - TRUE POSITIVE"
            results == [
                    111,
                    [
                            "complex": 123,
                            "object": 456
                    ]
            ]
        }

        for (result in results) {
            println "///assert needed in a loop - TRUE POSITIVE"
            result == 111
        }

        results.each {
            println "///assert needed in each (WARN) - TRUE POSITIVE"
            it == 111
        }

        println "///method void (parsed separately) - TRUE NEGATIVE"
        assertVoid()

        if (true) {
            println "///method returns boolean, but result is unused - FALSE NEGATIVE"
            assertBoolean()
        }

        println "///nested method (parsed separately) - TRUE NEGATIVE"
        nestedMethod()

        results.each {
            verifyAll(it) {
                println "nested each->verify, the latter is asserting - TRUE NEGATIVE"
                a != 123
            }
        }

        results.each {
            with(it) { it2 ->
                println "///results->with, the latter is asserting - TRUE NEGATIVE"
                it2 != [a: 123, b: 246]
            }
        }

        println "///nested asserting methods and closure params - TRUE NEGATIVE"
        verifyAll(messages.find {it.thingId == source.thingId && it.creationTime.truncatedTo(ChronoUnit.MILLIS) == NOW.truncatedTo(ChronoUnit.MILLIS) }) {
            profileId == event.profileId
        }


        println "///closure params as cast in regular methods are asserting - TRUE NEGATIVE"
        1 * sender.send(
                VALUE_A,
                VALUE_B,
                request.getCode(),
                { Thing2 t -> t == thing } as Thing2,
                { ThingProvider2 w -> w.name() == "X" } as ThingProvider2
        )
}
//</code>
/*<expected>
!!!FILE_OPEN,Combined.case.groovy,test-case/misc/Combined.case.groovy
!!!VISIT_METHOD,437888770:Combined.case.groovy:combined
>>>ERROR,Combined.case.groovy,437888770:Combined.case.groovy:combined,29,MOCK_CLOSURE,BinaryExpression,(1 == 2)
>>>ERROR,Combined.case.groovy,437888770:Combined.case.groovy:combined,31,MOCK_CLOSURE,BinaryExpression,(1 > 2)
>>>ERROR,Combined.case.groovy,437888770:Combined.case.groovy:combined,33,MOCK_CLOSURE,BinaryExpression,(2 < 1)
>>>ERROR,Combined.case.groovy,437888770:Combined.case.groovy:combined,35,MOCK_CLOSURE,BinaryExpression,(1 >= 2)
>>>ERROR,Combined.case.groovy,437888770:Combined.case.groovy:combined,37,MOCK_CLOSURE,BinaryExpression,(2 <= 1)
>>>ERROR,Combined.case.groovy,437888770:Combined.case.groovy:combined,39,MOCK_CLOSURE,BinaryExpression,(1 != 1)
>>>ERROR,Combined.case.groovy,437888770:Combined.case.groovy:combined,51,MOCK_CLOSURE,PropertyExpression,logsList.get(0).isTrue
>>>ERROR,Combined.case.groovy,437888770:Combined.case.groovy:combined,53,MOCK_CLOSURE,NotPropertyExpression,!logsList.get(0).isTrue
>>>SMELL,Combined.case.groovy,437888770:Combined.case.groovy:combined,59,root:then,,Not explicitly checking the number of invocations (e.g. 1*) can lead to hidden errors<!COMMA!> esp. if no return value is checked in the test; and if it's only setting up values<!COMMA!> it belongs to a 'given' block
>>>ERROR,Combined.case.groovy,437888770:Combined.case.groovy:combined,61,MOCK_CLOSURE,BinaryExpression,(1 == 2)
>>>ERROR,Combined.case.groovy,437888770:Combined.case.groovy:combined,66,CONDITION_OR_LOOP,BinaryExpression,(1 == 2)
>>>WARN,Combined.case.groovy,437888770:Combined.case.groovy:combined,73,CONDITION_OR_LOOP,MethodCallExpression,results.equals(888)
>>>WARN,Combined.case.groovy,437888770:Combined.case.groovy:combined,74,CONDITION_OR_LOOP,MethodCallExpression,results.contains(999)
>>>WARN,Combined.case.groovy,437888770:Combined.case.groovy:combined,75,CONDITION_OR_LOOP,MethodCallExpression,Optional.empty().isPresent()
>>>WARN,Combined.case.groovy,437888770:Combined.case.groovy:combined,76,CONDITION_OR_LOOP,MethodCallExpression,Optional.of(results).isEmpty()
>>>WARN,Combined.case.groovy,437888770:Combined.case.groovy:combined,77,CONDITION_OR_LOOP,MethodCallExpression,new ArrayList(List.of(1)).isEmpty()
>>>ERROR,Combined.case.groovy,437888770:Combined.case.groovy:combined,84,CONDITION_OR_LOOP,BinaryExpression,(results == [111<!COMMA!> [complex:123<!COMMA!> object:456]])
>>>ERROR,Combined.case.groovy,437888770:Combined.case.groovy:combined,95,CONDITION_OR_LOOP,BinaryExpression,(result == 111)
>>>WARN,Combined.case.groovy,437888770:Combined.case.groovy:combined,100,OTHER_METHOD,BinaryExpression,(it == 111)
!!!VISIT_METHOD,437888770:Combined.case.groovy:name
</expected>*/
