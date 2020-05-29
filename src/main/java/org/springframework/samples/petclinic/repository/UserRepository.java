
package org.springframework.samples.petclinic.repository;


import org.springframework.samples.petclinic.model.User;

public interface UserRepository {

	User findByUsername(String username) ;

	User save(User user);
	
	Integer numberUser() ;

}
