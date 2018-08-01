@Feature
Feature: Reset functionality on login page of Application

@Reset
Scenario: Verification of Reset button

	Given Open the Firefox and lauch the application
	When Enter the Username and Password
	Then Reset the credential	
	
@Reset1
Scenario: Enter login Credential and reset the value.

	Given 	Open browser and launch the application
	When 	Enter user: "tessdasst" and password: "password"
	Then 	Click credential
	Then 	Verify failure result contain text: "The username or password is incorrect"
