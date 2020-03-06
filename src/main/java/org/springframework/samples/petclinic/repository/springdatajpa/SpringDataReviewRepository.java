
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Review;

public interface SpringDataReviewRepository extends CrudRepository<Review, Integer> {

}
