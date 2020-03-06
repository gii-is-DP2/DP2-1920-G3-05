
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "news")
public class New extends NamedEntity {

	@NotEmpty
	private String		head;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	fecha;

	@NotEmpty
	private String		body;

	@NotNull
	private String		redactor;

	private String		tags;

	@URL
	private String		img;

	@ManyToMany
	@JoinTable(name = "news_books", joinColumns = @JoinColumn(name = "new_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
	private Set<Book>	book;

}
