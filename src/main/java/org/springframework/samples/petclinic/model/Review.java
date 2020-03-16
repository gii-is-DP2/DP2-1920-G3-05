
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Data;

@Data
@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {

	@Column(name = "raiting")
	@Range(min = 0, max = 5)
	@NotNull
	private Integer	raiting;

	@Column(name = "title")
	@NotEmpty
	private String	title;

	@Column(name = "opinion")
	@NotEmpty
	private String	opinion;

	@ManyToOne(optional = false)
	@JoinColumn(name = "book_id")
	@NotNull
	private Book	book;

	@ManyToOne(optional = false)
	@NotNull
	private User	user;

}
