//#name=don't skip AssertJ asserting methods if import not found
//#template=NONE
//<code>
package com.github.raphael.isvelin

import spock.lang.Specification

class SomeTestCaseWithout extends Specification {
    def "AssertJ assert methods"() {
        given:
            def a = [2, 5]
            ///should match the contains from the JUnit assert - TRUE POSITIVE
            assertThat(a).contains(21)
        expect:
            ///shouldn't match the contains as it's in then block anyway - TRUE NEGATIVE
            assertThat(a).contains(51)
    }
}
//</code>
/*<expected>
!!!FILE_OPEN,DontSkipAssertJMethodsIfNotImported.case.groovy,test-case/true-positive/DontSkipAssertJMethodsIfNotImported.case.groovy
!!!VISIT_METHOD,1205431647:DontSkipAssertJMethodsIfNotImported.case.groovy:AssertJ assert methods
>>>WARN,DontSkipAssertJMethodsIfNotImported.case.groovy,1205431647:DontSkipAssertJMethodsIfNotImported.case.groovy:AssertJ assert methods,10,root:given,MethodCallExpression,this.assertThat(a).contains(21)
</expected>*/
