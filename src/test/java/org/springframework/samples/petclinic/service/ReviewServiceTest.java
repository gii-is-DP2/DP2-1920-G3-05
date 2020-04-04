
package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

	@ParameterizedTest
	@CsvSource({
		"1,3",
		"2,1",
		"7,0"
	})
	void shouldFindReviewsIdByBookId(int bookId, int results) {
		List<Integer> reviewsId = this.sut.getReviewsIdFromBook(bookId);
		Assertions.assertThat(reviewsId.size()).isEqualTo(results);
	}

	@ParameterizedTest
	@CsvSource({
		"1,3",
		"2,1",
		"7,0"
	})
	void shouldFindReviewsByBookId(int bookId, int results) {
		List<Review> reviews = this.sut.getReviewsFromBook(bookId);
		Assertions.assertThat(reviews.size()).isEqualTo(results);
	}
	
	@ParameterizedTest
	@CsvSource({
		"1,admin1",
		"3,vet1",
		"1,owner1"
	})
	void isAlreadyReviewed(int bookId, String username) {
		Boolean alreadyReviewed = this.sut.alreadyReviewedBook(bookId, username);
		Assertions.assertThat(alreadyReviewed).isTrue();
	}
	
	@ParameterizedTest
	@CsvSource({
		"3,owner1",
		"2,vet1",
		"2,admin1"
	})
	void isNotAlreadyReviewed(int bookId, String username) {
		Boolean alreadyReviewed = this.sut.alreadyReviewedBook(bookId, username);
		Assertions.assertThat(alreadyReviewed).isFalse();
	}
	
	@ParameterizedTest
	@CsvSource({
		"1,owner1",
		"2,admin1",
		"3,vet1"
	})
	void reviewIsMine(int reviewId, String username) {
		Boolean isMine = this.sut.reviewIsMine(reviewId, username);
		Assertions.assertThat(isMine).isTrue();
	}
	
	@ParameterizedTest
	@CsvSource({
		"2,owner1",
		"1,admin1",
		"1,vet1"
	})
	void reviewIsNotMine(int reviewId, String username) {	
		Boolean isMine = this.sut.reviewIsMine(reviewId, username);
		Assertions.assertThat(isMine).isFalse();
	}
	
	@ParameterizedTest
	@CsvSource({
		"2,vet1",
		"4,owner1",
		"11,admin1"
	})
	void canWriteReview(int bookId, String username) {
		Boolean canWriteReview = this.sut.canWriteReview(bookId, username);
		Assertions.assertThat(canWriteReview).isTrue();
		
	}
	
	@ParameterizedTest
	@CsvSource({
		"2,admin1",
		"4,vet1",
		"3,owner1"
	})
	void cantWriteReviewBecauseBookIsNotRead(int bookId, String username) {
		Boolean canWriteReview = this.sut.canWriteReview(bookId, username);
		Assertions.assertThat(canWriteReview).isFalse();
	}
	
	@ParameterizedTest
	@CsvSource({
		"1,admin1",
		"4,vet1",
		"1,owner1"
	}) 
	void cantWriteReviewBecauseAlreadyReviewedBook(int bookId, String username) {
		Boolean canWriteReview = this.sut.canWriteReview(bookId, username);
		Assertions.assertThat(canWriteReview).isFalse();
	}

	@ParameterizedTest
	@CsvSource({
		"2,admin1",
		"4,vet1",
		"3,owner1"
	})
	void userWithNoReadBookWritesReview(int bookId, String username) {
		User user = this.userService.findUserByUsername(username);
		Book book = this.bookService.findBookById(bookId); 
		String title = "Test review";
		Integer rating = 3;
		String opinion = "Good";
		Review review = new Review();
		review.setBook(book);
		review.setOpinion(opinion);
		review.setRaiting(rating);
		review.setTitle(title);
		review.setUser(user);

		assertThrows(CantWriteReviewException.class, ()-> this.sut.writeReview(review, username));
	}

	@ParameterizedTest
	@CsvSource({
		"1,admin1",
		"4,vet1",
		"1,owner1"
	}) 	void userWithBookReviewedWritesAnotherReview(int bookId, String username) {
		User user = this.userService.findUserByUsername(username);
		Book book = this.bookService.findBookById(bookId);
		String title = "Test review";
		Integer rating = 3;
		String opinion = "Test opinion";
		Review review = new Review();
		review.setBook(book);
		review.setOpinion(opinion);
		review.setRaiting(rating);
		review.setTitle(title);
		review.setUser(user);

		assertThrows(CantWriteReviewException.class, ()-> this.sut.writeReview(review, username));
	}

	@ParameterizedTest
	@CsvSource({
		"2,vet1,8",
		"4,owner1,9",
		"11,admin1,10"
	})
	void userWritesReviewCorrectly(int bookId, String username, int futureId) {
		User user = this.userService.findUserByUsername(username);
		Book book = this.bookService.findBookById(bookId); 
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
			this.sut.writeReview(review, username);
		}catch (CantWriteReviewException e){}

		int id = review.getId();
		Review savedReview = this.sut.findReviewById(id);
		Assertions.assertThat(savedReview.getOpinion()).isEqualTo(opinion);
		Assertions.assertThat(savedReview.getRaiting()).isEqualTo(rating);
		Assertions.assertThat(savedReview.getTitle()).isEqualTo(title);
		Assertions.assertThat(savedReview.getBook()).isEqualTo(book);
		Assertions.assertThat(savedReview.getUser()).isEqualTo(user);
	}

	@ParameterizedTest
	@CsvSource({
		"1,owner1",
		"2,admin1",
		"3,vet1"
	})
	void userEditHisReview(int reviewId, String username){
		Review reviewToEdit = this.sut.findReviewById(reviewId);
		String newTitle = "Edited title";
		String newOpinion = "Edited opinion";
		Integer newRating = 5;
		reviewToEdit.setOpinion(newOpinion);
		reviewToEdit.setTitle(newTitle);
		reviewToEdit.setRaiting(newRating);
		try{
			this.sut.editReview(reviewToEdit, username);
		}catch(CantEditReviewException e){}
		Review editedReview = this.sut.findReviewById(reviewId);
		Assertions.assertThat(editedReview.getTitle()).isEqualTo(newTitle);
		Assertions.assertThat(editedReview.getOpinion()).isEqualTo(newOpinion);
		Assertions.assertThat(editedReview.getRaiting()).isEqualTo(newRating);
	}

	@ParameterizedTest
	@CsvSource({
		"2,owner1",
		"1,admin1",
		"1,vet1"
	})
	void userCantEditOthersReview(int reviewId, String username) {
		final Review review = this.sut.findReviewById(reviewId);
		assertThrows(CantEditReviewException.class, ()-> this.sut.editReview(review, username));
	}

	@ParameterizedTest
	@CsvSource({
		"1,owner1",
		"2,admin1",
		"3,vet1"
	})
	void shouldDeleteMyReview(int reviewId, String username) { 
		try {
			this.sut.deleteReviewById(reviewId, username);
		}catch (Exception e) {}
			Boolean existsReview = this.sut.existsReviewById(reviewId);
			Assertions.assertThat(existsReview).isFalse();
	}
	
	@ParameterizedTest
	@CsvSource({
		"1,admin1",
		"3,admin1",
		"4,admin1"
	})
	void AdminShouldDeleteAnyReview(int reviewId, String username) { 
		try {
			this.sut.deleteReviewById(reviewId, username);
		}catch (Exception e) {}
			Boolean existsReview = this.sut.existsReviewById(reviewId);
			Assertions.assertThat(existsReview).isFalse();
	}
	
	@ParameterizedTest
	@CsvSource({
		"3,owner1",
		"1,vet1"
	})
	void shouldNotDeleteOthersReview(int reviewId, String username) { 
		assertThrows(CantDeleteReviewException.class, ()-> this.sut.deleteReviewById(reviewId, username));
	}
	
	@ParameterizedTest
	@CsvSource({
		"4,9,13","3,12,7","8,2,10"})
	void shouldGetTopRaitedBooks(int bookId1, int bookId2, int bookId3) { 
		List<Integer> topRaited = this.sut.topRaitedBooks();
		Assertions.assertThat(topRaited).contains(bookId1,bookId2);
		Assertions.assertThat(topRaited).doesNotContain(bookId3);
	}
	
	@ParameterizedTest
	@CsvSource({
		"2,5.0","11,4.0","6,3.5"})
	void shouldGetRaitingBooks(int bookId, double raiting) { 
		Assertions.assertThat(this.sut.getRaitingBooks(bookId)).isEqualTo(raiting);
	}
}
