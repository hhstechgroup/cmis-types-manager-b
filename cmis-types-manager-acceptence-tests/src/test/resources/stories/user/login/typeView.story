Meta:

Narrative:
As a user
I want to View types with attributes and metadata

Scenario: authentification scenario
When the user opens the default page
When the user fills 'loginForm-login' field with 'test'
When the user fills 'loginForm-password' field with 'test'
When the user fills 'loginForm-URL' field with 'chemistry-opencmis-server-inmemory/atom11' using baseUrl
When clicks on element with id/name/className 'loginBut'
Then wait for element 'treeForm' is visible

Scenario: import xml file to type scenario
When the user clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When the user clicks on element with id/name/className 'ImportExportForm-import'
When the user uploads the fileName/filePath 'cmis-types-manager-acceptence-tests/src/test/resources/files/rel1.xml' to field with 'importForm-importFileUpload_input'
Then element 'importForm-fileUploadName' has attribute value 'rel1.xml'
When the user clicks on element with id/name/className 'importForm-createTypeBtm'
Then wait for element 'treeForm' is visible

Scenario: view type with metadata scenario
When clicks on element by '//span[@class="ui-tree-toggler ui-icon ui-icon-triangle-1-e"]'
When the user clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When the user clicks on element with id/name/className 'treeForm-tree-0_1-nodeText'
When the user clicks on element with id/name/className 'commandForm-view'
Then wait for element 'viewForm' is visible
When the user clicks on first element with className 'ui-icon-triangle-1-e' with text 'Metadata'
When the user clicks on element with id/name/className 'viewForm-return'
Then wait for element 'treeForm' is visible

Scenario: delete type scenario
When clicks on element by '//span[@class="ui-tree-toggler ui-icon ui-icon-triangle-1-e"]'
When the user clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When the user clicks on element with id/name/className 'treeForm-tree-0_1-nodeText'
When the user clicks on element with id/name/className 'commandForm-deleteButton'
Then wait for element 'deleteTypeDialog' is visible
When the user clicks on element with id/name/className 'deleteForm-Yes'
Then wait for element 'treeForm' is visible

Scenario: logout scenario
When clicks on element with id/name/className 'logoutForm-logoutBtn'
Then wait for element 'loginForm' is visible