package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Meeting;

public interface MeetingRepository extends CrudRepository<Meeting, Integer>{

}
