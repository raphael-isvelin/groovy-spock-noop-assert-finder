//#name=needs at least one block to make it a test case
//#template=INSERT_IN_CLASS
//<code>
def "this is not a test case"() {
    println "needs a block e.g. given/where to be a test case - TRUE POSITIVE"
    1 == 2
}
//</code>
/*<expected>
!!!FILE_OPEN,NeedsAtLeastOneBlockToMakeItATestCase.case.groovy,test-case/true-positive/NeedsAtLeastOneBlockToMakeItATestCase.case.groovy
!!!VISIT_METHOD,-1994666862:NeedsAtLeastOneBlockToMakeItATestCase.case.groovy:this is not a test case
>>>ERROR,NeedsAtLeastOneBlockToMakeItATestCase.case.groovy,-1994666862:NeedsAtLeastOneBlockToMakeItATestCase.case.groovy:this is not a test case,6,CLASS,,Test case has no block (given/then/etc.)
</expected>*/
