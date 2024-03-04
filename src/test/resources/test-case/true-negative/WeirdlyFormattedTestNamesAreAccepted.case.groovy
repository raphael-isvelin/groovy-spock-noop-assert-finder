//#name=weirdly-formatted test case names are accepted
//#template=INSERT_IN_CLASS
//<code>
def     "some stuff"() {
    println "///will not match - method understood as test case"
        1 == 1

    where:
        a << [1, 4]
}

def         'another one'() {
    println "///will not match - method understood as test case"
        1 == 1

    where:
        a << [1, 4]
}

def         'anothersinglword'() {
    println "///will not match - method understood as test case"
        1 == 1

    where:
        a << [1, 4]
}

void     "some stuff void"() {
    println "///will not match - method understood as test case"
        1 == 1

    where:
        a << [1, 4]
}

void         'another one void'() {
    println "///will not match - method understood as test case"
        1 == 1

    where:
        a << [1, 4]
}

void         'anothersinglwordvoid'() {
    println "///will not match - method understood as test case"
        1 == 1

    where:
        a << [1, 4]
}

def testThatAuthorizePCICardPaymentParametersAreSerializable() throws Exception {
    given:
        def a = 1
    when:
        def b = a + 1
    then:
        println "///this shouldn't match as we're in a then block - TRUE NEGATIVE"
        authoriseParams == readAuthoriseParams
}

// TODO add tests to make sure setup and cleanup are detected correctly
def   setup() {
    "todo" == "add test"
}
def   'setup'() {
    "todo" == "add test"
}
def   "setup"() {
    "todo" == "add test"
}
def   cleanup() {
    "todo" == "add test"
}
def   'cleanup'() {
    "todo" == "add test"
}
def   "cleanup"() {
    "todo" == "add test"
}
void   setup() {
    "todo" == "add test"
}
void   'setup'() {
    "todo" == "add test"
}
void   "setup"() {
    "todo" == "add test"
}
void   cleanup() {
    "todo" == "add test"
}
void   'cleanup'() {
    "todo" == "add test"
}
void   "cleanup"() {
    "todo" == "add test"
}
//</code>
/*<expected>
!!!FILE_OPEN,WeirdlyFormattedTestNamesAreAccepted.case.groovy,test-case/true-negative/WeirdlyFormattedTestNamesAreAccepted.case.groovy
!!!VISIT_METHOD,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:some stuff
!!!VISIT_METHOD,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:another one
!!!VISIT_METHOD,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:anothersinglword
!!!VISIT_METHOD,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:some stuff void
!!!VISIT_METHOD,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:another one void
!!!VISIT_METHOD,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:anothersinglwordvoid
!!!VISIT_METHOD,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:testThatAuthorizePCICardPaymentParametersAreSerializable
!!!VISIT_METHOD,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:setup
>>>ERROR,WeirdlyFormattedTestNamesAreAccepted.case.groovy,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:setup,66,OTHER_METHOD,BinaryExpression,(todo == add test)
>>>ERROR,WeirdlyFormattedTestNamesAreAccepted.case.groovy,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:setup,69,OTHER_METHOD,BinaryExpression,(todo == add test)
>>>ERROR,WeirdlyFormattedTestNamesAreAccepted.case.groovy,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:setup,72,OTHER_METHOD,BinaryExpression,(todo == add test)
!!!VISIT_METHOD,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:cleanup
>>>ERROR,WeirdlyFormattedTestNamesAreAccepted.case.groovy,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:cleanup,75,OTHER_METHOD,BinaryExpression,(todo == add test)
>>>ERROR,WeirdlyFormattedTestNamesAreAccepted.case.groovy,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:cleanup,78,OTHER_METHOD,BinaryExpression,(todo == add test)
>>>ERROR,WeirdlyFormattedTestNamesAreAccepted.case.groovy,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:cleanup,81,OTHER_METHOD,BinaryExpression,(todo == add test)
>>>ERROR,WeirdlyFormattedTestNamesAreAccepted.case.groovy,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:setup,84,OTHER_METHOD,BinaryExpression,(todo == add test)
>>>ERROR,WeirdlyFormattedTestNamesAreAccepted.case.groovy,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:setup,87,OTHER_METHOD,BinaryExpression,(todo == add test)
>>>ERROR,WeirdlyFormattedTestNamesAreAccepted.case.groovy,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:setup,90,OTHER_METHOD,BinaryExpression,(todo == add test)
>>>ERROR,WeirdlyFormattedTestNamesAreAccepted.case.groovy,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:cleanup,93,OTHER_METHOD,BinaryExpression,(todo == add test)
>>>ERROR,WeirdlyFormattedTestNamesAreAccepted.case.groovy,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:cleanup,96,OTHER_METHOD,BinaryExpression,(todo == add test)
>>>ERROR,WeirdlyFormattedTestNamesAreAccepted.case.groovy,-49477212:WeirdlyFormattedTestNamesAreAccepted.case.groovy:cleanup,99,OTHER_METHOD,BinaryExpression,(todo == add test)
</expected>*/
