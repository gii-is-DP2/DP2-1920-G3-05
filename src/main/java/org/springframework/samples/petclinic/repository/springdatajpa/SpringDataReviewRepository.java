
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.samples.petclinic.repository.ReviewRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataReviewRepository extends CrudRepository<Review, Integer>, ReviewRepository {

	@Override
	@Query("SELECT review.id FROM Review review WHERE review.book.id = ?1")
	@Transactional(readOnly = true)
	public List<Integer> getReviewsIdFromBook(int bookId);
	
	@Override
	@Query("SELECT review FROM Review review WHERE review.book.id = ?1")
	@Transactional(readOnly = true)
	public List<Review> getReviewsFromBook(int bookId);
	
	@Override 
	@Query("DELETE FROM Review WHERE id=?1") 
	@Transactional 
	@Modifying 
	void deleteReviewById(int reviewId); 

	@Override
	@Transactional
	@Query("SELECT review FROM Review review WHERE review.book.id = ?1 AND review.user.username = ?2")
	Review getReviewByBookIdAndUsername(int bookId, String username) ;

	@Override
	@Transactional
	@Query("SELECT AVG(review.raiting) FROM Review review WHERE review.book.id = ?1")
	Double getRaitingBooks(int bookId);
	
	@Override
	@Transactional
	@Query(value="SELECT book_id FROM Reviews GROUP BY book_id ORDER BY (AVG(raiting*20)) DESC Limit 0,10", nativeQuery=true)
	List<Integer> getTopRaitedBooks();
	
}
