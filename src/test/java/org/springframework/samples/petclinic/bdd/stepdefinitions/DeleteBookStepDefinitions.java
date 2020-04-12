package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteBookStepDefinitions extends AbstractStep {

    private boolean acceptNextAlert = true;
    private int booksAtTheBeginning;
    private String bookTitle;

    @LocalServerPort
    private int port;
    
    @Given("I am logged withh username {string} and password {string}")
    public void IdoLogginAsAdmin(String username, String paasword) throws Exception {
        loginAs(username, paasword, port);
    }

    @When("I try to delete a book")
    public void IdeleteBook() throws Exception{
        deleteBook();
    }

    @And("I confirm it")
    public void IconfirmDelete() {
      confirm();
    }

    @Then("the book is deleted and it does no appear in the books' list")
    public void assertBookIsDeleted() throws Exception{
        Assertions.assertThat(countBooks()).isLessThan(booksAtTheBeginning);
        Assertions.assertThat(getDriver().findElement(By.xpath("//table[@id='booksTable']")).getText().contains(bookTitle)).isFalse();
        stopDriver();
    }
          
    @And("I do not confirm it")
    public void IdoNotConfirmDelete() {
      notConfirm();
    }
    
    @Then("the book is not deleted")
    public void notDeletedBook(){
      goToBookList();
      Assertions.assertThat(booksAtTheBeginning).isEqualTo(countBooks());
      stopDriver();
    }

    @When("I try to delete a book through URL")
    public void goToDeleteBookUrl() throws Exception {
        goToBookList();
        booksAtTheBeginning = countBooks();
        insertUlr();
    }

    @Then("the system does not allow me")
    public void IdidNotDeleteBook() throws Exception {
        Assertions.assertThat(getDriver().findElement(By.xpath("//blockquote/p")).getText()).isEqualTo("UPS!");
        goToBookList();
        Assertions.assertThat(booksAtTheBeginning).isEqualTo(countBooks());
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

    private int countBooks() {
        WebElement booksTable =getDriver().findElement(By.xpath("//table[1]"));
        List<WebElement> books = booksTable.findElements(By.tagName("tr"));
        return books.size();

    }
       
    public void deleteBook() {
        goToBookList();
        booksAtTheBeginning = countBooks();
        bookTitle = "Destinos divididos";
        getDriver().findElement(By.linkText(bookTitle)).click();
    }

    public void confirm() {
        acceptNextAlert = true;
        getDriver().findElement(By.id("deleteBook")).click();
        Assertions.assertThat(closeAlertAndGetItsText().matches("^Are you sure you want to delete this book[\\s\\S]$")).isTrue();
    }

    public void notConfirm() {
      acceptNextAlert = false;
      getDriver().findElement(By.id("deleteBook")).click();
      Assertions.assertThat(closeAlertAndGetItsText().matches("^Are you sure you want to delete this book[\\s\\S]$")).isTrue();
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

  private void goToBookList() {
      getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
      getDriver().findElement(By.xpath("//button[@type='submit']")).click();
  }

  private void insertUlr() {
    getDriver().get("http://localhost:" + port + "/admin/books/delete/1");
 }

}