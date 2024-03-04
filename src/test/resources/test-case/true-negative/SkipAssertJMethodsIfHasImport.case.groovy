//#name=skip AssertJ asserting methods
//#template=NONE
//<code>
package com.github.raphael.isvelin

import spock.lang.Specification
import static org.assertj.core.api.Assertions.assertThat

class SomeTestCaseWith extends Specification {
    def "AssertJ assert methods"() {
        given:
            def a = [2, 5]
            ///shouldn't match the contains from the JUnit assert - TRUE NEGATIVE
            assertThat(a).contains(21)
        expect:
            ///shouldn't match the contains from the JUnit assert - TRUE NEGATIVE
            assertThat(a).contains(51)
    }
}
//</code>
/*<expected>
!!!FILE_OPEN,SkipAssertJMethodsIfHasImport.case.groovy,test-case/true-negative/SkipAssertJMethodsIfHasImport.case.groovy
!!!VISIT_METHOD,1058548244:SkipAssertJMethodsIfHasImport.case.groovy:AssertJ assert methods
</expected>*/
