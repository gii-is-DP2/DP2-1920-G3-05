package org.springframework.samples.petclinic.web;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.ReviewService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.CantDeleteReviewException;
import org.springframework.samples.petclinic.service.exceptions.CantEditReviewException;
import org.springframework.samples.petclinic.service.exceptions.CantWriteReviewException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReviewController {

	private ReviewService reviewService;

	private BookService bookService;

	private UserService userService;
	
	private static final String CONSTANT1= "redirect:/oups";
	
	private static final String CONSTANT2= "review";
	
	private static final String CONSTANT3= "redirect:/reviews/";

	@Autowired
	public ReviewController(ReviewService reviewService, BookService bookService, UserService userService) {
		this.reviewService = reviewService;
		this.bookService = bookService;
		this.userService = userService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("book");
		dataBinder.setDisallowedFields("user");
		dataBinder.setValidator(new ReviewValidator());
	}

	@GetMapping(value = "/books/{bookId}/reviews/new")
	public String initCreationForm( @PathVariable("bookId") final int bookId, Map<String, Object> model) {
		Review review = new Review();
		Book book = this.bookService.findBookById(bookId);
		review.setBook(book);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		User me = this.userService.findUserByUsername(userDetail.getUsername());
		review.setUser(me);

		Boolean canWriteReview = this.reviewService.canWriteReview(bookId, userDetail.getUsername());
		if(Boolean.FALSE.equals(canWriteReview)) {
			return CONSTANT1;
		}
		model.put(CONSTANT2, review);
		return "reviews/reviewAddForm";
	}

	@PostMapping(value = "/books/{bookId}/reviews/new")
	public String processCreationForm(@PathVariable("bookId") final int bookId, @Valid Review review, BindingResult result) {
		Book book = this.bookService.findBookById(bookId);
		review.setBook(book);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		User me = this.userService.findUserByUsername(userDetail.getUsername());
		review.setUser(me);

		if (result.hasErrors()) {
			return "reviews/reviewAddForm";
		}else {
			try {
				this.reviewService.writeReview(review, userDetail.getUsername());
				return CONSTANT3 + review.getId();
			}catch (CantWriteReviewException e) {
				return CONSTANT1;
			}
		}
	}

	@GetMapping("/books/{bookId}/reviews")
	public String findReviewsFromBook(Review review, @PathVariable("bookId") final int bookId, final BindingResult result,
			final Map<String, Object> model) {
		List<Review> reviews = this.reviewService.getReviewsFromBook(bookId);
		if (reviews.isEmpty()) {
			// no reviews found
			return "redirect:/books/" + bookId;
		} else if (reviews.size() == 1) {
			// 1 review found
			review = reviews.get(0);
			return CONSTANT3 + review.getId();
		} else {
			// multiple reviews found
			model.put("reviews", reviews);
			return "/reviews/reviewList";
		}
	}

	@GetMapping("/reviews/{reviewId}")
	public ModelAndView showReview(@PathVariable("reviewId") final int reviewId) {
		Review review = this.reviewService.findReviewById(reviewId);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		Boolean isMine = this.reviewService.reviewIsMine(reviewId, userDetail.getUsername());
		Boolean canDeleteReview = this.reviewService.canDeleteReview(reviewId, userDetail.getUsername());

		ModelAndView mav = new ModelAndView("reviews/reviewDetails");
		mav.addObject(CONSTANT2, review);
		mav.addObject("isMine", isMine);
		mav.addObject("canDeleteReview", canDeleteReview);
		return mav;
	}

	@GetMapping("/books/{bookId}/reviews/{reviewId}/edit")
	public String formEdit(final ModelMap modelMap, @PathVariable("reviewId") final int reviewId, @PathVariable("bookId") final int bookId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		Boolean isMine = this.reviewService.reviewIsMine(reviewId, userDetail.getUsername());
		if(Boolean.FALSE.equals(isMine)) {
			return CONSTANT1;
		}
		Review review = this.reviewService.findReviewById(reviewId);
		modelMap.addAttribute(CONSTANT2, review);
		modelMap.addAttribute("bookId", bookId);
		return "reviews/reviewUpdateForm";
	}

	@PostMapping("/books/{bookId}/reviews/{reviewId}/edit")
	public String updateReview(@Valid final Review updatedReview, final BindingResult result, final ModelMap modelMap, @PathVariable("reviewId") final int reviewId, @PathVariable("bookId") final int bookId) {

		if(result.hasErrors()) {
			return "reviews/reviewUpdateForm";
		}else {
			Review review = this.reviewService.findReviewById(reviewId);
			updatedReview.setId(review.getId());
			updatedReview.setBook(review.getBook());
			updatedReview.setUser(review.getUser());
			try {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				UserDetails userDetail = (UserDetails) auth.getPrincipal();
				this.reviewService.editReview(updatedReview, userDetail.getUsername());
				return CONSTANT3+reviewId;
			}catch (CantEditReviewException e) {
				return CONSTANT1;
			}
		}
	}

	@GetMapping("/books/{bookId}/reviews/{reviewId}/delete")
	public String deleteBook(@PathVariable("bookId") final int bookId, @PathVariable("reviewId") final int reviewId) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			this.reviewService.deleteReviewById(reviewId, userDetail.getUsername());
		}catch (CantDeleteReviewException e) {
			return CONSTANT1;
		}
		return "redirect:/books/" + bookId;
	}
}
