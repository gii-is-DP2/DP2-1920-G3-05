Feature: Add to read books
  	I can add a book to read books
  
  Scenario: Successful add book to read books  (Positive)
    Given I am logged withh username "admin1" and password "4dm1n"
    When I try to add the book "Las marcas de la muerte" to the list of read books
    Then I can see that the book was added
  
  Scenario: Successful add book from to read list to read books  (Positive)
    Given I am logged withh username "admin1" and password "4dm1n"
    When I try to add the book "Dime quien soy" from to read list to the list of read books
    Then I can see that the book was added and the wish list was updated
    
  Scenario: Cannot add book to read books because was already added (Negative)
    Given I am logged withh username "admin1" and password "4dm1n"
    When I try to add a book with URL that I have already add to read books
    Then I see a message that was already added

    