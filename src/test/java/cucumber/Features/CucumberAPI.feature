Feature: Get book by ISBN
 
 @GET_METHOD
  Scenario: User calls web service to get a book by its ISBN
    Given a book exists with an isbn of "9781451648546"
 	When a user retrieves the book by isbn
 	Then get response all header
 	
 	
 	Then the header "Content-Encoding" is "gzip"
 	Then the header "X-Frame-Options" is "SAMEORIGIN"

	Then Check response contains "Steve Jobs"
	Then Check response "items[0].volumeInfo.authors[0]" contains "Walter Isaacson"
    Then the status code is "200"



