
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ReviewServiceTest {

	@Autowired
	private ReviewService sut;


	@Test
	void shouldFindReviewsIdByBookId() {
		int bookId = 1;
		List<Integer> reviewsId = this.sut.getReviewsFromBook(bookId);
		Assertions.assertThat(reviewsId.size()).isEqualTo(3);
		Assertions.assertThat(reviewsId).contains(1,2,3);
		
		bookId = 2;
		reviewsId = this.sut.getReviewsFromBook(bookId);
		Assertions.assertThat(reviewsId.size()).isEqualTo(1);
		Assertions.assertThat(reviewsId).contains(4);

		bookId = 7;
		reviewsId = this.sut.getReviewsFromBook(bookId);
		Assertions.assertThat(reviewsId).isEmpty();;
	}

	@Test
	void shouldDeleteReview() {
		int reviewId = 1;
		Assertions.assertThat(this.sut.existsReviewById(reviewId)).isTrue();
		this.sut.deleteReviewById(reviewId);
		Assertions.assertThat(this.sut.existsReviewById(reviewId)).isFalse();
	}
	
}
