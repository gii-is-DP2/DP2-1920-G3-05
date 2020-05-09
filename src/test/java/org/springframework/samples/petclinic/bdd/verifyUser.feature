Feature: verify user

   Only the admin can verify users 

    Scenario: Admin verifies selected user (Positive)
        Given I am logged with username "admin1" and password "4dm1n"
        When I try to verify an user 
        And The user is not verified
        Then the user gets verified and it appears like that in the user list

    Scenario: Admin tries to verify a verified user (Negative)
        Given I am logged with username "admin1" and password "4dm1n"
        When I try to verify an user 
        And The user is verified
        Then the system shows an alert saying that the user is already verified
        