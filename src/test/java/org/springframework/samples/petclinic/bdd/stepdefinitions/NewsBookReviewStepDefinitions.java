package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class NewsBookReviewStepDefinitions extends AbstractStep {
	
	 private boolean acceptNextAlert = true;

	 @LocalServerPort
	 private int port;
	  
	    
	    @When("I try to see news recommended")
	    public void tryToSeeNewsRecomended() {
	    	getDriver().findElement(By.linkText("All news")).click();
	    	getDriver().findElement(By.xpath("//a[contains(text(),'Recommended\n						news')]")).click();
	    }
	    
	    @Then("the system redirect me to a view with the news of the book i have reviewed")
	    public void assertCanSeeNewBookReview() throws Exception{
	    	Assert.assertEquals("Harry potter vuelve", getDriver().findElement(By.xpath("//div/div/div/h2")).getText());
	    	stopDriver();
	    }
	    
	    @When("I try to see recommended news")
	    public void tryToSeeNewsRecomendedWithoutReview() {
	    	 getDriver().findElement(By.xpath("//a[contains(text(),'Recommended\n						news')]")).click();
	    	
	    }
	    
	    @Then("the system shows an alert saying that I cannot see because I do not review any book")
	    public void assertAlertNewsRecommended() throws Exception{
	    	acceptNextAlert = true;
	    	Assert.assertEquals("In orden to see this news you must review at least one book", closeAlertAndGetItsText());
	    	stopDriver();
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
