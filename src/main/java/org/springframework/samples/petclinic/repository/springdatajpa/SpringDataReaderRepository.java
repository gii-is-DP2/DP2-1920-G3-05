/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Reader;
import org.springframework.samples.petclinic.repository.ReaderRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataReaderRepository extends ReaderRepository, CrudRepository<Reader, Integer> {

	@Override
	@Transactional
	@Query("SELECT reader FROM Reader reader WHERE reader.user.username = ?1")
	Reader findReaderByUsername(String username);
	
	@Override
	@Modifying
	@Query("UPDATE Reader SET verified=true WHERE id =:userId")
	void verifyUser(@Param("userId") int userId);
	
	@Override
	@Modifying
	@Query("UPDATE Book SET verified=true WHERE user.username =:userName")
	void verifyBooksByUser(@Param("userName")String username);
}
