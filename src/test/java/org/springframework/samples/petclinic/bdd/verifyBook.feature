Feature: verify book

   Only the admin can verify books 

    Scenario: Admin verifies selected book (Positive)
        Given I am logged with bookname "admin1" and password "4dm1n"
        When I try to verify a book 
        And The book is not verified
        Then the book gets verified and it appears like that in the book list

    Scenario: Admin tries to verify a verified book (Negative)
        Given I am logged with bookname "admin1" and password "4dm1n"
        When I try to verify a book 
        And The book is verified
        Then the system shows an alert saying that the book is already verified
        