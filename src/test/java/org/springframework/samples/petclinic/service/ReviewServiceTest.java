
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ReviewServiceTest {

	@Autowired
	private ReviewService sut;


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
	void shouldDeleteReview() {
		int reviewId = 1;
		Assertions.assertThat(this.sut.existsReviewById(reviewId)).isTrue();
		this.sut.deleteReviewById(reviewId);
		Assertions.assertThat(this.sut.existsReviewById(reviewId)).isFalse();
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
	
}
