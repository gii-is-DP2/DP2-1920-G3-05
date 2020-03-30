package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.URL;

import lombok.Data;

@Entity
@Data
@Table(name = "images")
public class Image extends BaseEntity{
	
	@Column(name="link")
	@NotEmpty
	@URL
	private String link;
	
	@ManyToOne()
	@JoinColumn(name = "publication_id")
	private Publication publication;
}
