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

package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.samples.petclinic.model.Reader;
import org.springframework.samples.petclinic.repository.ReaderRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedUsernameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class ReaderService {

	private ReaderRepository	readerRepository;

	@Autowired
	private UserService			userService;

	@Autowired
	private AuthoritiesService	authoritiesService;


	@Autowired
	public ReaderService(final ReaderRepository readerRepository) {
		this.readerRepository = readerRepository;
	}

	@Transactional
	public Reader findReaderByUsername(final String username) {
		return this.readerRepository.findReaderByUsername(username);
	}

	@Transactional
	public Iterable<Reader> findReaders() {
		return this.readerRepository.findAll();
	}
	
	@Transactional
	public void verifyUser(int userId) {
		this.readerRepository.verifyUser(userId);
		Reader reader= this.readerRepository.findById(userId);
		this.readerRepository.verifyBooksByUser(reader.getUser().getUsername());
	}
	@Transactional
	public void saveReader(final Reader reader) throws DuplicatedUsernameException {
		Reader readeer = this.findReaderByUsername(reader.getUser().getUsername());
		if (readeer == null) {
			//creando reader
			this.readerRepository.save(reader);
			//creando user
			this.userService.saveUser(reader.getUser());
			//creando authorities
			this.authoritiesService.saveAuthorities(reader.getUser().getUsername(), "reader");
		} else if (readeer.getId() == reader.getId()) {
			//actualizando reader
			this.readerRepository.save(reader);
			//actualizando user
			this.userService.saveUser(reader.getUser());

		} else {
			throw new DuplicatedUsernameException();
		}
	}

	public Reader findById(int userId) {
		return this.readerRepository.findById(userId);
	}

}
