package org.springframework.samples.petclinic.web;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Review;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.samples.petclinic.service.BookService;
import org.springframework.samples.petclinic.service.ReadBookService;
import org.springframework.samples.petclinic.service.ReviewService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedISBNException;
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

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ReadBookService readBookService;
	
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

		Boolean isReadBook = this.readBookService.esReadBook(bookId, me.getUsername());
		if(!isReadBook) {
			return "redirect:/oups";
		}
		
		Boolean alreadyReviewed = this.reviewService.alreadyReviewedBook(bookId, me.getUsername());
		if(alreadyReviewed) {
			return "redirect:/oups";
		}
		
		model.put("review", review);
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
		
		Boolean isReadBook = this.readBookService.esReadBook(bookId, me.getUsername());
		if(!isReadBook) {
			return "redirect:/oups";
		}
		
		Boolean alreadyReviewed = this.reviewService.alreadyReviewedBook(bookId, me.getUsername());
		if(alreadyReviewed) {
			return "redirect:/oups";
		}
		
		if (result.hasErrors()) {
			return "reviews/reviewAddForm";
		}
		else {
			this.reviewService.save(review);
			
			return "redirect:/reviews/" + review.getId();
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
			return "redirect:/reviews/" + review.getId();
		} else {
			// multiple books found
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
		
		ModelAndView mav = new ModelAndView("reviews/reviewDetails");
		mav.addObject("review", review);
		mav.addObject("isMine", isMine);
		return mav;
	}
	
	@GetMapping("/books/{bookId}/reviews/{reviewId}/edit")
	public String formEdit(final ModelMap modelMap, @PathVariable("reviewId") final int reviewId, @PathVariable("bookId") final int bookId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		Boolean isMine = this.reviewService.reviewIsMine(reviewId, userDetail.getUsername());
		if(!isMine) {
			return "redirect:/oups";
		}
		
		Review review = this.reviewService.findReviewById(reviewId);
		modelMap.addAttribute("review", review);
		modelMap.addAttribute("bookId", bookId);
		return "reviews/reviewUpdateForm";
	}
	
	@PostMapping("/books/{bookId}/reviews/{reviewId}/edit")
	public String updateReview(@Valid final Review updatedReview, final BindingResult result, final ModelMap modelMap, @PathVariable("reviewId") final int reviewId, @PathVariable("bookId") final int bookId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		Boolean isMine = this.reviewService.reviewIsMine(reviewId, userDetail.getUsername());
		if(!isMine) {
			return "redirect:/oups";
		}
		if(result.hasErrors()) {
			return "reviews/reviewUpdateForm";
		}else {
			Review review = this.reviewService.findReviewById(reviewId);
			updatedReview.setId(review.getId());
			updatedReview.setBook(review.getBook());
			updatedReview.setUser(review.getUser());
			this.reviewService.save(updatedReview);
			return "redirect:/reviews/"+reviewId;
		}
	}
	
	@GetMapping("/books/{bookId}/reviews/{reviewId}/delete")
	public String deleteBook(@PathVariable("bookId") final int bookId, @PathVariable("reviewId") final int reviewId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		Boolean isMine = this.reviewService.reviewIsMine(reviewId, userDetail.getUsername());
		if(!isMine) {
			return "redirect:/oups";
		}
		this.reviewService.deleteReviewById(reviewId);
		return "redirect:/books/" + bookId;
	}
}
