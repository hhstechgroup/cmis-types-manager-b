 Meta:

Narrative:
As a user
I want to perform login
So that I can login and see tree table

Scenario: authentification scenario
When the user opens the 'login.xhtml' page
When the user fills 'loginForm-login' field with 'test'
When the user fills 'loginForm-password' field with 'test'
When the user fills 'loginForm-URL' field with 'http://localhost:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11'
When clicks on element with id/name/className 'loginForm-loginBut'
Then wait for element 'changeRepositoryForm-changeRepositoryMenu' is visible

Narrative:
As a user
I want to choose a repository
So that I can active dropeDown menu with repositories, choose the one and see tree of types that repository

Scenario: choose repository scenario
When select 'Apache Chemistry OpenCMIS InMemory Repository' from 'changeRepositoryForm-changeRepositoryMenu' drop-down
Then wait for element 'treeForm-tree' is visible

Narrative:
As a user
I want to perform logout
So that I can logout and see login page

Scenario: logout scenario
When clicks on element with id/name/className 'logoutForm-logoutBtn'
Then wait for element 'loginForm' is visible




