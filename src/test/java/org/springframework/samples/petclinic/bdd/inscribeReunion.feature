Feature: Subscribe to a meeting

	Users can subscribe to a meeting by pressing a button
	
	Scenario: Successful subscribe to a meeting (Positive)
		Given I am logged with username "reader1" and password "reader"
		When I try to subscribe to the meeting "Reunion sin asistentes"
		Then the system redirects me to a list with the meetings
	
	Scenario: Unsuccessful subscribe to a meeting because not read book (Negative)
		Given I am logged with username "vet1" and password "v3t"
		When I try to subscribe to the meeting "Primera reunion"
		Then the system shows an alert saying that I cannot subscribe to this meeting because not read book
		
	Scenario: Unsuccessful subscribe to a meeting because the meeting has already finished (Negative)
		Given I am logged with username "reader1" and password "reader"
		When I try to subscribe to the meeting "Reunion prueba aforo"
		Then the system shows an alert saying that I cannot subscribe to this meeting because already finish
			
	Scenario: Unsuccessful subscribe to a meeting because the meeting coincides with another (Negative)
		Given I am logged with username "admin1" and password "4dm1n"
		When I try to subscribe to the meeting "Primera reunion"
		Then the system shows an alert saying that I cannot subscribe to this meeting
	
	Scenario: Unsuccessful subscribe to a meeting because the capacity is already full (Negative)
		Given I am logged with username "reader1" and password "reader"
		When I try to subscribe to the meeting "Reunion prueba aforo"
		Then the system shows an alert saying that I cannot subscribe to this meeting because the capacity is already full	