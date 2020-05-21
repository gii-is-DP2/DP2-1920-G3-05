package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.openqa.selenium.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class VerifyBookStepDefinitions extends AbstractStep {

    private boolean acceptNextAlert = true;

    @When("I try to verify a book")
    public void Iverifybook() throws Exception{
        listbooks();
    }


	@And("The book is not verified")
    public void bookNotVerified() {
		verifybook();
    }

    @Then("the book gets verified and it appears like that in the book list")
    public void assertbookIsVerified() throws Exception{
    	Assert.assertEquals("YesVerify book",
				getDriver().findElement(By.xpath("//tr[9]/td")).getText());
        stopDriver();
    }
    
    @And("The book is verified")
    public void bookVerified() {
    	
    }

	@Then("the system shows an alert saying that the book is already verified")
	 public void assertAlertbookIsVerified() throws Exception{
		acceptNextAlert = true;
		verifyVerifiedbook();
		Assert.assertEquals("This book is already verified", closeAlertAndGetItsText());
        stopDriver();
    }


    public void loginAs(String bookname, String password, int port) {
        getDriver().get("http://localhost:" + port);
        getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
        WebElement logIn = getDriver().findElement(By.linkText("Login"));
        if(logIn == null) {
          logOut();
          logIn = getDriver().findElement(By.linkText("Login"));
        }
        logIn.click();
        getDriver().findElement(By.id("username")).click();
        getDriver().findElement(By.id("username")).clear();
        getDriver().findElement(By.id("username")).sendKeys(bookname);
        getDriver().findElement(By.id("password")).click();
        getDriver().findElement(By.id("password")).clear();
        getDriver().findElement(By.id("password")).sendKeys(password);
        getDriver().findElement(By.xpath("//button[@type='submit']")).click();
    }

    public void logOut(){
        getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
        getDriver().findElement(By.linkText("Logout")).click();
        getDriver().findElement(By.xpath("//button[@type='submit']")).click();
      }
    
    private void listbooks() {
    	getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
    	getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		
	}
    private void verifybook() {
    	getDriver().findElement(By.xpath("//a[contains(@href, '/books/3')]")).click();
    	getDriver().findElement(By.xpath("//a[contains(@href, '/admin/books/3/verify')]")).click();
		
	}
    
    private void verifyVerifiedbook() {
    	getDriver().findElement(By.xpath("//a[contains(@href, '/books/2')]")).click();
    	getDriver().findElement(By.xpath("//a[contains(text(),'Verify book')]")).click();		
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