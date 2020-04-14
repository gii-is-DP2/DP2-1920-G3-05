package org.springframework.samples.petclinic.bdd.stepdefinitions;


import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class InscribeReunionStepDefinitions extends AbstractStep {
	
	 private boolean acceptNextAlert = true;
	
	@LocalServerPort
	private int port;
	
	@When("I try to subscribe to the meeting {string}")
	public void subscribe(final String meetingName) throws Exception {
		goToMeetings();
		whenITryToSubscribeFromTheMeeting(meetingName);
	}
	
	@Then("the system redirects me to a list with the meetings")
	public void assertInscribe() throws Exception {
		 Assert.assertEquals("Meetings", getDriver().findElement(By.id("page title")).getText());
		 stopDriver();
    }
	
	@Then("the system shows an alert saying that I cannot subscribe to this meeting because not read book")
	@Then("the system shows an alert saying that I cannot subscribe to this meeting because already finish")
	@Then("the system shows an alert saying that I cannot subscribe to this meeting")
	@Then("the system shows an alert saying that I cannot subscribe to this meeting because the capacity is already full")
	public void assertAlertNoInscribe() throws Exception{
		acceptNextAlert = true;
		Assert.assertEquals("In order to inscribe a meeting you must have read the book and you can only attend one meeeting at the same time, it cant be in the past and there must be capacity", closeAlertAndGetItsText());
		stopDriver();
	}
	
	private void whenITryToSubscribeFromTheMeeting(final String meetingName) {
		this.getDriver().findElement(By.xpath("//a[contains(text(),'" + meetingName + "')]")).click();
		getDriver().findElement(By.linkText("Inscribe")).click();
	}
	
	private void goToMeetings() {
   	 	getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[8]/a/span[2]")).click();
    	getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	
	 private String closeAlertAndGetItsText() {
	        try {
	          Alert alert = getDriver().switchTo().alert();
	          String alertText = alert.getText();
	          if (acceptNextAlert) {
	            alert.accept();
	          } else {
	            alert.dismiss();
	          }
	          return alertText;
	        } finally {
	          acceptNextAlert = true;
	        }
	      }

}
