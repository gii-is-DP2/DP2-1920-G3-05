
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.springdatajpa.SpringDataReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

	@Autowired
	private SpringDataReviewRepository reviewRepo;


	@Transactional
	public int reviewCount() {
		return (int) this.reviewRepo.count();
	}

}
