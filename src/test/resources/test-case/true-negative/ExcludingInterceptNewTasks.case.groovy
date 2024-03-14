//#name=hardcoding exclude interceptNewTasks
//#template=INSERT_IN_CLASS
//<code>
def "hardcoding exclude interceptNewTasks"() {
    given:
        testTasksService.interceptNewTasks { it.type == getTaskType() }

    expect:
        1 == 1
}
//</code>
/*<expected>
!!!FILE_OPEN,ExcludingInterceptNewTasks.case.groovy,test-case/true-negative/ExcludingInterceptNewTasks.case.groovy
!!!VISIT_METHOD,-192058250:ExcludingInterceptNewTasks.case.groovy:hardcoding exclude interceptNewTasks
>>>MAYBE,ExcludingInterceptNewTasks.case.groovy,-192058250:ExcludingInterceptNewTasks.case.groovy:hardcoding exclude interceptNewTasks,8,OTHER_METHOD,BinaryExpression,(it.type == this.getTaskType())
</expected>*/
