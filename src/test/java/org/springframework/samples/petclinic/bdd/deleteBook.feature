Feature: delete book

   Only the admin can delete books and he must confirm the alert 

    Scenario: Admin does not confirm delete (Negative)
        Given I am logged withh username "admin1" and password "4dm1n"
        When I try to delete a book 
        And I do not confirm it
        Then the book is not deleted

    Scenario: Admin deletes book successfully (Positive)
        Given I am logged withh username "admin1" and password "4dm1n"
        When I try to delete a book 
        And I confirm it
        Then the book is deleted and it does no appear in the books' list

    Scenario: No admin user cannot delete book (Negative)
        Given I am logged withh username "owner1" and password "0wn3r"
        When I try to delete a book through URL
        Then the system does not allow me

        