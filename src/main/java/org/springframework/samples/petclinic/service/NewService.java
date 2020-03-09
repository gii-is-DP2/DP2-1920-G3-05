
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.repository.BookInNewRepository;
import org.springframework.samples.petclinic.repository.NewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewService {

	@Autowired
	private NewRepository newRepo;

	@Autowired
	private BookInNewService bookInNewService;

	/*
	 * @Transactional public int newCount() { return (int) this.newRepo.count(); }
	 */

	@Transactional(readOnly = true)
	public List<Integer> getNewsFromBook(int bookId) throws DataAccessException {
		return this.newRepo.getNewsFromBook(bookId);
	}

	@Transactional
	@Modifying
	public void deleteNew(int newId, int bookId) throws DataAccessException {
		// Casuistica: (1)Si al borrar el libro no quedan más libros en la noticia se
		// borra el bookInNew y la New
		// (2) Si hay mas de un libro en la New solo se borra el bookInNew
		List<Integer> bookInNewIds = this.bookInNewService.getBooksInNewFromNew(newId); //Lista de los id de los libros que hay en la noticia
		if (bookInNewIds != null && !bookInNewIds.isEmpty()) {
			if (bookInNewIds.size() == 1) {
				// borro el libro de la noticia
				this.bookInNewService.deleteBookInNew(newId, bookId);
				// borro la noticia
				this.newRepo.deleteNew(newId);
			} else {
				// borro el libro de la noticia
				this.bookInNewService.deleteBookInNew(newId, bookId);
			}
		}
	}
	
	@Transactional(readOnly = true)
	public Boolean existsNewById(int newId) throws DataAccessException {
		return this.newRepo.existsById(newId);
	}
}
