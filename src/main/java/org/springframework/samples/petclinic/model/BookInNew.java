package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "book_in_new")
public class BookInNew extends BaseEntity{

	@ManyToOne(optional = false)
	private Book book;
	
	@ManyToOne(optional = false)
	private New neew; //new esta reservado para los constructores
}
