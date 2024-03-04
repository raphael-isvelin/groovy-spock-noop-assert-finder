//#name=capture params in mock are asserting
//#template=INSERT_IN_CLASS
//<code>
class ThingProvider { String name() {} }
class Thing {}
class Request {}

def "capture params in mock are asserting"() {
    given:
        def a = 1
    expect:
        println "///returning param"
        1 * myService.calculate({
            it.amount == 121454
            it.param == baseRequest.param
            it.thing.find({ t -> t == "ID" }) != null
        }) >> dummyResult1
        println "///returning method call"
        1 * myService.calculate({
            it.amount == 121454
            it.param == baseRequest.param
            it.thing.find({ t -> t == "ID" }) != null
        }) >> dummyResult1.something()
        println "///returning closure"
        1 * myService.calculate({
            it.amount == 121454
            it.param == baseRequest.param
            it.thing.find({ t -> t == "ID" }) != null
        }) >> { dummyResult1 }
        println "///another"
        1 * myService.calculate(
                VALUE_A,
                VALUE_B,
                request.getCode(),
                { Thing c -> c == card } as Thing,
                { ThingProvider w -> w.name() == "X" } as ThingProvider
        )
        println "///property invocation count"
        expectedInvocations * myService.calculate({ Request req -> {
            req.id == expectedId1
        }})
        println "-"
        expectedInvocations * myService.calculate({ Request req -> {
            req.id == expectedId2
        }}) >> something
        println "-"
        expectedInvocations * myService.calculate({ Request req -> {
            req.id == expectedId3
        }}) >> { something() }
}
//</code>
/*<expected>
!!!FILE_OPEN,MockCaptureParamIsAsserting.case.groovy,test-case/true-negative/MockCaptureParamIsAsserting.case.groovy
!!!VISIT_METHOD,-133920384:MockCaptureParamIsAsserting.case.groovy:capture params in mock are asserting
!!!VISIT_METHOD,-133920384:MockCaptureParamIsAsserting.case.groovy:name
</expected>*/
