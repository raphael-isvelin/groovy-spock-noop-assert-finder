//#name=boolean-returning methods, assertion not needed at top-level then block
//#template=INSERT_IN_CLASS
//<code>
def "boolean-returning methods in verifyAll/verify/with - true negatives"() {
    given:
        def a = [1, 2]
        def b = Map.of("1", 1)

        verifyAll { itx ->
            println "///boolean-returning method called in then block - TRUE NEGATIVE"
            a.find(it -> it > 15)
            a.findAll(it -> it > 15)
            a.every(it -> it == 7)
            a.find { it > 15 }
            a.findAll { it > 15 }
            a.every { it == 7 }
            b.entrySet().stream().allMatch(e -> e.getValue() == 135)
            every {
                1 == 1
                5 == 6
            }
            any {
                1 == 4
                2 == 5
            }
        }
        with { itx ->
            println "///boolean-returning method called in then block - TRUE NEGATIVE"
            a.find(it -> it > 15)
            a.findAll(it -> it > 15)
            a.every(it -> it == 7)
            a.find { it > 15 }
            a.findAll { it > 15 }
            a.every { it == 7 }
            b.entrySet().stream().allMatch(e -> e.getValue() == 135)
            every {
                1 == 1
                5 == 6
            }
            any {
                1 == 4
                2 == 5
            }
        }

    expect:
        1 == 2
}
//</code>
/*<expected>
!!!FILE_OPEN,GroovyBooleanReturningMethods_VerifyAndWithBlocks.case.groovy,test-case/true-negative/GroovyBooleanReturningMethods_VerifyAndWithBlocks.case.groovy
!!!VISIT_METHOD,1728102020:GroovyBooleanReturningMethods_VerifyAndWithBlocks.case.groovy:boolean-returning methods in verifyAll/verify/with - true negatives
</expected>*/
