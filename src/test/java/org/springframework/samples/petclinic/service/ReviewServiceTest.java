
package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.CantDeleteReviewException;
import org.springframework.samples.petclinic.service.exceptions.CantEditReviewException;
import org.springframework.samples.petclinic.service.exceptions.CantWriteReviewException;
import org.springframework.stereotype.Service;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ReviewServiceTest {

	@Autowired
	private ReviewService sut;

	@Autowired
	private BookService bookService;

	@Autowired
	private UserService userService;

	@Test
	void shouldFindReviewsIdByBookId() {
		int bookId = 1;
		List<Integer> reviewsId = this.sut.getReviewsIdFromBook(bookId);
		Assertions.assertThat(reviewsId.size()).isEqualTo(3);
		Assertions.assertThat(reviewsId).contains(1,2,3);
		
		bookId = 2;
		reviewsId = this.sut.getReviewsIdFromBook(bookId);
		Assertions.assertThat(reviewsId.size()).isEqualTo(1);
		Assertions.assertThat(reviewsId).contains(4);

		bookId = 7;
		reviewsId = this.sut.getReviewsIdFromBook(bookId);
		Assertions.assertThat(reviewsId).isEmpty();
	}

	@Test
	void shouldFindReviewsByBookId() {
		int bookId = 1;
		List<Review> reviews = this.sut.getReviewsFromBook(bookId);
		Assertions.assertThat(reviews.size()).isEqualTo(3);
		
		bookId = 2;
		reviews = this.sut.getReviewsFromBook(bookId);
		Assertions.assertThat(reviews.size()).isEqualTo(1);

		bookId = 7;
		reviews = this.sut.getReviewsFromBook(bookId);
		Assertions.assertThat(reviews).isEmpty();
	}
	
	@Test
	void isAlreadyReviewed() {
		int bookId = 1;
		String username = "admin1";
		Boolean alreadyReviewed = this.sut.alreadyReviewedBook(bookId, username);
		Assertions.assertThat(alreadyReviewed).isTrue();
	}
	
	@Test
	void isNotAlreadyReviewed() {
		int bookId = 7;
		String username = "admin1";
		Boolean alreadyReviewed = this.sut.alreadyReviewedBook(bookId, username);
		Assertions.assertThat(alreadyReviewed).isFalse();
	}
	
	@Test
	void reviewIsMine() {
		int reviewId = 1;
		String username = "owner1";
		Boolean isMine = this.sut.reviewIsMine(reviewId, username);
		Assertions.assertThat(isMine).isTrue();
	}
	
	@Test
	void reviewIsNotMine() {
		int reviewId = 1;
		String username = "admin1";		
		Boolean isMine = this.sut.reviewIsMine(reviewId, username);
		Assertions.assertThat(isMine).isFalse();
	}
	
	@Test
	void canWriteReview() {
		int bookId = 2;
		//El usuario vet1 tiene el libro 2 como leido y ademas no ha escrito una review del mismo luego si podra
		String username = "vet1";
		Boolean canWriteReview = this.sut.canWriteReview(bookId, username);
		Assertions.assertThat(canWriteReview).isTrue();
		
	}
	
	@Test 
	void cantWriteReviewBecauseBookIsNotRead() {
		int bookId = 7;
		//El user admin1 no se ha leido el libro con id 7
		String username = "admin1";
		Boolean canWriteReview = this.sut.canWriteReview(bookId, username);
		Assertions.assertThat(canWriteReview).isFalse();
	}
	
	@Test 
	void cantWriteReviewBecauseAlreadyReviewedBook() {
		int bookId = 1;
		//El user admin1 ya ha hecho una review del libro con id 1
		String username = "admin1";
		Boolean canWriteReview = this.sut.canWriteReview(bookId, username);
		Assertions.assertThat(canWriteReview).isFalse();
	}

	@Test
	void userWithNoReadBookWritesReview() {
		User user = this.userService.findUserByUsername("owner1");
		Book book = this.bookService.findBookById(8); //owner1 no Ha leido el libro con id 8
		String title = "Test review";
		Integer rating = 3;
		String opinion = "Good";
		Review review = new Review();
		review.setBook(book);
		review.setOpinion(opinion);
		review.setRaiting(rating);
		review.setTitle(title);
		review.setUser(user);

		assertThrows(CantWriteReviewException.class, ()-> this.sut.writeReview(review, user.getUsername()));
	}

	@Test
	void userWithBookReviewedWritesAnotherReview() {
		User user = this.userService.findUserByUsername("admin1");
		Book book = this.bookService.findBookById(1); //admin1 ya Ha escrito una review del libto con id 1
		String title = "Test review";
		Integer rating = 3;
		String opinion = "Test opinion";
		Review review = new Review();
		review.setBook(book);
		review.setOpinion(opinion);
		review.setRaiting(rating);
		review.setTitle(title);
		review.setUser(user);

		assertThrows(CantWriteReviewException.class, ()-> this.sut.writeReview(review, user.getUsername()));
	}

	@Test
	void userWritesReviewCorrectly() {
		User user = this.userService.findUserByUsername("owner1");
		Book book = this.bookService.findBookById(4); //Ha leido el 4 y no ha hecho review de el
		String title = "Test review";
		Integer rating = 3;
		String opinion = "Good";
		Review review = new Review();
		review.setBook(book);
		review.setOpinion(opinion);
		review.setRaiting(rating);
		review.setTitle(title);
		review.setUser(user);

		try{
			this.sut.writeReview(review, user.getUsername());
		}catch (CantWriteReviewException e){}

		//Le toca la id 8 
		Review savedReview = this.sut.findReviewById(8);
		Assertions.assertThat(savedReview.getOpinion()).isEqualTo(opinion);
		Assertions.assertThat(savedReview.getRaiting()).isEqualTo(rating);
		Assertions.assertThat(savedReview.getTitle()).isEqualTo(title);
		Assertions.assertThat(savedReview.getBook()).isEqualTo(book);
		Assertions.assertThat(savedReview.getUser()).isEqualTo(user);
	}

	@Test
	void userEditHisReview(){
		Review reviewToEdit = this.sut.findReviewById(1);
		String newTitle = "Edited title";
		String newOpinion = "Edited opinion";
		Integer newRating = 5;
		reviewToEdit.setOpinion(newOpinion);
		reviewToEdit.setTitle(newTitle);
		reviewToEdit.setRaiting(newRating);
		try{
			this.sut.editReview(reviewToEdit, reviewToEdit.getUser().getUsername());
		}catch(CantEditReviewException e){}
		Review editedReview = this.sut.findReviewById(1);
		Assertions.assertThat(editedReview.getTitle()).isEqualTo(newTitle);
		Assertions.assertThat(editedReview.getOpinion()).isEqualTo(newOpinion);
		Assertions.assertThat(editedReview.getRaiting()).isEqualTo(newRating);
	}

	@Test
	void userCantEditOthersReview() {
		final Review review = this.sut.findReviewById(1);
		String username = "vet1"; //La review le pertenece a owner1
		assertThrows(CantEditReviewException.class, ()-> this.sut.editReview(review, username));
	}

	@Test
	void shouldDeleteMyReview() { 
		int reviewId = 1;
		String username = "owner1";
		try {
		this.sut.deleteReviewById(reviewId, username);
		}catch (Exception e) {}
		Boolean existsReview = this.sut.existsReviewById(reviewId);
		Assertions.assertThat(existsReview).isFalse();
	}
	
	@Test
	void AdminShouldDeleteAnyReview() { 
		int reviewId = 7;
		String username = "admin1";
		try {
			this.sut.deleteReviewById(reviewId, username);
		}catch (Exception e) {}
		Boolean existsReview = this.sut.existsReviewById(reviewId);
		Assertions.assertThat(existsReview).isFalse();
	}
	
	@Test
	void shouldNotDeleteOthersReview() { 
		int reviewId = 4;
		String username = "vet1";
		assertThrows(CantDeleteReviewException.class, ()-> this.sut.deleteReviewById(reviewId, username));
	}
}
