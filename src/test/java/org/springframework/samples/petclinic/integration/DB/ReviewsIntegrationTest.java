package org.springframework.samples.petclinic.integration.DB;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.BookRepository;
import org.springframework.samples.petclinic.repository.ReviewRepository;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles; 

@ActiveProfiles("mysql") 
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class)) 
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE) 
 class ReviewsIntegrationTest {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookRepository bookRepository;

	@ParameterizedTest
	@CsvSource({
		"admin1,5,Test review 1","owner1,8,Test review 2"
	})
	 void shouldCreateReview(String username,int bookId,String title) {
		User user = this.userRepository.findByUsername(username);
		Book book = this.bookRepository.findById(bookId); 
		Integer rating = 3;
		String opinion = "Good";
		Review review = new Review();
		review.setBook(book);
		review.setOpinion(opinion);
		review.setRaiting(rating);
		review.setTitle(title);
		review.setUser(user);
		this.reviewRepository.save(review);
		Assertions.assertThat(this.reviewRepository.findById(review.getId()).getBook().getId()).isEqualTo(bookId);
		Assertions.assertThat(this.reviewRepository.findById(review.getId()).getTitle()).isEqualTo(title);
		Assertions.assertThat(this.reviewRepository.findById(review.getId()).getUser().getUsername()).isEqualTo(username);
	}
	
	@ParameterizedTest
	@CsvSource({
		"1,true","14,true","20,false"
	})
	 void shouldGetExistReviewById(int reviewId,boolean result) {
		Assertions.assertThat(this.reviewRepository.existsById(reviewId)).isEqualTo(result);
	}
	
	@ParameterizedTest
	@CsvSource({
		"1,owner1,1","10,reader1,12"
	})
	 void shouldFindReviewById(int reviewId,String username,int bookId) {
		Review review = this.reviewRepository.findById(reviewId);
		Assertions.assertThat(review.getBook().getId()).isEqualTo(bookId);
		Assertions.assertThat(review.getUser().getUsername()).isEqualTo(username);
	}
	
	@ParameterizedTest
	@CsvSource({
		"Test title update 1,6","Test title update 2,9"
	})
	 void shouldUpdateReview(String title,int reviewId) {
		Review review = this.reviewRepository.findById(reviewId);
		review.setTitle(title);
		this.reviewRepository.save(review);
		Assertions.assertThat(this.reviewRepository.findById(reviewId).getTitle()).isEqualTo(title);
	}
	
	@ParameterizedTest
	@CsvSource({
		"2","5"
	})
	 void shouldDeleteReviewById(int id) {
		this.reviewRepository.deleteReviewById(id);
		Assertions.assertThat(this.reviewRepository.findById(id)).isNull();
	}
	
	
	
}
