Feature: search meetings

   Users can filter meetings by name, place or book's title

   Scenario: Search meeting with one result (Positive)
        Given I am logged withh username "owner1" and password "0wn3r" 
        When I search a meeting by "Harry Potter"
        Then the system redirects me to the meeting details as there's only one result for "Harry Potter"

    Scenario: Search meeting with several results (Positive)
        Given I am logged withh username "owner1" and password "0wn3r"
        When I search a meeting by "ETSII"
        Then the system redirects me to a list with the meetings results
        
    Scenario: Search meetings with no results (Negative)
        Given I am logged withh username "owner1" and password "0wn3r"
        When I search a meeting by "No results"
        Then the system will indicate me that there are no meetings that coincide
