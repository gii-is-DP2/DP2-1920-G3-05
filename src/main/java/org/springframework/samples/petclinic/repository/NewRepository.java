
package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.New;

public interface NewRepository extends CrudRepository<New, Integer> {
}