package org.springframework.samples.petclinic.bdd.stepdefinitions;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class AddBookStepDefinitions extends AbstractStep{

    @LocalServerPort
    private int port;

    private int numberBooks;

    @When("I try to add a book with name {string}, genre {string}, writed by {string}, published on {string}, {string} pages, editorial {string}, ISBN {string}, synopsis {string} and image {string}")
    public void addBook(String name, String genre, String author, String date, String pages, String editorial, String ISBN, String synopsis, String image) throws Exception {
        goToBooksList();
        numberBooks = countBooks();
        fillForm(name, genre, author, date, pages, editorial, ISBN, synopsis, image);
        submitForm();
    }

    @Then("the book {string} will be added and it will not be verified")
    public void assertBookAddedNotVerified(String name) throws Exception {
        int newNumberBooks = countBooks();
        Assertions.assertThat(numberBooks + 1).isEqualTo(newNumberBooks);
        goToBook(name);
        Assertions.assertThat(getDriver().findElement(By.id("verified")).getText()).contains("No");
        stopDriver();
    }
    
    @Then("the book {string} will be added and it will be verified")
    public void asserBookAddedVerified(String name) throws Exception {
        int newNumberBooks = countBooks();
        Assertions.assertThat(numberBooks + 1).isEqualTo(newNumberBooks);
        goToBook(name);
        Assertions.assertThat(getDriver().findElement(By.id("verified")).getText()).contains("Yes");
        stopDriver();
    }

    @Then("the system will indicate that it is not a valid ISBN")
    public void invalidIsbn() throws Exception {
        Assertions.assertThat(getDriver().findElement(By.xpath("//form[@id='book']/div/div[7]/div/span[2]")).getText()).isEqualTo("Enter a valid ISBN");
        stopDriver();
    }

    @Then("the system will indicate that the ISBN is already in use")
    public void duplicatedIsbn() throws Exception {
        Assertions.assertThat(getDriver().findElement(By.xpath("//form[@id='book']/div/div[7]/div/span[2]")).getText()).isEqualTo("Is already in use");
        stopDriver();
    }

    private void goToBook(String name) {
        getDriver().findElement(By.linkText(name)).click();
    }

    private void goToBooksList() {
        getDriver().findElement(By.id("findBook")).click();
        getDriver().findElement(By.xpath("//button[@type='submit']")).click();
    }

    private int countBooks() {
        WebElement booksTable =getDriver().findElement(By.xpath("//table[1]"));
        List<WebElement> books = booksTable.findElements(By.tagName("tr"));
        return books.size();
    }

    private void fillForm(String name, String genre, String author, String date, String pages,
            String editorial, String ISBN, String synopsis, String image) {
            getDriver().findElement(By.id("findBook")).click();
            getDriver().findElement(By.linkText("Add Book")).click();
            getDriver().findElement(By.id("title")).click();
            getDriver().findElement(By.id("title")).clear();
            getDriver().findElement(By.id("title")).sendKeys(name);
            getDriver().findElement(By.id("author")).click();
            getDriver().findElement(By.id("author")).clear();
            getDriver().findElement(By.id("author")).sendKeys(author);
            getDriver().findElement(By.id("editorial")).click();
            getDriver().findElement(By.id("editorial")).clear();
            getDriver().findElement(By.id("editorial")).sendKeys(editorial);
            new Select(getDriver().findElement(By.id("genre"))).selectByVisibleText(genre);
            getDriver().findElement(By.id("pages")).click();
            getDriver().findElement(By.id("pages")).clear();
            getDriver().findElement(By.id("pages")).sendKeys(pages);
            getDriver().findElement(By.id("synopsis")).click();
            getDriver().findElement(By.id("synopsis")).clear();
            getDriver().findElement(By.id("synopsis")).sendKeys(synopsis);
            getDriver().findElement(By.id("ISBN")).click();
            getDriver().findElement(By.id("ISBN")).clear();
            getDriver().findElement(By.id("ISBN")).sendKeys(ISBN);
            getDriver().findElement(By.id("publicationDate")).click();
            getDriver().findElement(By.id("publicationDate")).clear();
            getDriver().findElement(By.id("publicationDate")).sendKeys(date);
            getDriver().findElement(By.id("image")).click();
            getDriver().findElement(By.id("image")).clear();
            getDriver().findElement(By.id("image")).sendKeys(image);
    }

    private void submitForm() {
            getDriver().findElement(By.xpath("//button[@type='submit']")).click();
    }
    
}