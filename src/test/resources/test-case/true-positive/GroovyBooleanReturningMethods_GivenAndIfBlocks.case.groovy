//#name=boolean-returning methods should likely be asserting outside of a top-level then block
//#template=INSERT_IN_CLASS
//<code>
def "boolean-returning methods - true positives"() {
    given:
        println "///boolean-returning find should likely not be called at root level outside of then blocks - TRUE POSITIVE (WARN)"
        a.find(it -> it > 15)
        !a.find(it -> it > 15)
        a.find { it > 15 }
        b.entrySet().stream().allMatch(e -> e.getValue() == 135)

        println "///boolean-returning findAll should likely not be called at root level outside of then blocks - TRUE POSITIVE (WARN)"
        a.findAll(it -> it > 15)
        a.findAll { it > 15 }
        a.every(it -> it == 7)

        println "///boolean-returning every should likely not be called at root level outside of then blocks - TRUE POSITIVE (WARN)"
        a.every { it == 7 }
        every {
            1 == 6
            5 == 7
        }

        println "///boolean-returning any should likely not be called at root level outside of then blocks - TRUE POSITIVE (WARN)"
        any {
            1 == 4
            2 == 5
        }

        println "///asserting methods called anywhere - TRUE NEGATIVE"
        verifyAll {
            1 == 1 // good NOPE ('in ASSERTING_METHOD block')
            6 == 6 // good NOPE ('in ASSERTING_METHOD block')
        }
        with(a) {
            a.contains(40) // good NOPE ('in ASSERTING_METHOD block')
        }

    when:
        def h = 5 * 5

    then:
        if (true) {
            println "///boolean-returning find should likely not be called at root level outside of then blocks - TRUE POSITIVE (WARN)"
            a.find(it -> it > 15)
            !a.find(it -> it > 15)
            a.find { it > 15 }
            b.entrySet().stream().allMatch(e -> e.getValue() == 135)

            println "///boolean-returning findAll should likely not be called at root level outside of then blocks - TRUE POSITIVE (WARN)"
            a.findAll(it -> it > 15)
            a.findAll { it > 15 }
            a.every(it -> it == 7)

            println "///boolean-returning every should likely not be called at root level outside of then blocks - TRUE POSITIVE (WARN)"
            a.every { it == 7 }
            every {
                1 == 6
                5 == 7
            }

            println "///boolean-returning any should likely not be called at root level outside of then blocks - TRUE POSITIVE (WARN)"
            any {
                1 == 4
                2 == 5
            }

            println "///asserting methods called anywhere - TRUE NEGATIVE"
            verifyAll {
                1 == 1
                6 == 6
            }
            with(a) {
                a.contains(40)
            }
        }
}
//</code>
/*<expected>
!!!FILE_OPEN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,test-case/true-positive/GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy
!!!VISIT_METHOD,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,9,root:given,,boolean-returning Groovy method called outside of then block: a.find((java.lang.Object it) -> { ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,10,root:given,,boolean-returning Groovy method called outside of then block: a.find((java.lang.Object it) -> { ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,11,root:given,,boolean-returning Groovy method called outside of then block: a.find({ -> ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,12,root:given,,boolean-returning Groovy method called outside of then block: b.entrySet().stream().allMatch((java.lang.Object e) -> { ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,15,root:given,,boolean-returning Groovy method called outside of then block: a.findAll((java.lang.Object it) -> { ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,16,root:given,,boolean-returning Groovy method called outside of then block: a.findAll({ -> ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,17,root:given,,boolean-returning Groovy method called outside of then block: a.every((java.lang.Object it) -> { ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,20,root:given,,boolean-returning Groovy method called outside of then block: a.every({ -> ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,21,root:given,,boolean-returning Groovy method called outside of then block: this.every({ -> ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,27,root:given,,boolean-returning Groovy method called outside of then block: this.any({ -> ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,47,CONDITION_OR_LOOP,,boolean-returning Groovy method called outside of then block: a.find((java.lang.Object it) -> { ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,48,CONDITION_OR_LOOP,,boolean-returning Groovy method called outside of then block: a.find((java.lang.Object it) -> { ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,49,CONDITION_OR_LOOP,,boolean-returning Groovy method called outside of then block: a.find({ -> ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,50,CONDITION_OR_LOOP,,boolean-returning Groovy method called outside of then block: b.entrySet().stream().allMatch((java.lang.Object e) -> { ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,53,CONDITION_OR_LOOP,,boolean-returning Groovy method called outside of then block: a.findAll((java.lang.Object it) -> { ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,54,CONDITION_OR_LOOP,,boolean-returning Groovy method called outside of then block: a.findAll({ -> ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,55,CONDITION_OR_LOOP,,boolean-returning Groovy method called outside of then block: a.every((java.lang.Object it) -> { ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,58,CONDITION_OR_LOOP,,boolean-returning Groovy method called outside of then block: a.every({ -> ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,59,CONDITION_OR_LOOP,,boolean-returning Groovy method called outside of then block: this.every({ -> ... })
>>>WARN,GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy,770435803:GroovyBooleanReturningMethods_GivenAndIfBlocks.case.groovy:boolean-returning methods - true positives,65,CONDITION_OR_LOOP,,boolean-returning Groovy method called outside of then block: this.any({ -> ... })
</expected>*/
