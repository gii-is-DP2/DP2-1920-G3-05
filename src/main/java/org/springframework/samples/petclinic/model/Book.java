/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table(name = "books")
@Data
public class Book extends BaseEntity {

	@Column(name = "title")
	@NotEmpty
	private String		title;

	@Column(name = "author")
	@NotEmpty
	private String		author;

	@Column(name = "editorial")
	@NotEmpty
	private String		editorial;

	@ManyToOne
	@JoinColumn(name = "genre_id")
	private Genre		genre;

	@Column(name = "ISBN", unique = true)
	@Digits(fraction = 0, integer = 13)
	@NotEmpty
	private String		ISBN;

	@Column(name = "pages")
	@NotNull
	private Integer		pages;

	@Column(name = "synopsis")
	@NotEmpty
	private String		synopsis;

	@Column(name = "publication_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	publicationDate;

	@Column(name = "verified")
	@NotNull
	private Boolean		verified;

	@Column(name = "image")
	@URL
	private String		image;

}
