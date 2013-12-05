Meta:

Narrative:
As a user
I want to perform log in
So that I can log in and see tree table

Scenario: authentification scenario
When the user opens the 'login.xhtml' page
When the user fills 'loginForm-login' field with 'test'
When the user fills 'loginForm-password' field with 'test'
When the user fills 'loginForm-URL' field with 'http://winctrl-tdl6ti6:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11'
When clicks on element with id/name/className 'loginForm-loginBut'
Then wait for element 'treeForm-tree' is visible
When clicks on element with id/name/className 'create'
Then wait for element 'createForm' is visible
When the user fills 'inputTextView' field with 'test1'
When the user fills 'inputTextView1' field with 'test1'
When the user fills 'inputTextView2' field with 'test1'
When the user fills 'inputTextView3' field with 'test1'
When the user fills 'inputTextView4' field with 'test1'
When the user fills 'inputTextView5' field with 'test1'
When clicks on element with id/name/className 'createTypeBtm'
Then wait for element 'treeForm-tree' is visible
When the user clicks on element with id/name/className 'ui-tree-toggler'
When clicks on element with id/name/className 'logoutForm-logoutBtn'
Then wait for element 'loginForm' is visible