
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Book;
import org.springframework.samples.petclinic.model.New;
import org.springframework.samples.petclinic.repository.NewRepository;
import org.springframework.samples.petclinic.service.exceptions.CantDeleteBookInNewException;
import org.springframework.samples.petclinic.service.exceptions.CantShowNewReviewException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewService {

	@Autowired
	private NewRepository		newRepo;

	@Autowired
	private BookInNewService	bookInNewService;

	@Autowired
	private BookService			bookService;
	


	@Transactional(readOnly = true)
	public List<Integer> getNewsFromBook(final int bookId)  {
		return this.newRepo.getNewsFromBook(bookId);
	}

	@Transactional
	@Modifying
	public void deleteNew(final int newId, final int bookId)  {
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
	public Boolean existsNewById(final int newId)  {
		return this.newRepo.existsById(newId);
	}

	@Transactional(readOnly = true)
	public Collection<New> getAllNews()  {
		return this.newRepo.getAllNews();
	}
	
	@Transactional(readOnly = true)
	public Collection<New> getNewsBookReview(final String userId) throws CantShowNewReviewException {
		Boolean CanShowNews = this.canShowNewsBookReview(userId); 
		if(Boolean.TRUE.equals(CanShowNews)) {
			return this.newRepo.getNewsBookReview(userId);
		}else {
			throw new CantShowNewReviewException();
		}
		
	}
	@Transactional(readOnly = true)
	public Collection<New> getNewsBookReview2(final String userId){
		return this.newRepo.getNewsBookReview(userId);
	}
	
	@Transactional(readOnly = true)
	public Collection<Book> getBooksFromNews(final int newId) {
		return this.bookInNewService.getBooksInNew(newId);
	}

	@Transactional
	@Modifying
	public void deleteById(final int newId) {
		List<Integer> bookInNewIds = this.bookInNewService.getBooksInNewFromNew(newId); //Lista de los id de los libros que hay en la noticia
		for (int i = 0; i < bookInNewIds.size(); i++) {
			this.bookInNewService.deleteBookInNew(newId, bookInNewIds.get(i));
		}
		this.newRepo.deleteNew(newId);
	}

	@Transactional
	public New getNewById(final int newId) {
		return this.newRepo.findById(newId);

	}

	@Transactional
	@Modifying
	public void save(final New neew) {
		this.newRepo.save(neew);

	}

	@Transactional
	@Modifying
	public void saveBookInNew(final int neewId, final int bookId) {
		if (this.bookInNewService.getByNewIdBookId(neewId, bookId) == null) {
			this.bookInNewService.save(this.getNewById(neewId), this.bookService.findBookById(bookId));
		}

	}

	@Transactional
	@Modifying
	public void deleteBookInNew(final int newId, final int bookId) throws CantDeleteBookInNewException {
		Collection<Book> booksIncludes = this.getBooksFromNews(newId);
		if (booksIncludes.size() > 1) {
			this.bookInNewService.deleteBookInNew(newId, bookId);
		} else {
			throw new CantDeleteBookInNewException();
		}
	}

	public Boolean canShowNewsBookReview (String username) {
		Boolean canShow = this.newRepo.getNewsBookReview(username).isEmpty();
		return !Boolean.TRUE.equals(canShow);
	}
}
