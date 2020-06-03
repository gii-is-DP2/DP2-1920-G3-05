Feature: News book review

	Users can see the news of the books they have reviewed

	Scenario: See news recommended (Positive)
		Given I am logged with username "owner1" and password "0wn3r" 
		When I try to see news recommended
		Then the system redirect me to a view with the news of the book i have reviewed

	Scenario: User without book review, try to see news recommended (Negative)
		Given I am logged with username "reader2" and password "reader" 
		When I try to see recommended news
		Then the system shows an alert saying that I cannot see because I do not review any book
		