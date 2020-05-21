Feature: add book

    Any user can add a book, but only if the user is admin or is verified the book will appear as verified

    Scenario: no admin or verified user adds book

        Given I am logged with username "owner1" and password "0wn3r"
        When I try to add a book with name "The name of the wind", genre "Fantasy", writed by "Patrick Rothfuss", published on "2007/03/27", "613" pages, editorial "Plaza y Janes", ISBN "9780345805362", synopsis "sinopsis" and image "https://www.example.com"
        Then the book "The name of the wind" will be added and it will not be verified

    Scenario: admin or verified user adds book

        Given I am logged with username "admin1" and password "4dm1n"
        When I try to add a book with name "Wise Men Fear", genre "Fantasy", writed by "Patrick Rothfuss", published on "2007/03/27", "613" pages, editorial "Plaza y Janes", ISBN "9785699195527", synopsis "sinopsis" and image "https://www.example.com"
        Then the book "Wise Men Fear" will be added and it will be verified

    Scenario: user adds book with wrong ISBN

        Given I am logged with username "admin1" and password "4dm1n"
        When I try to add a book with name "The name of the wind", genre "Fantasy", writed by "Patrick Rothfuss", published on "2007/03/27", "613" pages, editorial "Plaza y Janes", ISBN "1234", synopsis "sinopsis" and image "https://www.example.com"
        Then the system will indicate that it is not a valid ISBN

    Scenario: admin or verified user adds with ISBN in use

        Given I am logged with username "admin1" and password "4dm1n"
        When I try to add a book with name "The name of the wind", genre "Fantasy", writed by "Patrick Rothfuss", published on "2007/03/27", "613" pages, editorial "Plaza y Janes", ISBN "9788466345347", synopsis "sinopsis" and image "https://www.example.com"
        Then the system will indicate that the ISBN is already in use


        