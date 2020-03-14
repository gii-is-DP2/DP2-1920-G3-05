
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "read_book")
public class ReadBook extends BaseEntity {

	@ManyToOne(optional = false)
	private Book	book;

	@ManyToOne(optional = false)
	private User	user;
}
