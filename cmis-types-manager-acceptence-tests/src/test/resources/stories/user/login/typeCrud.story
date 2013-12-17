Scenario: authentification scenario
When the user opens the default page
When the user fills 'loginForm-login' field with 'test'
When the user fills 'loginForm-password' field with 'test'
When the user fills 'loginForm-URL' field with 'chemistry-opencmis-server-inmemory/atom11' using baseUrl
When clicks on element with id/name/className 'loginForm-loginBut'
Then wait for element 'treeForm' is visible

Scenario: create type scenario
When the user clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When clicks on element with id/name/className 'commandForm-create'
Then wait for element 'createTypeForm' is visible
When the user fills 'inputTextView' field with 'rel1'
When the user fills 'inputTextView1' field with 'rel1'
When the user fills 'inputTextView2' field with 'rel1'
When the user fills 'inputTextView3' field with 'rel1'
When the user fills 'inputTextView4' field with 'rel1'
When the user fills 'inputTextView5' field with 'rel1'
When the user clicks on element with id/name/className 'selectCheckboxCreatabel'
When the user clicks on element with id/name/className 'selectCheckboxFileable'
When the user clicks on element with id/name/className 'selectCheckboxQueryable'
When the user clicks on element with id/name/className 'selectCheckboxIst'
When the user clicks on element with id/name/className 'selectCheckboxFti'
When the user clicks on element with id/name/className 'selectCheckboxACL'
When the user clicks on element with id/name/className 'selectCheckboxPC'
When clicks on element with id/name/className 'createTypeBtm'
When the user clicks on element with id/name/className 'ui-icon-close'
Then wait for element 'treeForm' is visible
When the user opens all tree with className 'ui-tree-toggler'
Then element with '//span[@id='treeForm-tree-0_1-nodeText']' has text 'rel1'

Scenario: view type scenario
When the user clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When the user clicks on element with id/name/className 'treeForm-tree-0_1-nodeText'
When the user clicks on element with id/name/className 'commandForm-view'
Then wait for element 'viewForm' is visible
When the user clicks on element with id/name/className 'viewForm-return'
Then wait for element 'treeForm' is visible

Scenario: create subtype scenario
When the user opens all tree with className 'ui-tree-toggler'
When the user clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When the user clicks on element with id/name/className 'treeForm-tree-0_1-nodeText'
When clicks on element with id/name/className 'commandForm-create'
Then wait for element 'createTypeForm' is visible
When the user fills 'inputTextView' field with 'rel11'
When the user fills 'inputTextView1' field with 'rel11'
When the user fills 'inputTextView2' field with 'rel11'
When the user fills 'inputTextView3' field with 'rel11'
When the user fills 'inputTextView4' field with 'rel11'
When the user fills 'inputTextView5' field with 'rel11'
When the user clicks on element with id/name/className 'selectCheckboxCreatabel'
When the user clicks on element with id/name/className 'selectCheckboxFileable'
When the user clicks on element with id/name/className 'selectCheckboxQueryable'
When the user clicks on element with id/name/className 'selectCheckboxIst'
When the user clicks on element with id/name/className 'selectCheckboxFti'
When the user clicks on element with id/name/className 'selectCheckboxACL'
When the user clicks on element with id/name/className 'selectCheckboxPC'
When clicks on element with id/name/className 'createTypeBtm'
When the user clicks on element with id/name/className 'ui-icon-close'
Then wait for element 'treeForm' is visible

Scenario: create subtype scenario
When the user opens all tree with className 'ui-tree-toggler'
When the user clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When the user clicks on element with id/name/className 'treeForm-tree-0_1-nodeText'
When clicks on element with id/name/className 'commandForm-create'
Then wait for element 'createTypeForm' is visible
When the user fills 'inputTextView' field with 'rel12'
When the user fills 'inputTextView1' field with 'rel12'
When the user fills 'inputTextView2' field with 'rel12'
When the user fills 'inputTextView3' field with 'rel12'
When the user fills 'inputTextView4' field with 'rel12'
When the user fills 'inputTextView5' field with 'rel12'
When the user clicks on element with id/name/className 'selectCheckboxCreatabel'
When the user clicks on element with id/name/className 'selectCheckboxFileable'
When the user clicks on element with id/name/className 'selectCheckboxQueryable'
When the user clicks on element with id/name/className 'selectCheckboxIst'
When the user clicks on element with id/name/className 'selectCheckboxFti'
When the user clicks on element with id/name/className 'selectCheckboxACL'
When the user clicks on element with id/name/className 'selectCheckboxPC'
When clicks on element with id/name/className 'createTypeBtm'
When the user clicks on element with id/name/className 'ui-icon-close'
Then wait for element 'treeForm' is visible

Scenario: delete type scenario
When the user opens all tree with className 'ui-tree-toggler'
When the user clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When the user clicks on element with id/name/className 'treeForm-tree-0_1-nodeText'
When the user clicks on element with id/name/className 'commandForm-deleteButton'
Then wait for element 'deleteForm' is visible
When the user clicks on element with id/name/className 'deleteForm-Yes'
When the user clicks on element with id/name/className 'ui-icon-close'

Scenario: logout scenario
When clicks on element with id/name/className 'logoutForm-logoutBtn'
Then wait for element 'loginForm' is visible