package org.springframework.samples.petclinic.repository;

import java.util.List;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.transaction.annotation.Transactional;



public interface AuthoritiesRepository extends  CrudRepository<Authorities, String>{
    
    @Query("SELECT a FROM Authorities a WHERE a.username = ?1")
    @Transactional
    List<Authorities> findAuthoritiesByUsername(String username) ;
}
