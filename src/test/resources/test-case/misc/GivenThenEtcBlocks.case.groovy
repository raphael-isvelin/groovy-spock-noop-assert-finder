//#name=given, when, then, etc. blocks
//#template=INSERT_IN_CLASS
//<code>
def 'given and then blocks'() {
    given:
        println "///given, assert is needed - TRUE POSITIVE"
        1 == 2
    and:
        println "///still given, assert is needed - TRUE POSITIVE"
        2 == 3
    when:
        println "///still still given, assert is needed - TRUE POSITIVE"
        3 == 4
    then:
        println "///then, assert is not needed - TRUE NEGATIVE"
        10 == 11
    and:
        println "///then again, assert is not needed - TRUE NEGATIVE"
        11 == 12
    when:
        println "///back to given, assert is needed - TRUE POSITIVE"
        4 == 5
    expect:
        println "///back to then, assert is not needed - TRUE NEGATIVE"
        12 == 13
    where:
        println "///back to given again, assert is needed - TRUE POSITIVE"
        5 == 6
}

def "more blocks"() {
    setup:
        println "///setup acts as a given block - TRUE POSITIVE"
        def a = 1
        a == 5
    when:
        println "///when acts as a given block - TRUE POSITIVE"
        a == 7
    then:
        println "///then performs the assertions - TRUE NEGATIVE"
        a == 1
    cleanup:
        println "///cleanup acts as a given block - TRUE POSITIVE"
        a == 9
}
//</code>
/*<expected>
!!!FILE_OPEN,GivenThenEtcBlocks.case.groovy,test-case/misc/GivenThenEtcBlocks.case.groovy
!!!VISIT_METHOD,-55880995:GivenThenEtcBlocks.case.groovy:given and then blocks
>>>ERROR,GivenThenEtcBlocks.case.groovy,-55880995:GivenThenEtcBlocks.case.groovy:given and then blocks,9,root:given,BinaryExpression,(1 == 2)
>>>ERROR,GivenThenEtcBlocks.case.groovy,-55880995:GivenThenEtcBlocks.case.groovy:given and then blocks,12,root:given,BinaryExpression,(2 == 3)
>>>ERROR,GivenThenEtcBlocks.case.groovy,-55880995:GivenThenEtcBlocks.case.groovy:given and then blocks,15,root:given,BinaryExpression,(3 == 4)
>>>ERROR,GivenThenEtcBlocks.case.groovy,-55880995:GivenThenEtcBlocks.case.groovy:given and then blocks,24,root:given,BinaryExpression,(4 == 5)
>>>ERROR,GivenThenEtcBlocks.case.groovy,-55880995:GivenThenEtcBlocks.case.groovy:given and then blocks,30,root:given,BinaryExpression,(5 == 6)
!!!VISIT_METHOD,-55880995:GivenThenEtcBlocks.case.groovy:more blocks
>>>ERROR,GivenThenEtcBlocks.case.groovy,-55880995:GivenThenEtcBlocks.case.groovy:more blocks,37,root:given,BinaryExpression,(a == 5)
>>>ERROR,GivenThenEtcBlocks.case.groovy,-55880995:GivenThenEtcBlocks.case.groovy:more blocks,40,root:given,BinaryExpression,(a == 7)
>>>ERROR,GivenThenEtcBlocks.case.groovy,-55880995:GivenThenEtcBlocks.case.groovy:more blocks,46,root:given,BinaryExpression,(a == 9)
</expected>*/
