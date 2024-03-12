package com.github.raphael.isvelin.helpers;

import static com.github.raphael.isvelin.Helpers.addMetaDataToSourceCode;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TestCase {
    public String name;
    public Template template;
    public String code;
    public String expectedResult;

    public enum Template {
        NONE("<<CODE>>", "<<CODE>>", 0),
        INSERT_IN_CLASS(
                """
package com.github.raphael.isvelin

import spock.lang.Specification

class SomeTestCase extends Specification {
<<CODE>>
}
                """,
                "<<CODE>>",
                4
        ),
        INSERT_IN_TEST_GIVEN(
                """
package com.github.raphael.isvelin

import spock.lang.Specification

class SomeTestCase extends Specification {
    def "template test given"() {
        given:
<<CODE>>

        expect:
            1 == 1
    }
}
                """,
                "<<CODE>>",
                12
        ),
        INSERT_IN_TEST_THEN(
                """
package com.github.raphael.isvelin

import spock.lang.Specification

class SomeTestCase extends Specification {
    def "template test then"() {
        given:
            def a = 1

        when:
            def b = a * a

        then:
<<CODE>>
    }
}
                """,
                        "<<CODE>>",
                12
        );

        String baseCode;
        String marker;
        int codeIndent;

        Template(String baseCode, String marker, int codeIndent) {
            this.baseCode = baseCode;
            this.marker = marker;
            this.codeIndent = codeIndent;
        }

        public String fill(String code) {
            code = Arrays.stream(code.split("\n"))
                .map(line -> " ".repeat(codeIndent) + line)
                .collect(Collectors.joining("\n"));
            code = code.replaceAll("\\$\\{[a-zA-Z0-9.:_-]+}", "<!VARIABLE!>");
            return baseCode.replaceAll(marker, code);
        }
    }

    public TestCase(String name, Template template, String code, String expectedResult) {
        this.name = name;
        this.template = template;
        this.code = addMetaDataToSourceCode(template.fill(code));
        this.expectedResult = expectedResult;
    }
}
