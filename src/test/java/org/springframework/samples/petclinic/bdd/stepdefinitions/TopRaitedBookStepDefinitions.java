package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.samples.petclinic.repository.BookRepository;
import org.springframework.samples.petclinic.repository.ReviewRepository;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class TopRaitedBookStepDefinitions extends AbstractStep{

	@Autowired
	ReviewRepository rev;
	
	    @LocalServerPort
	    private int port;
	    
	    @Given("there is no review")
	    public void deleteAllReviews() throws Exception {
	    	deleteReviews();
	    	logOut();
	    }
	    
	    @When("I request to see the best rated books")
	    public void bestRatedBook() throws Exception {
	    	getDriver().findElement(By.id("topRaited")).click();
	    } 
	    
	    @Then("the system shows me a top based on the books with the highest average of their reviews")
	    public void listTopRaited() throws Exception {
	    	Assertions.assertThat("Top best raited books").isEqualTo(getDriver().findElement(By.id("topRaitedBooks")).getText());
	        stopDriver();
	    } 
	    
	    @Then("the system indicates that it cannot be displayed because there is no data required in the application yet")
	    public void emptyTopRaited() throws Exception {
	    	Assertions.assertThat("Not enough data in the app yet").isEqualTo(getDriver().findElement(By.id("emptyTopRaited")).getText());
	        stopDriver();
	    } 
	    
	    public void logOut(){
	        getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	        getDriver().findElement(By.linkText("Logout")).click();
	        getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	      }
	    
	    public void deleteReview(String title,int reviews){
	    	getDriver().findElement(By.id("topRaited")).click();
	        getDriver().findElement(By.linkText(title)).click();
	        for(int i=0;i<reviews;i++) {
	        	 getDriver().findElement(By.id("Go to the reviews")).click();
	        	 if(i==0 && reviews>1) {
	        		 getDriver().findElement(By.linkText("Libro muy recomendable")).click();
	        	 }else if(i==1 && reviews==3) {
	        		 getDriver().findElement(By.linkText("Libro mediocre")).click();
	        	 }
	        	 getDriver().findElement(By.id("Delete review")).click();
	        }
       	 	
	      }
	    
	    public void deleteReviews(){
	    	 deleteReview("Harry Potter y la piedra filosofal", 1);
	    	 deleteReview("La chica de nieve", 1);
	    	 deleteReview("Dispara, yo ya estoy muerto", 1);
	    	 deleteReview("El hijo del italiano", 1);
	    	 deleteReview("Las marcas de la muerte", 2);
	    	 deleteReview("Un cuento perfecto", 1);
	    	 deleteReview("Reina Roja", 1);
	    	 deleteReview("Harry Potter y la camara secreta", 1);
	    	 deleteReview("Dime quien soy", 1);
	    	 deleteReview("IT", 3);
	    	 deleteReview("El Principito", 1);
	      }
	   
	    
	    
	}
