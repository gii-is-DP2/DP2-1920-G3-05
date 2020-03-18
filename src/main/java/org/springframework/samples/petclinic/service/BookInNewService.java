
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.BookInNew;
import org.springframework.samples.petclinic.model.New;
import org.springframework.samples.petclinic.repository.BookInNewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookInNewService {

	@Autowired
	private BookInNewRepository bookInNewRepository;


	@Transactional(readOnly = true)
	public List<Integer> getBooksInNewFromNew(final int newId) throws DataAccessException {
		return this.bookInNewRepository.getBooksInNewFromNew(newId);
	}

	@Transactional
	@Modifying
	public void deleteBookInNew(final int newId, final int bookId) throws DataAccessException {
		this.bookInNewRepository.deleteBookInNew(newId, bookId);
	}

	public Collection<Book> getBooksInNew(final int newId) {
		return this.bookInNewRepository.getBooksInNew(newId);
	}

	public void save(final New neew, final Book book) {
		BookInNew bn = new BookInNew();
		bn.setBook(book);
		bn.setNeew(neew);
		this.bookInNewRepository.save(bn);

	}

	public BookInNew getByNewIdBookId(final int newId, final int bookId) {
		return this.bookInNewRepository.getByNewIdBookId(newId, bookId);
	}

}
