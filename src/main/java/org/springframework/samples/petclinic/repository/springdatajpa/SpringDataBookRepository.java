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

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.Genre;
import org.springframework.samples.petclinic.repository.BookRepository;

public interface SpringDataBookRepository extends BookRepository, Repository<Book, Integer> {

	@Override
	@Query("SELECT book FROM Book book WHERE UPPER(book.title) LIKE %:title% OR UPPER(book.author) LIKE %:title% OR UPPER(book.genre.name) LIKE %:title% OR UPPER(book.ISBN) LIKE %:title%")
	Collection<Book> findBookByTitleAuthorGenreISBN(@Param("title") String title);

	@Override
	@Query("SELECT book FROM Book book WHERE book.id =:id")
	Book findById(@Param("id") int id);

	@Override
	@Query("SELECT bgenre FROM Genre bgenre ORDER BY bgenre.name")
	List<Genre> findBookGenres() throws DataAccessException;

	@Override
	@Query("SELECT genre FROM Genre genre WHERE genre.name =:name")
	Genre findGenreByName(final String name);
}
