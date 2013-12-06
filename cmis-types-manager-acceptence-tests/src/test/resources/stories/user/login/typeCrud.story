Meta:

Narrative:
As a user
I want to perform login
So that I can login and see tree table

Scenario: authentification scenario

When the user opens the default page
When the user fills 'loginForm-login' field with 'test'
When the user fills 'loginForm-password' field with 'test'
When the user fills 'loginForm-URL' field with 'http://localhost:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11'
When clicks on element with id/name/className 'loginForm-loginBut'
Then wait for element 'treeForm' is visible

Narrative:
As a user
I want to create new type
So that I can create and see new type

Scenario: create type scenario

When the user clicks on element with id/name/className 'treeForm-tree-0-nodeValue'
When clicks on element with id/name/className 'create'
Then wait for element 'createForm' is visible
When the user fills 'inputTextView' field with 'test1'
When the user fills 'inputTextView1' field with 'test1'
When the user fills 'inputTextView2' field with 'test1'
When the user fills 'inputTextView3' field with 'test1'
When the user fills 'inputTextView4' field with 'test1'
When the user fills 'inputTextView5' field with 'test1'
When the user clicks on element with id/name/className 'selectCheckboxCreatabel'
When the user clicks on element with id/name/className 'selectCheckboxFileable'
When the user clicks on element with id/name/className 'selectCheckboxQueryable'
When clicks on element with id/name/className 'createTypeBtm'
Then wait for element 'messages' is visible
When the user clicks on element with id/name/className 'ui-icon-close'
Then wait for element 'messages' is not visible

Narrative:
As a user
I want to view new type
So that I can select type, view it and return back to tree view

Scenario: view type scenario

When the user clicks on element with id/name/className 'treeForm-tree-0-nodeValue'
When the user clicks on element with id/name/className 'ui-tree-toggler'
When the user clicks on element with id/name/className 'treeForm-tree-0_1-nodeValue'
When the user clicks on element with id/name/className 'j_idt22-view'
Then wait for element 'viewForm' is visible
When the user clicks on element with id/name/className 'viewForm-return'
Then wait for element 'treeForm' is visible

Narrative:
As a user
I want to view new type
So that I can delete type and see message info

Scenario: delete type scenario

When the user clicks on element with id/name/className 'treeForm-tree-0-nodeValue'
When the user clicks on element with id/name/className 'ui-tree-toggler'
When the user clicks on element with id/name/className 'treeForm-tree-0_1-nodeValue'
When the user clicks on element with id/name/className 'j_idt22-deleteButton'
Then wait for element 'deleteForm' is visible
When the user clicks on element with id/name/className 'deleteForm-accept'
Then wait for element 'messages' is visible
When the user clicks on element with id/name/className 'ui-icon-close'
Then wait for element 'messages' is not visible

Narrative:
As a user
I want to perform logout
So that I can logout and see login page

Scenario: logout scenario
When clicks on element with id/name/className 'logoutForm-logoutBtn'
Then wait for element 'loginForm' is visible




