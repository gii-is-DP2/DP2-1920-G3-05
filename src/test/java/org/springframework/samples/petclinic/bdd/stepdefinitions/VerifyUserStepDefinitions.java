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
public class VerifyUserStepDefinitions extends AbstractStep {

    private boolean acceptNextAlert = true;

    @LocalServerPort
    private int port;
    @Given("I am logged with username {string} and password {string}")
    public void IdoLogginAsAdmin(String username, String paasword) throws Exception {
        loginAs(username, paasword, port);
    }

    @When("I try to verify an user")
    public void IverifyUser() throws Exception{
        listUsers();
    }


	@And("The user is not verified")
    public void userNotVerified() {
		verifyUser();
    }

    @Then("the user gets verified and it appears like that in the user list")
    public void assertUserIsVerified() throws Exception{
    	Assert.assertEquals("Yes Verify user",
				getDriver().findElement(By.xpath("//table[@id='readersTable']/tbody/tr[2]/td[4]")).getText());
        stopDriver();
    }
    
    @And("The user is verified")
    public void userVerified() {
    	
    }

	@Then("the system shows an alert saying that the user is already verified")
	 public void assertAlertUserIsVerified() throws Exception{
		acceptNextAlert = true;
		verifyVerifiedUser();
		Assert.assertEquals("This user is already verified", closeAlertAndGetItsText());
        stopDriver();
    }


    public void loginAs(String username, String password, int port) {
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
        getDriver().findElement(By.id("username")).sendKeys(username);
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
    
    private void listUsers() {
    	getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/span")).click();
		
	}
    private void verifyUser() {
    	getDriver().findElement(By.linkText("List users")).click();
    	getDriver().findElement(By.xpath("//a[contains(@href, '/admin/users/2/verify')]")).click();
		
	}
    
    private void verifyVerifiedUser() {
    	getDriver().findElement(By.linkText("List users")).click();
    	getDriver().findElement(By.xpath("(//a[contains(text(),'Verify user')])[3]")).click();		
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