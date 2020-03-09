package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewRepository {

	@Transactional(readOnly = true)
	List<Integer> getReviewsFromBook(int bookId) throws DataAccessException;
	
	@Transactional
	@Modifying
	void deleteReviewById(int reviewId) throws DataAccessException;
	
	@Transactional(readOnly = true)
	Review findById(int reviewId) throws DataAccessException;
	
	@Transactional(readOnly = true)
	Boolean existsById(int reviewId) throws DataAccessException;

}
