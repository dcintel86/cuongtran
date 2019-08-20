@Feature
Feature: Reset functionality on login page of Application

@Reset
Scenario: Verification of Reset button

	Given Open the Firefox and lauch the application
	When Enter the Username and Password
	Then Reset the credential	
	
@Reset1
Scenario: Login fail1

	Given 	Open browser and launch the application
	When 	Enter user: "tessdasst" and password: "password"
	Then 	Verify failure result contain text: "test"
	
@Reset1
Scenario: Login Pass2

	Given 	Open browser and launch the application
	When 	Enter user: "tessdasst" and password: "password"
	Then 	Verify failure result contain text: "The username or password is incorrect"

@Reset1
Scenario: Login fail3

	Given 	Open browser and launch the application
	When 	Enter user: "tessdasst" and password: "password"
	Then 	Verify failure result contain text: "test"
	
@Reset1
Scenario: Login Pass4

	Given 	Open browser and launch the application
	When 	Enter user: "tessdasst" and password: "password"
	Then 	Verify failure result contain text: "The username or password is incorrect"
	
