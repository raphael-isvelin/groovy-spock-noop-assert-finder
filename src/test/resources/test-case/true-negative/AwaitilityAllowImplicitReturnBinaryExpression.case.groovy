//#name=skip awaitility asserting methods
//#template=NONE
//<code>
import spock.lang.Specification
import java.util.concurrent.TimeUnit

import static java.util.concurrent.TimeUnit.SECONDS
import static org.awaitility.Awaitility.await;

class SomeTestCaseAwaitility extends Specification {
    def "awaitility assert methods - true negative"() {
        given:
            def a = [2, 5]
        expect:
            await()
                .pollDelay(2, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.SECONDS)
                .until(() -> {
                    3 == 5
                    2 == 1
                })
            println "-"
            await().until({
                2 == 4
                1 == 1
            })
            println "-"
            await()
                .atMost(5, SECONDS)
                .until(() -> getTaxesByTransactionId(321L, TransactionType.CASHBACK_PAYOUT).size() == 1)
    }
}
//</code>
/*<expected>
!!!FILE_OPEN,AwaitilityAllowImplicitReturnBinaryExpression.case.groovy,test-case/true-negative/AwaitilityAllowImplicitReturnBinaryExpression.case.groovy
!!!VISIT_METHOD,-575989599:AwaitilityAllowImplicitReturnBinaryExpression.case.groovy:awaitility assert methods - true negative
>>>ERROR,AwaitilityAllowImplicitReturnBinaryExpression.case.groovy,-575989599:AwaitilityAllowImplicitReturnBinaryExpression.case.groovy:awaitility assert methods - true negative,16,OTHER_METHOD,BinaryExpression,(3 == 5)
>>>MAYBE,AwaitilityAllowImplicitReturnBinaryExpression.case.groovy,-575989599:AwaitilityAllowImplicitReturnBinaryExpression.case.groovy:awaitility assert methods - true negative,17,OTHER_METHOD,BinaryExpression,(2 == 1)
>>>ERROR,AwaitilityAllowImplicitReturnBinaryExpression.case.groovy,-575989599:AwaitilityAllowImplicitReturnBinaryExpression.case.groovy:awaitility assert methods - true negative,21,OTHER_METHOD,BinaryExpression,(2 == 4)
>>>MAYBE,AwaitilityAllowImplicitReturnBinaryExpression.case.groovy,-575989599:AwaitilityAllowImplicitReturnBinaryExpression.case.groovy:awaitility assert methods - true negative,22,OTHER_METHOD,BinaryExpression,(1 == 1)
>>>MAYBE,AwaitilityAllowImplicitReturnBinaryExpression.case.groovy,-575989599:AwaitilityAllowImplicitReturnBinaryExpression.case.groovy:awaitility assert methods - true negative,27,OTHER_METHOD,BinaryExpression,(this.getTaxesByTransactionId(321<!COMMA!> TransactionType.CASHBACK_PAYOUT).size() == 1)
</expected>*/
