Meta:

Narrative:
As a user
I want to perform log in
So that I can log in and see tree table

Scenario: authentification scenario
When the user opens the default page
When the user fills 'loginForm:login' field with 'test'
When the user fills 'loginForm:password' field with 'test'
When the user fills 'loginForm:URL' field with 'http://lab14:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11'
When clicks on element with id/name/className 'loginForm:j_idt23'
Then wait for element 'types:treetable' is visible
When clicks on element with id/name/className 'logoutForm:j_idt25'
Then wait for element 'loginForm' is visible

