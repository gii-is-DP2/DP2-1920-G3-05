package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class FindBookStepDefinitions extends AbstractStep{

	    @LocalServerPort
	    private int port;

	    @When("I search a book by {string}")
	    public void searchBook(final String seacrhParam) throws Exception {
	        goSearchBook(seacrhParam);
	    } 

	    @Then("the system redirects me to the book details as there's only one result for {string}")
	    public void oneResult(String searchParam) throws Exception {
	        checkOneResult(searchParam);
	        stopDriver();
	    } 
	    
	    @Then("the system redirects me to a list with the results")
	    public void severalResult() throws Exception {
	        checkSeveralResults();
	        stopDriver();
	    } 

	    @Then("the system will indicate me that there are no results")
	    public void noResult() throws Exception {
	        checkNoResults();
	        stopDriver();
	    } 

	    private void goSearchBook(String searchParam) {
	        getDriver().findElement(By.id("findBook")).click();
	        getDriver().findElement(By.name("title")).click();
	        getDriver().findElement(By.name("title")).clear();
	        getDriver().findElement(By.name("title")).sendKeys(searchParam);
	        getDriver().findElement(By.id("findBookButton")).click();
	    }

	    private void checkOneResult(String searchParam) {
	        Assertions.assertThat("Book Information").isEqualTo(getDriver().findElement(By.id("bookDetail")).getText());
	        Assertions.assertThat(getDriver().findElement(By.id("authorBook")).getText()).contains(searchParam);
	    }

	    private void checkSeveralResults() {
	        Assertions.assertThat("Books").isEqualTo(getDriver().findElement(By.id("booksList")).getText());
	    }

	    private void checkNoResults() {
	        Assertions.assertThat("Has not been found").isEqualTo(getDriver().findElement(By.id("book.errors")).getText());
	    }
	}
