
package org.springframework.samples.petclinic.UiTests;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnsubscribeUITest {

	@LocalServerPort
	private int				port;
	private WebDriver		driver;
	private String			baseUrl;
	private boolean			acceptNextAlert		= true;
	private StringBuffer	verificationErrors	= new StringBuffer();


	@BeforeEach
	public void setUp() throws Exception {
		System.setProperty("webdriver.gecko.driver", System.getenv("webdriver.gecko.driver"));
		this.driver = new FirefoxDriver();
		this.baseUrl = "https://www.google.com/";
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	@ParameterizedTest
	@CsvSource({
		"owner1, Primera reunion",
		"admin1, Reunion club de lectura ETSII",
	})
	public void testUnsubscribePositiveCase(final String username, final String meetingName) throws Exception {
		as(username).
		whenITryToUnsubscribeFromTheMeeting(meetingName).
		thenISeeAMessageSayingIWasUnsubscribed();

	}
	@Test
	public void testUnsubscribeNegativeCaseNotSuscribed() throws Exception {
		as("admin1").
		whenITryToUnsubscribeWithURLFromAMeetingWhichImNotSubscribed().
		thenISeeAMessageSayingImNotsuscribed();

	}
	@Test
	public void testUnsubscribeNegativeCaseDate() throws Exception {
		as("admin1").
		whenITryToUnsubscribeFromAMeetingThatHasAlreadyHeld().
		thenISeeAMessageSayingTheMeetingWasHeld();

	}

	@AfterEach
	public void tearDown() throws Exception {
		this.driver.quit();
		String verificationErrorString = this.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}

	}

	private boolean isElementPresent(final By by) {
		try {
			this.driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}


	private boolean isAlertPresent() {
		try {
			this.driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = this.driver.switchTo().alert();
			String alertText = alert.getText();
			if (this.acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			this.acceptNextAlert = true;
		}
	}
	private UnsubscribeUITest as(final String username) {
		String password;
		if (username.equals("admin1")) {
			password = "4dm1n";
		} else {
			password = "0wn3r";
		}
		this.driver.get("http://localhost:" + this.port + "/news");
		this.driver.findElement(By.xpath("//a[contains(@href, '/meetings')]")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys(username);
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys(password);
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}
	private UnsubscribeUITest whenITryToUnsubscribeFromTheMeeting(final String meetingName) {
		this.driver.findElement(By.xpath("//a[contains(text(),'" + meetingName + "')]")).click();
		this.driver.findElement(By.xpath("//a[contains(text(),'Unsubscribe')]")).click();
		return this;
	}
	private UnsubscribeUITest whenITryToUnsubscribeFromAMeetingThatHasAlreadyHeld() {
		this.driver.findElement(By.xpath("//a[contains(text(),'Libro forum')]")).click();
		this.driver.findElement(By.xpath("//a[contains(text(),'Unsubscribe')]")).click();
		return this;	
	}
	private UnsubscribeUITest thenISeeAMessageSayingTheMeetingWasHeld() {
		Assert.assertEquals("The meeting has already been held!", this.driver.findElement(By.cssSelector("div.container.xd-container > div")).getText());
		return this;
	}
	private UnsubscribeUITest thenISeeAMessageSayingIWasUnsubscribed() {
		Assert.assertEquals("You are successfully unsubscribed", this.driver.findElement(By.cssSelector("div.container.xd-container > div")).getText());
		return this;
	}
	private UnsubscribeUITest whenITryToUnsubscribeWithURLFromAMeetingWhichImNotSubscribed() {
		this.driver.get("http://localhost:" + this.port + "/meetings/4/unsuscribe");
		return this;	
	}
	private UnsubscribeUITest thenISeeAMessageSayingImNotsuscribed() {
		Assert.assertEquals("You are not suscribed!", this.driver.findElement(By.cssSelector("div.container.xd-container > div")).getText());
		return this;
	}
}
