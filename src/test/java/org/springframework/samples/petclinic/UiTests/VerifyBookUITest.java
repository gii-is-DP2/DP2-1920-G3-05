package org.springframework.samples.petclinic.UiTests;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class VerifyBookUITest {
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
			as("admin1").whenITryToVerifyBook("3").thenISeeTheBookIsVerifiedInTheList();
		}
		
		@Test
		public void testVerifyUserNegativeCase() throws Exception {
			
			as("admin1").whenITryToVerifyVerifiedBook("1").thenISeeAMessageSayingBookIsAlreadyVerified();
		}
		
		private VerifyBookUITest as(final String username) {
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
			driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		    driver.findElement(By.xpath("//button[@type='submit']")).click();
			
			return this;
		}
		
		private VerifyBookUITest whenITryToVerifyBook(final String id) {
			this.driver.findElement(By.xpath("//a[contains(@href, '/books/"+id+"')]")).click();
			this.driver.findElement(By.xpath("//a[contains(@href, '/admin/books/"+id+"/verify')]")).click();
			return this;
		}
		
		private VerifyBookUITest whenITryToVerifyVerifiedBook(final String id) {
			this.driver.findElement(By.xpath("//a[contains(@href, '/books/"+id+"')]")).click();
			this.driver.findElement(By.linkText("Verify book")).click();
			return this;
		}
		
		private VerifyBookUITest thenISeeTheBookIsVerifiedInTheList() {
			Assert.assertEquals("YesVerify book", driver.findElement(By.xpath("//tr[9]/td")).getText());
			return this;
		}
		
		private VerifyBookUITest thenISeeAMessageSayingBookIsAlreadyVerified() {
			Assert.assertEquals("This book is already verified", closeAlertAndGetItsText());
			return this;
		}

	  @After
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
}
