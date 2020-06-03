package org.springframework.samples.petclinic.repository;

import java.util.List;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewRepository {

	@Transactional(readOnly = true)
	List<Integer> getReviewsIdFromBook(int bookId) ;
	
	@Transactional(readOnly = true)
	List<Review> getReviewsFromBook(int bookId) ;
	
	@Transactional
	@Modifying
	void deleteReviewById(int reviewId) ;
	
	@Transactional(readOnly = true)
	Review findById(int reviewId) ;
	
	@Transactional(readOnly = true)
	Boolean existsById(int reviewId) ;
	
	@Transactional
	@Modifying
	Review save(Review review) ;
	
	@Transactional
	Review getReviewByBookIdAndUsername(int bookId, String username) ;

	@Transactional
	List<Integer> getTopRaitedBooks() ;
	
	@Transactional
	Double getRaitingBooks(int bookId) ;
}
