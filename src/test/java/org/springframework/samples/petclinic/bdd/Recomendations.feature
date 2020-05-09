Feature: Recomendations

   The users can see some recommended books

    Scenario: I can see some recommended books (Positive)
        Given I am logged withh username "reader1" and password "reader"
        When I try to see my recomendations
        Then I see a list of recommended books

    Scenario: The database do not have more books of the same genre (Negative)
        Given I am logged withh username "admin1" and password "4dm1n"
        When I try to see my recomendations but there is no more books of the same genre
        Then I see a message saying what is my favourite genre and that i have read all their books of this genre

    Scenario: The user do not have any book marked as read (Negative)
        Given I am logged withh username "reader2" and password "reader"
        When I try to see my recomendations without read books
        Then I see a message saying I need to read at least one book

        