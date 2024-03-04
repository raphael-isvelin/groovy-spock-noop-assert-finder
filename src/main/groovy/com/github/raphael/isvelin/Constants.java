package com.github.raphael.isvelin;

import java.util.List;

public class Constants {

    public static final List<String> GROOVY_METHOD_WITH_NON_ASSERTING_CLOSURE_PARAMS = List.of("any", "average", "callClosureForLine", "callClosureForMapEntry", "callClosureForMapEntryAndCounter", "collect", "collectEntries", "collectMany", "count", "countBy", "downto", "dropWhile", "each", "eachByte", "eachWithIndex", "every", "find", "findAll", "findIndexOf", "findIndexValues", "findLastIndexOf", "findResult", "findResults", "groovy.transform.stc.ClosureParams;", "groupBy", "groupEntriesBy", "identity", "inject", "max", "metaClass", "min", "removeAll", "retainAll", "reverseEach", "sort", "split", "sum", "takeWhile", "tap", "times", "toSorted", "toUnique", "unique", "upto", "with", "withDefault", "withEagerDefault", "withLazyDefault");
    public static final List<String> GROOVY_METHOD_WITH_NON_ASSERTING_CLOSURE_PARAMS_RETURNING_BOOLEAN = List.of("allMatch", "every", "any", "count", "find", "findAll", "retainAll", "removeAll", "split", "takeWhile", "dropWhile", "findIndexOf", "findLastIndexOf", "findIndexValues");
    public static final List<String> ASSERTING_METHODS = List.of("verify", "verifyAll", "with");
    public static final List<String> METHODS_IMPLICIT_RETURN_ALLOWED = List.of("filter", "map", "reduce");
    public static final List<String> ASSERTJ_METHODS = List.of("assertThat", "assertThatThrownBy", "assertThatObject", "assertWith");
    public static final String AWAITILITY_METHOD_NAME = "await"; // just hardcoding some more edge cases :shrug:

    public static final String GIVEN_BLOCK_TAG = "<!GIVEN block!>";
    public static final String WHEN_BLOCK_TAG = "<!WHEN block!>";
    public static final String SETUP_BLOCK_TAG = "<!SETUP block!>";
    public static final String CLEANUP_BLOCK_TAG = "<!CLEANUP block!>";
    public static final String THEN_BLOCK_TAG = "<!THEN block!>";
    public static final String EXPECT_BLOCK_TAG = "<!EXPECT block!>";
    public static final String WHERE_BLOCK_TAG = "<!WHERE block!>";
    public static final String TEST_CASE_BLOCK_TAG = "<!TEST_CASE block!>";
}
