package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "wished_book")
public class WishedBook extends BaseEntity{
	@ManyToOne(optional = false)
	private Book	book;

	@ManyToOne(optional = false)
	private User	user;
}
