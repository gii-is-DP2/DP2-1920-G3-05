package org.springframework.samples.petclinic.UiTests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VerifyUserUITest {
	@LocalServerPort
	private int port;
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {
		System.setProperty("webdriver.gecko.driver", System.getenv("webdriver.gecko.driver"));
		driver = new FirefoxDriver();
		baseUrl = "https://www.google.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testVerifyUserPositiveCase() throws Exception {
		as("admin1").whenITryToVerifyUser("2").thenISeeTheUserIsVerifiedInTheList();
	}
	
	@Test
	public void testVerifyUserNegativeCase() throws Exception {
		
		as("admin1").whenITryToVerifyVerifiedUser("3").thenISeeAMessageSayingUserIsAlreadyVerified();
	}

	@AfterEach
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
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
	
	private VerifyUserUITest as(final String username) {
		String password="4dm1n";
		this.driver.get("http://localhost:"+ this.port);
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/span[2]")).click();
		this.driver.findElement(By.linkText("Login")).click();
		this.driver.findElement(By.id("username")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys(username);
		this.driver.findElement(By.id("password")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys(password);
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/span")).click();
		
		return this;
	}
	
	private VerifyUserUITest whenITryToVerifyUser(final String id) {
		this.driver.findElement(By.linkText("List users")).click();
		this.driver.findElement(By.xpath("//a[contains(@href, '/admin/users/"+id+"/verify')]")).click();
		return this;
	}
	
	private VerifyUserUITest whenITryToVerifyVerifiedUser(final String id) {
		this.driver.findElement(By.linkText("List users")).click();
		this.driver.findElement(By.xpath("(//a[contains(text(),'Verify user')])["+id+"]")).click();
		return this;
	}
	
	private VerifyUserUITest thenISeeTheUserIsVerifiedInTheList() {
		Assert.assertEquals("Yes Verify user",
				driver.findElement(By.xpath("//table[@id='readersTable']/tbody/tr[2]/td[4]")).getText());
		return this;
	}
	
	private VerifyUserUITest thenISeeAMessageSayingUserIsAlreadyVerified() {
		Assert.assertEquals("This user is already verified", closeAlertAndGetItsText());
		return this;
	}
}
