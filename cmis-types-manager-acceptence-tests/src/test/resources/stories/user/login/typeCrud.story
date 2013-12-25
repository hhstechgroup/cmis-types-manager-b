Scenario: authentification scenario
When the user opens the default page
When the user fills 'loginForm-login' field with 'test'
When the user fills 'loginForm-password' field with 'test'
When the user fills 'loginForm-URL' field with 'chemistry-opencmis-server-inmemory-0.10.0/atom11' using baseUrl
When clicks on element with id/name/className 'loginBut'
Then wait for element 'treeForm' is visible

Scenario: create type with metadata scenario
When clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When clicks on element with id/name/className 'create'
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

When the user clicks on element with className 'ui-icon-triangle-1-e' with text 'Metadata'
When the user clicks on element with id/name/className 'newSourceSystemButton'
When the user fills 'createMetadataModalForm-newMetaDataId' field with 'met1'
When the user fills 'createMetadataModalForm-newMetaDataName' field with 'met1'
When the user fills 'createMetadataModalForm-newMetaDataLocalName' field with 'met1'
When the user fills 'createMetadataModalForm-newMetaDataQueryName' field with 'met1'
When the user fills 'createMetadataModalForm-newMetaDataLocalNamespace' field with 'met1'
When the user fills 'createMetadataModalForm-newMetaDataDescription' field with 'met1'
When clicks on element with id/name/className 'createMetadataModalForm-createMetadataButton'
When the user clicks on element with className 'ui-icon-triangle-1-e' with text 'Metadata'
When the user clicks on element with id/name/className 'newSourceSystemButton'
When the user fills 'createMetadataModalForm-newMetaDataId' field with 'met2'
When the user fills 'createMetadataModalForm-newMetaDataName' field with 'met2'
When the user fills 'createMetadataModalForm-newMetaDataLocalName' field with 'met2'
When the user fills 'createMetadataModalForm-newMetaDataQueryName' field with 'met2'
When the user fills 'createMetadataModalForm-newMetaDataLocalNamespace' field with 'met2'
When the user fills 'createMetadataModalForm-newMetaDataDescription' field with 'met2'
When clicks on element with id/name/className 'createMetadataModalForm-createMetadataButton'
Then wait for element 'createMetadataModalDialog' is not visible
When the user clicks on element with id/name/className 'createTypeForm-createTypeBtm'
Then wait for element 'treeForm' is visible
When the user clicks on element with xpathOrCss '//span[@class="ui-tree-toggler ui-icon ui-icon-triangle-1-e"]'
Then element with '//span[@id='treeForm-tree-0_1-nodeText']' has text 'rel1'

Scenario: view type with metadata scenario
When the user clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When the user clicks on element with id/name/className 'treeForm-tree-0_1-nodeText'
When the user clicks on element with id/name/className 'commandForm-view'
Then wait for element 'viewForm' is visible
When the user clicks on element with className 'ui-icon-triangle-1-e' with text 'Metadata'
When the user clicks on element with id/name/className 'viewForm-return'
Then wait for element 'treeForm' is visible

Scenario: create subtype scenario
When the user clicks on element with xpathOrCss '//span[@class="ui-tree-toggler ui-icon ui-icon-triangle-1-e"]'
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
Then wait for element 'treeForm' is visible
When the user clicks on element with xpathOrCss '//span[@class="ui-tree-toggler ui-icon ui-icon-triangle-1-e"]'
When the user clicks on element with xpathOrCss '//span[@class="ui-tree-toggler ui-icon ui-icon-triangle-1-e"]'
Then element with '//span[@id='treeForm-tree-0_1_0-nodeText']' has text 'rel11'

Scenario: create subtype scenario
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
Then wait for element 'treeForm' is visible
When the user clicks on element with xpathOrCss '//span[@class="ui-tree-toggler ui-icon ui-icon-triangle-1-e"]'
When the user clicks on element with xpathOrCss '//span[@class="ui-tree-toggler ui-icon ui-icon-triangle-1-e"]'
Then element with '//span[@id='treeForm-tree-0_1_1-nodeText']' has text 'rel12'

Scenario: delete type scenario
When the user clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When the user clicks on element with id/name/className 'treeForm-tree-0_1-nodeText'
When the user clicks on element with id/name/className 'commandForm-deleteButton'
Then wait for element 'deleteTypeDialog' is visible
When the user clicks on element with id/name/className 'deleteForm-Yes'

Scenario: logout scenario
When clicks on element with id/name/className 'logoutForm-logoutBtn'
Then wait for element 'loginForm' is visible