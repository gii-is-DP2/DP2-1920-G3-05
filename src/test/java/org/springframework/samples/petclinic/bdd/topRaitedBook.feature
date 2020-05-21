Feature: Top rated book

   Users can know which books are best valued by the rest of the users of the page

   Scenario: Top rated book (Positive)
        Given I am logged with username "owner1" and password "0wn3r" 
        When I request to see the best rated books
        Then the system shows me a top based on the books with the highest average of their reviews

    Scenario: Top rated books without sufficient data (Negative)
    		Given I am logged with username "admin1" and password "4dm1n" 
    		And there is no review
        And I am logged with username "owner1" and password "0wn3r" 
        When I request to see the best rated books
        Then the system indicates that it cannot be displayed because there is no data required in the application yet
      