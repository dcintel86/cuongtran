@feature
Feature: Get book by ISBN
 
 @GET_METHOD
  Scenario: User calls web service to get a book by its ISBN
    Given a book exists with an isbn of "9781451648546"
 	When a user retrieves the book by isbn
 	Then get response all header
 	
 	Then verify the header "Content-Encoding" is "gzip"
 	Then verify the header "X-Frame-Options" is "SAMEORIGIN"

	Then verify response contains "Steve Jobs"
	Then verify response "items[0].volumeInfo.industryIdentifiers[1].identifier" contains "1451648545"
    Then verify status code is "200"


@POST_METHOD
	Scenario: Modify Request Parameter
	Given Insert URI: "http://restapi.demoqa.com/customer/register" and location for json file: "data\JSON\JsonData"
	Then Verify the response with key: "FaultId" and its value: "User already exists"
