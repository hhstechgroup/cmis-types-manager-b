Meta:

Narrative:
As a user
I want to check that validation works for login page

Scenario: authentification scenario
When the user opens the default page
When the user fills 'loginForm-login' field with 'test'
When the user fills 'loginForm-password' field with 'test'
When the user fills 'loginForm-URL' field with 'chemistry-opencmis-server-inmemory/atom11' using baseUrl
When clicks on element with id/name/className 'loginBut'
Then wait for element 'treeForm' is visible

Scenario: create type with metadata scenario
When clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When clicks on element with id/name/className 'create'
Then wait for element 'createTypeForm' is visible
When the user fills 'inputTextView' field with 'rel2'
When the user clicks on element with id/name/className 'createTypeBtm'
Then wait for element 'messages' is visible
Then element id/name/className 'ui-messages-error-summary' has text 'Please, enter id'
When the user fills 'inputTextView' field with ''
When the user fills 'inputTextView2' field with 'rel2'
When the user clicks on element with id/name/className 'createTypeBtm'
Then wait for element 'messages' is visible
Then element id/name/className 'ui-messages-error-summary' has text 'Please, enter display name'
When the user fills 'inputTextView' field with 'rel2'
When the user clicks on first element with className 'ui-icon-triangle-1-e' with text 'Metadata'
When the user clicks on element with id/name/className 'newSourceSystemButton'
When the user fills 'newMetaDataId' field with 'met2'
When the user fills 'newMetaDataName' field with 'met2'
When the user fills 'newMetaDataLocalName' field with 'met2'
When the user fills 'newMetaDataQueryName' field with 'met2'
When clicks on element with id/name/className 'createMetadataButton'
Then wait for element 'createTypeForm-accordionPanel-metadataTabel_data' is visible
Then wait for visible element '//tbody[@id="createTypeForm-accordionPanel-metadataTabel_data"]/tr[@class="ui-widget-content ui-datatable-even"]'
When the user clicks on element with id/name/className 'createTypeBtm'
Then wait for element 'treeForm' is visible
When the user clicks on element with xpathOrCss '//span[@class="ui-tree-toggler ui-icon ui-icon-triangle-1-e"]'
Then element with '//span[@id='treeForm-tree-0_1-nodeText']' has text 'rel2'

Scenario: logout scenario
When clicks on element with id/name/className 'logoutForm-logoutBtn'
Then wait for element 'loginForm' is visible
