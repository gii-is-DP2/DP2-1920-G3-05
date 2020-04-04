
package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnsubscribeMeetingStepDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;


	@Given("I am logged with username {string}")
	public void login(final String username) throws Exception {
		this.as(username);
	}

	@When("I try to unsubscribe from the meeting {string}")
	public void unsubscribe(final String meetingName) throws Exception {
		this.whenITryToUnsubscribeFromTheMeeting(meetingName);
	}
	@Then("I see a message saying i was unsubscribed")
	public void checkWell() throws Exception {
		this.thenISeeAMessageSayingIWasUnsubscribed();
		this.stopDriver();
	}
	@When("I try to unsubscribe with URL from a meeting which I am not subscribed")
	public void tryUnsubscribe() throws Exception {
		this.whenITryToUnsubscribeWithURLFromAMeetingWhichImNotSubscribed();
	}
	@Then("I see a message saying i am not subscribed")
	public void checkBad() throws Exception {
		this.thenISeeAMessageSayingImNotsuscribed();
		this.stopDriver();
	}
	@When("I try to unsubscribe from a meeting that has already held")
	public void tryUnsubscribeThatWasHeld() throws Exception {
		this.whenITryToUnsubscribeFromAMeetingThatHasAlreadyHeld();
	}
	@Then("I see a message saying that the meeting was held")
	public void checkICannot() throws Exception {
		this.thenISeeAMessageSayingTheMeetingWasHeld();
		this.stopDriver();
	}
	private void as(final String username) {
		String password;
		if (username.equals("admin1")) {
			password = "4dm1n";
		} else {
			password = "0wn3r";
		}
		this.getDriver().get("http://localhost:" + this.port + "/news");
		this.getDriver().findElement(By.xpath("//a[contains(@href, '/meetings')]")).click();
		this.getDriver().findElement(By.id("username")).clear();
		this.getDriver().findElement(By.id("username")).sendKeys(username);
		this.getDriver().findElement(By.id("password")).clear();
		this.getDriver().findElement(By.id("password")).sendKeys(password);
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();

	}
	private void whenITryToUnsubscribeFromTheMeeting(final String meetingName) {
		this.getDriver().findElement(By.xpath("//a[contains(text(),'" + meetingName + "')]")).click();
		this.getDriver().findElement(By.xpath("//a[contains(text(),'Unsubscribe')]")).click();
	}
	private void thenISeeAMessageSayingIWasUnsubscribed() {
		Assert.assertEquals("You are successfully unsubscribed", this.getDriver().findElement(By.cssSelector("div.container.xd-container > div")).getText());
	}
	private void whenITryToUnsubscribeWithURLFromAMeetingWhichImNotSubscribed() {
		this.getDriver().get("http://localhost:" + this.port + "/meetings/4/unsuscribe");

	}
	private void thenISeeAMessageSayingImNotsuscribed() {
		Assert.assertEquals("You are not suscribed!", this.getDriver().findElement(By.cssSelector("div.container.xd-container > div")).getText());

	}
	private void whenITryToUnsubscribeFromAMeetingThatHasAlreadyHeld() {
		this.getDriver().findElement(By.xpath("//a[contains(text(),'Libro forum')]")).click();
		this.getDriver().findElement(By.xpath("//a[contains(text(),'Unsubscribe')]")).click();

	}
	private void thenISeeAMessageSayingTheMeetingWasHeld() {
		Assert.assertEquals("The meeting has already been held!", this.getDriver().findElement(By.cssSelector("div.container.xd-container > div")).getText());

	}
}
