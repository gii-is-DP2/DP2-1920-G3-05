Feature: Find books

   Users can filter book by title,author,genre or ISBN

   Scenario: Search book with one result (Positive)
        Given I am logged with username "owner1" and password "0wn3r" 
        When I search a book by "Rafel Nadal"
        Then the system redirects me to the book details as there's only one result for "Rafel Nadal"

    Scenario: Search book with several results (Positive)
        Given I am logged with username "owner1" and password "0wn3r"
        When I search a book by "J.K. Rowling"
        Then the system redirects me to a list with the results

    Scenario: Search books with no results (Negative)
        Given I am logged with username "owner1" and password "0wn3r"
        When I search a book by "9788466333720"
        Then the system will indicate me that there are no results
        