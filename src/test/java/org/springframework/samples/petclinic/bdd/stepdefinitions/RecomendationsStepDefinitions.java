
package org.springframework.samples.petclinic.bdd.stepdefinitions;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.springframework.boot.web.server.LocalServerPort;


import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class RecomendationsStepDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;

	@When("I try to see my recomendations")
	@When("I try to see my recomendations without read books")
	@When("I try to see my recomendations but there is no more books of the same genre")
	public void goRecomendations() throws Exception {
		goToRecomendations();
	}

	@Then("I see a message saying I need to read at least one book")
	public void checkNoReadBooks() throws Exception {
		this.thenISeeAMessageSayingINeedToReadABook();
		this.stopDriver();
	}

	@Then("I see a message saying what is my favourite genre and that i have read all their books of this genre")
	public void checkFavouriteGenre() throws Exception {
		this.thenISeeAMessageSayingWhatIsMyGenreFavourite();
		this.stopDriver();
	}
	@Then("I see a list of recommended books")
	public void checkWell() throws Exception {
		Assertions.assertThat(countBooks()).isGreaterThan(0);
		this.stopDriver();
	}
	
	

	private void thenISeeAMessageSayingINeedToReadABook() {
		Assert. assertEquals("You need to have at least 1 book marked as read", getDriver().findElement(By.xpath("//h2")).getText());
		}
	private void thenISeeAMessageSayingWhatIsMyGenreFavourite() {
		Assert.assertEquals("your most read genre is horror but you have read all our books of this genre", getDriver().findElement(By.xpath("//h2")).getText());
		}
	private void goToRecomendations() {
   	 	getDriver().findElement(By.xpath("//a[contains(@href, '/books/recomendations')]")).click();
	}
    private int countBooks() {
        WebElement booksTable =getDriver().findElement(By.xpath("//table[1]"));
        List<WebElement> books = booksTable.findElements(By.tagName("tr"));
        return books.size();

    }
       

}
