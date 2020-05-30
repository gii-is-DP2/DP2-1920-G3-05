
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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
	public List<Integer> getReviewsIdFromBook(int bookId)  {
		return this.reviewRepo.getReviewsIdFromBook(bookId);
	}

	@Transactional
	public List<Review> getReviewsFromBook(int bookId)  {
		return this.reviewRepo.getReviewsFromBook(bookId);
	}

	@Transactional
	@Modifying
	public void deleteReviewById(int reviewId, String username) throws CantDeleteReviewException {
		Boolean canDeleteReview = this.canDeleteReview(reviewId, username);
		if(Boolean.TRUE.equals(canDeleteReview)) {
			this.reviewRepo.deleteReviewById(reviewId);
		}else {
			throw new CantDeleteReviewException();
		}
	}

	@Transactional
	public Review findReviewById(int reviewId)  {
		return this.reviewRepo.findById(reviewId);
	}

	@Transactional 
	public Boolean existsReviewById(int reviewId)  {
		return this.reviewRepo.existsById(reviewId);
	}

	@Transactional
	@Modifying
	public void writeReview(Review review, String username) throws CantWriteReviewException {
		Boolean canWriteReview = this.canWriteReview(review.getBook().getId(), username);
		if(Boolean.TRUE.equals(canWriteReview)) {
			this.reviewRepo.save(review);
		}else {
			throw new CantWriteReviewException();
		}
	}

	@Transactional
	@Modifying
	public void editReview(Review review, String username) throws CantEditReviewException {
		Boolean isMine = this.reviewIsMine(review.getId(), username);
		if(Boolean.TRUE.equals(isMine)) {
			this.reviewRepo.save(review);
		}else {
			throw new CantEditReviewException();
		}	
	}

	@Transactional
	public Boolean alreadyReviewedBook(int bookId, String username) {
		Review review = this.reviewRepo.getReviewByBookIdAndUsername(bookId, username);
		return review != null;
	}

	@Transactional
	public Boolean reviewIsMine(int reviewId, String username)  {
		Review review = this.reviewRepo.findById(reviewId);
		return review.getUser().getUsername().equals(username);
	}

	public Boolean canWriteReview(int bookId, String username)  {
		Boolean bookIsRead = this.readBookService.esReadBook(bookId, username);
		Boolean alreadyReviewed = this.alreadyReviewedBook(bookId, username);
		return Boolean.TRUE.equals(bookIsRead) && !alreadyReviewed;
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
		return isMine || imAdmin;
	}
	
	@Transactional
	public List<Integer> topRaitedBooks() {
		return this.reviewRepo.getTopRaitedBooks();
	}
	
	@Transactional
	public Double getRaitingBooks(int bookId) {
		return this.reviewRepo.getRaitingBooks(bookId);
	}

}
