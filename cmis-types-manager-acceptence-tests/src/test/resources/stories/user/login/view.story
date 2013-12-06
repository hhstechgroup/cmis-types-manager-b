Meta:

Narrative:
As a user
I want to perform view of existing types
So when I press "View" button on Main page,
View page opens with selected type attributes

Scenario: authentification scenario
When the user opens the 'login.xhtml' page
When the user fills 'loginForm-login' field with 'test'
When the user fills 'loginForm-password' field with 'test'
When the user fills 'loginForm-URL' field with 'http://localhost:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11'
When clicks on element with id/name/className 'loginForm-loginBut'
Then wait for element 'treeForm-tree' is visible
When clicks on element with id/name/className 'view'
Then wait for element 'viewForm' is visible
When clicks on element with id/name/className 'accordionTabAttributes'
Then wait for element 'viewForm' is visible
When clicks on element with id/name/className 'accordionTabAttributes'
Then wait for element 'viewForm' is visible
When clicks on element with id/name/className 'return'
Then wait for element 'treeForm-tree' is visible
When clicks on element with id/name/className 'logoutForm-logoutBtn'
Then wait for element 'loginForm' is visible



