Meta:

Narrative:
As a user
I want to check that validation works for login page

Scenario: Validation for Login page
When the user opens the default page
When the user fills 'loginForm-login' field with 'test'
When the user fills 'loginForm-password' field with 'test'
When the user fills 'loginForm-URL' field with ''
When clicks on element with id/name/className 'loginBut'
Then wait for element 'messages' is visible
Then element id/name/className 'ui-messages-error-summary' has text 'URL: Validation Error: Value is required.'
When the user fills 'loginForm-URL' field with 'http'
When clicks on element with id/name/className 'loginBut'
Then wait for element 'messages' is visible
Then element id/name/className 'ui-messages-error-summary' has text 'Url validation failed.'
When the user fills 'loginForm-URL' field with '' using baseUrl
When clicks on element with id/name/className 'loginBut'
Then wait for element 'messages' is visible
Then element id/name/className 'ui-messages-error-summary' has text 'The repository on this URL doesn't exist!'
When the user fills 'loginForm-URL' field with 'chemistry-opencmis-server-inmemory/atom11' using baseUrl
When clicks on element with id/name/className 'loginBut'
Then wait for element 'treeForm' is visible

Scenario: logout scenario
When clicks on element with id/name/className 'logoutForm-logoutBtn'
Then wait for element 'loginForm' is visible

