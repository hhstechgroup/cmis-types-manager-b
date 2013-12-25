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
When the user clicks on element with id/name/className 'selectCheckboxQuarylable'
When the user clicks on element with id/name/className 'selectCheckboxOrderable'
When the user clicks on element with id/name/className 'selectCheckboxRequeried'
When the user clicks on element with id/name/className 'selectCheckboxInheried'
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
Then wait for element 'createTypeForm-accordionPanel-metadataTabel_data' is visible
When the user clicks on element with xpathOrCss '//tbody[@id="createTypeForm-accordionPanel-metadataTabel_data"]/tr[@class="ui-widget-content ui-datatable-even"]'
When the user clicks on element with id/name/className 'selectedMetadataUpdateBtn'
When the user fills 'updateMetadataModalForm-newMetaDataIdUpdate' field with 'met2'
When the user fills 'updateMetadataModalForm-newMetaDataNameUpdate' field with 'met2'
When the user fills 'updateMetadataModalForm-newMetaDataLocalNameUpdate' field with 'met2'
When the user fills 'updateMetadataModalForm-newMetaDataQueryNameUpdate' field with 'met2'
When the user fills 'updateMetadataModalForm-newMetaDataLocalNamespaceUpdate' field with 'met2'
When the user fills 'updateMetadataModalForm-newMetaDataDescriptionUpdate' field with 'met2'
When clicks on element with id/name/className 'updateMetadataModalForm-updateMetadataButton'
Then wait for element 'updateMetadataModalDialog' is not visible
When the user clicks on element with xpathOrCss '//tbody[@id="createTypeForm-accordionPanel-metadataTabel_data"]/tr[@class="ui-widget-content ui-datatable-even"]'
When the user clicks on element with id/name/className 'selectedMetadataDeleteBtn'
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

Scenario: export type to xml file scenario
When the user clicks on element with xpathOrCss '//span[@class="ui-tree-toggler ui-icon ui-icon-triangle-1-e"]'
When the user clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When the user clicks on element with id/name/className 'treeForm-tree-0_1-nodeText'
When the user clicks on element with id/name/className 'ImportExportForm-export'
When the user clicks on element with xpathOrCss '//div[@id="fileExportForm-includeChildCheckbox"]/div[@class="ui-chkbox-box ui-widget ui-corner-all ui-state-default"]'
When the user clicks on element with xpathOrCss '//table[@id="fileExportForm-xmlOrJsonOptions"]//div[@class="ui-radiobutton-box ui-widget ui-corner-all ui-state-default ui-state-active"]'
When the user clicks on 'fileExportForm-submitExportModalButton' export file
Then verify that file is exported
Then verify that length of the exported file isn't zero

Scenario: delete type scenario
When the user clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When the user clicks on element with id/name/className 'treeForm-tree-0_1-nodeText'
When the user clicks on element with id/name/className 'commandForm-deleteButton'
Then wait for element 'deleteTypeDialog' is visible
When the user clicks on element with id/name/className 'deleteForm-Yes'
Then wait for element 'treeForm' is visible

Scenario: import xml file to type scenario
When the user clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When the user clicks on element with id/name/className 'ImportExportForm-import'
When the user clicks on element with id/name/className 'importForm-importFileUpload_input'
When the user uploads the fileName/filePath 'rel1.xml' to field with 'importForm'
Then wait for timeout
When the user clicks on element with id/name/className 'importForm-createTypeBtm'
Then wait for element 'treeForm' is visible
When the user opens all tree with className 'ui-icon-triangle-1-e'

Scenario: export type to json file scenario
When the user clicks on element with xpathOrCss '//span[@class="ui-tree-toggler ui-icon ui-icon-triangle-1-e"]'
When the user clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When the user clicks on element with id/name/className 'treeForm-tree-0_1-nodeText'
When the user clicks on element with id/name/className 'ImportExportForm-export'
When the user clicks on element with xpathOrCss '//div[@id="fileExportForm-includeChildCheckbox"]/div[@class="ui-chkbox-box ui-widget ui-corner-all ui-state-default"]'
When the user clicks on element with xpathOrCss '//table[@id="fileExportForm-xmlOrJsonOptions"]//div[@class="ui-radiobutton-box ui-widget ui-corner-all ui-state-default"]'
When the user clicks on 'fileExportForm-submitExportModalButton' export file
Then verify that file is exported
Then verify that length of the exported file isn't zero

Scenario: delete type scenario
When the user clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When the user clicks on element with id/name/className 'treeForm-tree-0_1-nodeText'
When the user clicks on element with id/name/className 'commandForm-deleteButton'
Then wait for element 'deleteTypeDialog' is visible
When the user clicks on element with id/name/className 'deleteForm-Yes'
Then wait for element 'treeForm' is visible

Scenario: import json file to type scenario
When the user clicks on element with id/name/className 'treeForm-tree-0-nodeText'
When the user clicks on element with id/name/className 'ImportExportForm-import'
When the user clicks on element with id/name/className 'importForm-importFileUpload_input'
When the user uploads the fileName/filePath 'rel1.json' to field with 'importForm'
Then wait for timeout
When the user clicks on element with id/name/className 'importForm-createTypeBtm'
Then wait for element 'treeForm' is visible
When the user opens all tree with className 'ui-icon-triangle-1-e'

Scenario: logout scenario
When clicks on element with id/name/className 'logoutForm-logoutBtn'
Then wait for element 'loginForm' is visible