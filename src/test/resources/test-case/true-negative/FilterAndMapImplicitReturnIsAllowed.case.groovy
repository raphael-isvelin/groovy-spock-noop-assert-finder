//#name=filter
//#template=INSERT_IN_CLASS
//<code>
def "filter and map: implicit return is allowed"() {
    given:
        String iso2 = Country.FR.countryISO2code()

    expect:
        response.stream().filter(country -> country.country == iso2) == "FR"
        response.stream().filter(country -> country.isSomething) == "FR"

        response.map(country -> country.hasPrice == false).reduce(true, (a, b) -> a && b)
        response.map(country -> country.hasPrice).reduce(true, (a, b) -> a && b)
}
//</code>
/*<expected>
!!!FILE_OPEN,FilterAndMapImplicitReturnIsAllowed.case.groovy,test-case/true-negative/FilterAndMapImplicitReturnIsAllowed.case.groovy
!!!VISIT_METHOD,-1931309493:FilterAndMapImplicitReturnIsAllowed.case.groovy:filter and map: implicit return is allowed
</expected>*/
