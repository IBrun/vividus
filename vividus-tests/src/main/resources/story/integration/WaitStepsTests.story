Description: Integration tests for WaitSteps class.

Meta:
    @epic vividus-plugin-web-app

Scenario: Step verification "When I wait until element located by '$locator' disappears"
Given I am on page with URL `${vividus-test-site-url}/elementState.html`
Given I initialize scenario variable `disappearing-locator` with value `By.id(element-to-hide)`
Then number of elements found by `${disappearing-locator}` is equal to `1`
When I click on element located by `id(button-hide)`
When I wait until element located by `${disappearing-locator}` disappears
Then number of elements found by `${disappearing-locator}` is equal to `0`


Scenario: Verify step: When I wait until scroll finished
Given I am on page with URL `${vividus-test-site-url}/delayedScroll.html`
When I click on element located by `id(anchor)`
When I wait until scroll is finished
When I execute sequence of actions:
|type          |argument                                        |
|CLICK         |By.id(toClick)                                  |
Then an alert is not present
Then an element with the id 'anchor' disappears


Scenario: Verify step: When I wait until scroll finished should not lock the tests when no scroll performing
When I refresh page
When I wait until scroll is finished
When I execute sequence of actions:
|type          |argument                                        |
|CLICK         |By.id(toClick)                                  |
Then an alert is not present
Then an element with the id 'anchor' disappears

Scenario: Step verification I wait '$duration' with '$pollingDuration' polling until element located by `$locator` becomes $state
Given I am on page with URL `${vividus-test-site-url}/elementState.html`
Then number of elements found by `<element>` is equal to `<before>`
When I click on element located by `<button>`
When I wait `PT10S` with `PT1S` polling until element located by `<element>` becomes <state>
Then number of elements found by `<element>` is equal to `<after>`
Examples:
|button            |element               |state      |before|after|
|By.id(button-hide)|By.id(element-to-hide)|NOT_VISIBLE|1     |0    |
|By.id(button-show)|By.id(element-to-show)|VISIBLE    |0     |1    |

Scenario: Verify deprecated step: 'When I wait until element located `$locator` appears'
Given I am on page with URL `${vividus-test-site-url}/elementState.html`
Then number of elements found by `id(element-to-show)` is equal to `0`
When I click on element located by `id(button-show)`
When I wait until element located `id(element-to-show)` appears
Then number of elements found by `id(element-to-show)` is equal to `1`

Scenario: Verify deprecated step: 'When I wait until element located `$locator` disappears'
Given I am on page with URL `${vividus-test-site-url}/elementState.html`
Then number of elements found by `id(element-to-hide)` is equal to `1`
When I click on element located by `id(button-hide)`
When I wait until element located `id(element-to-hide)` disappears
Then number of elements found by `id(element-to-hide)` is equal to `0`

Scenario: Verify step: 'Then element located `$locator` exists for `$duration` duration'
Then element located `id(button-hide)` exists for `PT1S` duration
Then number of elements found by `id(button-hide)` is equal to `1`

Scenario: Verify step 'Then element located `$locator` exists for `$duration` duration' waits for element appearance
Meta:
    @issueId 2750
When I click on element located by `id(button-show)`
Then element located `id(element-to-show)` exists for `PT1S` duration
Then number of elements found by `id(element-to-show)` is equal to `1`

Scenario: Verify step: 'When I set page load timeout to `$duration`'
Meta:
    @requirementId 2122
Given I am on page with URL `${vividus-test-site-url}/elementState.html`
When I set page load timeout to `PT20S`
When I open URL `${vividus-test-site-url}/delayedLoading?imageTimeout=10000` in new tab
When I set page load timeout to `PT10S`

Scenario: Verify step: 'When I wait until number of elements located by `$locator` is $comparisonRule $number'
Given I am on page with URL `${vividus-test-site-url}/elementState.html`
When I wait until number of elements located by `id(element-to-show):i` is = 1
When I click on element located by `id(button-show)`
When I wait until number of elements located by `id(element-to-show):i` is equal to 0
Then number of elements found by `id(element-to-show)` is equal to `1`

Scenario: Verify step: 'When I wait until element located by `$locator` stops moving'
Given I am on page with URL `${vividus-test-site-url}/movingElement.html`
When I change context to element located by `cssSelector(.external)`
When I wait until element located by `cssSelector(.internal)` stops moving
When I COMPARE_AGAINST baseline with name `moving-element-after-stopping`
When I reset context
