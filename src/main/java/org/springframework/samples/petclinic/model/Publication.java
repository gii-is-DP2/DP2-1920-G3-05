package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
@Table(name = "publications")
public class Publication extends BaseEntity {
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	@Column(name="title")
	@NotEmpty
	private String title;

	@Column(name = "description")
	@NotEmpty
	private String description;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "publication")
	private Set<Image> images;

	@Column(name = "publication_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate publicationDate;

}
