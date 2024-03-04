package com.github.raphael.isvelin;

public enum Type {
    ROOT_THEN(          "        root:then", false),
    ROOT_GIVEN(         "       root:given", true),
    MOCK_CLOSURE(       "     MOCK_CLOSURE", true),
    CONDITION_OR_LOOP(  "CONDITION_OR_LOOP", true),
    EACH(               "             EACH", true),
    ASSERTING_METHOD(   " ASSERTING_METHOD", false),
    OTHER_METHOD(       "     OTHER_METHOD", true),
    CLASS(              "            CLASS", true);

    String value;
    boolean requiresAssert;

    Type(String value, boolean requiresAssert) {
        this.value = value;
        this.requiresAssert = requiresAssert;
    }
}
