
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
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

	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	fecha;

	@NotEmpty
	private String		body;

	@NotEmpty
	private String		redactor;

	private String		tags;

	@URL
	private String		img;

}
