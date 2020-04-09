
package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.assertj.core.api.Assertions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class AddToReadBooksStepDefinitions extends AbstractStep {

	@LocalServerPort
	private int		port;
	private int		booksAtTheBeginningInReadBooks;
	private int		booksAtTheBeginningInWishList;
	private String	bookTitle;


	@When("I try to add the book {string} to the list of read books")
	public void tryToAddAbook(final String bookName) throws Exception {
		this.goToReadBooks();
		this.bookTitle = bookName;
		this.booksAtTheBeginningInReadBooks = this.countBooks();
		this.goToBooks();
		this.whenITryToAddTheBook(bookName);
	}
	@Then("I can see that the book was added")
	public void checkWasAddedSuccesfully() {
		this.goToReadBooks();
		Assertions.assertThat(this.countBooks()).isGreaterThan(this.booksAtTheBeginningInReadBooks);
		Assertions.assertThat(this.getDriver().findElement(By.xpath("//table[@id='booksTable']")).getText().contains(this.bookTitle)).isTrue();
		this.stopDriver();
	}
	@When("I try to add the book {string} from to read list to the list of read books")
	public void tryToAddAbookToReadList(final String bookName) throws Exception {
		this.goToReadBooks();
		this.bookTitle = bookName;
		this.booksAtTheBeginningInReadBooks = this.countBooks();
		this.goToWishList();
		this.booksAtTheBeginningInWishList = this.countBooks();
		this.whenITryToAddTheBook(bookName);
	}
	@Then("I can see that the book was added and the wish list was updated")
	public void checkWasAddedSuccesfullyAndTheToReadListWasUpdated() {
		this.goToReadBooks();
		Assertions.assertThat(this.countBooks()).isGreaterThan(this.booksAtTheBeginningInReadBooks);
		Assertions.assertThat(this.getDriver().findElement(By.xpath("//table[@id='booksTable']")).getText().contains(this.bookTitle)).isTrue();
		this.goToWishList();
		Assertions.assertThat(this.countBooks()).isLessThan(this.booksAtTheBeginningInWishList);
		this.stopDriver();
	}
	@When("I try to add a book with URL that I have already add to read books")
	public void tryToAddAbookToReadBooksNegative() throws Exception {
		URLOfBookThatWasAdded();
	}
	@Then("I see a message that was already added")
	public void checkMessage() {
	    assertEquals("you have already read the book!", getDriver().findElement(By.xpath("//body/div/div/div")).getText());
		this.stopDriver();
	}

	private void whenITryToAddTheBook(final String bookName) {
		this.getDriver().findElement(By.xpath("//a[contains(text(),'" + bookName + "')]")).click();
		this.getDriver().findElement(By.xpath("//a[contains(text(),'Add to read books')]")).click();

	}

	private void goToReadBooks() {
		this.getDriver().findElement(By.xpath("//a[contains(@href, '/books/readBooks')]")).click();
	}
	private void URLOfBookThatWasAdded() {
		this.getDriver().get("http://localhost:" + this.port + "/books/readBooks/1");
	}
	private void goToBooks() {
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
	private void goToWishList() {
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a/span[2]")).click();

	}
	private int countBooks() {
		WebElement booksTable = this.getDriver().findElement(By.xpath("//table[1]"));
		List<WebElement> books = booksTable.findElements(By.tagName("tr"));
		return books.size();

	}

}
