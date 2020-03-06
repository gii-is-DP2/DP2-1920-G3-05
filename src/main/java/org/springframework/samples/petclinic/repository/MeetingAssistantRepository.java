package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.MeetingAssistant;

public interface MeetingAssistantRepository extends CrudRepository<MeetingAssistant, Integer> {

}
