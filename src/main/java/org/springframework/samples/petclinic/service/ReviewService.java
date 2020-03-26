
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.samples.petclinic.repository.ReviewRepository;
import org.springframework.samples.petclinic.service.exceptions.CantDeleteReviewException;
import org.springframework.samples.petclinic.service.exceptions.CantEditReviewException;
import org.springframework.samples.petclinic.service.exceptions.CantWriteReviewException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

	private ReviewRepository reviewRepo;

	private ReadBookService readBookService;

	private AuthoritiesService authoritiesService;
	
	@Autowired
	public ReviewService(ReviewRepository reviewRepo, ReadBookService readBookService, AuthoritiesService authoritiesService){
		this.reviewRepo = reviewRepo;
		this.readBookService = readBookService;
		this.authoritiesService = authoritiesService;
	}

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
	public void deleteReviewById(int reviewId, String username) throws DataAccessException, CantDeleteReviewException {
		Boolean canDeleteReview = this.canDeleteReview(reviewId, username);
		if(canDeleteReview) {
			this.reviewRepo.deleteReviewById(reviewId);
		}else {
			throw new CantDeleteReviewException();
		}
	}

	@Transactional
	public Review findReviewById(int reviewId) throws DataAccessException {
		return this.reviewRepo.findById(reviewId);
	}

	@Transactional 
	public Boolean existsReviewById(int reviewId) throws DataAccessException {
		return this.reviewRepo.existsById(reviewId);
	}

	@Transactional
	@Modifying
	public void writeReview(Review review, String username) throws DataAccessException, CantWriteReviewException {
		Boolean canWriteReview = this.canWriteReview(review.getBook().getId(), username);
		if(canWriteReview) {
			this.reviewRepo.save(review);
		}else {
			throw new CantWriteReviewException();
		}
	}

	@Transactional
	@Modifying
	public void editReview(Review review, String username) throws DataAccessException, CantEditReviewException {
		Boolean isMine = this.reviewIsMine(review.getId(), username);
		if(isMine) {
			this.reviewRepo.save(review);
		}else {
			throw new CantEditReviewException();
		}	
	}

	@Transactional
	public Boolean alreadyReviewedBook(int bookId, String username) throws DataAccessException{
		Review review = this.reviewRepo.getReviewByBookIdAndUsername(bookId, username);
		if(review == null) {
			return false;
		}else {
			return true;
		}
	}

	@Transactional
	public Boolean reviewIsMine(int reviewId, String username) throws DataAccessException {
		Review review = this.reviewRepo.findById(reviewId);
		if(review.getUser().getUsername().equals(username)) {
			return true;
		}else{
			return false;
		}
	}

	public Boolean canWriteReview(int bookId, String username) throws DataAccessException {
		Boolean bookIsRead = this.readBookService.esReadBook(bookId, username);
		Boolean alreadyReviewed = this.alreadyReviewedBook(bookId, username);
		if(bookIsRead && !alreadyReviewed) {
			return true;
		}else {
			return false;
		}
	}

	public Boolean canDeleteReview(int reviewId, String username) {
		//Solo las borra el admin o el duenyo
		Boolean isMine = this.reviewIsMine(reviewId, username);

		List<Authorities> authorities = this.authoritiesService.getAuthoritiesByUsername(username);
		Boolean imAdmin = false;
		for(Authorities a: authorities){
			if(a.getAuthority().equals("admin")){
				imAdmin = true;
			}
		}
		if(isMine || imAdmin) {
			return true;
		}else {
			return false;
		}

	}

}
