package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class SearchMeetingsStepDefinitions extends AbstractStep {

    @LocalServerPort
    private int port;

    @When("I search a meeting by {string}")
    public void searchMeeting(final String seacrhParam) throws Exception {
        goSearchMeeting(seacrhParam);
    } 

    @Then("the system redirects me to the meeting details as there's only one result for {string}")
    public void oneResult(String searchParam) throws Exception {
        checkOneResult(searchParam);
        stopDriver();
    } 
    
    @Then("the system redirects me to a list with the meetings results")
    public void severalResult() throws Exception {
        checkSeveralResults();
        stopDriver();
    } 

    @Then("the system will indicate me that there are no meetings that coincide")
    public void noResult() throws Exception {
        checkNoResults();
        stopDriver();
    } 

    private void goSearchMeeting(String searchParam) {
        getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[8]/a/span[2]")).click();
        getDriver().findElement(By.name("name")).click();
        getDriver().findElement(By.name("name")).clear();
        getDriver().findElement(By.name("name")).sendKeys(searchParam);
        getDriver().findElement(By.xpath("//button[@type='submit']")).click();
    }

    private void checkOneResult(String searchParam) {
        Assertions.assertThat("Meeting Information").isEqualTo(getDriver().findElement(By.id("info")).getText());
        Assertions.assertThat(getDriver().findElement(By.id("booksTitle")).getText()).contains(searchParam);
    }

    private void checkSeveralResults() {
        Assertions.assertThat("Meetings").isEqualTo(getDriver().findElement(By.id("page title")).getText());
    }

    private void checkNoResults() {
        Assertions.assertThat("Has not been found").isEqualTo(getDriver().findElement(By.id("meeting.errors")).getText());
    }
}