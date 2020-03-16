
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.samples.petclinic.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepo;

	@Transactional
	public List<Integer> getReviewsIdFromBook(int bookId) throws DataAccessException {
		return this.reviewRepo.getReviewsIdFromBook(bookId);
	}
	
	@Transactional
	public List<Review> getReviewsFromBook(int bookId) throws DataAccessException {
		return this.reviewRepo.getReviewsFromBook(bookId);
	}
	
	@Transactional
	@Modifying
	public void deleteReviewById(int reviewId) throws DataAccessException {
		this.reviewRepo.deleteReviewById(reviewId);
	}
	
	@Transactional
	public Review findReviewById(int reviewId) throws DataAccessException {
		return this.reviewRepo.findById(reviewId);
	}
	
	@Transactional 
	public Boolean existsReviewById(int reviewId) throws DataAccessException{
		return this.reviewRepo.existsById(reviewId);
	}
	
	@Transactional
	@Modifying
	public void save(Review review) throws DataAccessException{
		this.reviewRepo.save(review);
	}

}
