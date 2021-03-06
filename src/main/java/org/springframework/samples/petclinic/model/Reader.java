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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.core.style.ToStringCreator;

import lombok.Data;

/**
 * Simple JavaBean domain object representing an owner.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
@Entity
@Data
@Table(name = "readers")
public class Reader extends Person {

	@Column(name = "address")
	@NotBlank(message = "Must not be empty")
	private String	address;

	@Column(name = "city")
	@NotBlank(message = "Must not be empty")
	private String	city;

	@Column(name = "telephone")
	@NotBlank(message = "Must not be empty")
	@Digits(fraction = 0, integer = 10, message = "Must be an integer from 1 to 10")
	private String	telephone;

	@Column(name = "verified")
	@NotNull
	private Boolean	verified	= false;

	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "username", referencedColumnName = "username")
	private User	user;


	@Override
	public String toString() {
		return new ToStringCreator(this)

			.append("id", this.getId()).append("new", this.isNew()).append("lastName", this.getLastName()).append("firstName", this.getFirstName()).append("address", this.address).append("city", this.city).append("telephone", this.telephone).toString();
	}

}
