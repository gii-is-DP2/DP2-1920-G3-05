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
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.core.style.ToStringCreator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "books")
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

	@Column(name = "genre")
	@NotEmpty
	private String		genre;

	@Column(name = "ISBN")
	@NotNull
	private Integer		ISBN;

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


	public String getTitle() {
		return this.title;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	public String getGenre() {
		return this.genre;
	}

	public void setGenre(final String genre) {
		this.genre = genre;
	}

	public Integer getISBN() {
		return this.ISBN;
	}

	public void setISBN(final Integer ISBN) {
		this.ISBN = ISBN;
	}

	public Integer getPages() {
		return this.pages;
	}

	public void setPages(final Integer pages) {
		this.pages = pages;
	}

	public LocalDate getPublicationDate() {
		return this.publicationDate;
	}

	public void setPublicationDate(final LocalDate publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getSynopsis() {
		return this.synopsis;
	}

	public void setSynopsis(final String synopsis) {
		this.synopsis = synopsis;
	}

	public String getEditorial() {
		return this.editorial;
	}

	public void setEditoria(final String editorial) {
		this.editorial = editorial;
	}

	public Boolean getVerified() {
		return this.verified;
	}

	public void setVerified(final Boolean verified) {
		this.verified = verified;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)

			.append("id", this.getId()).append("new", this.isNew()).append("title", this.title).append("author", this.author).append("genre", this.genre).append("ISBN", this.ISBN).toString();
	}

}
