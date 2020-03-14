
package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.User;

public interface UserRepository {

	User findByUsername(String username) throws DataAccessException;

	void save(User user);
}
