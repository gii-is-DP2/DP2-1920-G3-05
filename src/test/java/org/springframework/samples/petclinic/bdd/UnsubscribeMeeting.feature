Feature: Unsubscribe meeting
  	I can unsubscribe from a meeting pressing a button
  
  Scenario: Successful unsubscribe from the meeting Reunion club de lectura ETSII  (Positive)
    Given I am logged withh username "admin1" and password "4dm1n"
    When I try to unsubscribe from the meeting "Reunion club de lectura ETSII"
    Then I see a message saying i was unsubscribed
    
  Scenario: I cannot unsubscribe because i was not subscribed  (Negative)
    Given I am logged withh username "admin1" and password "4dm1n"
    When I try to unsubscribe with URL from a meeting which I am not subscribed
    Then I see a message saying i am not subscribed
     
  Scenario: I cannot unsubscribe because the meeting was already held (Negative)
    Given I am logged withh username "admin1" and password "4dm1n"
    When I try to unsubscribe from a meeting that has already held
    Then I see a message saying that the meeting was held
    