
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	@NotEmpty(message = "Must not be empty")
	String	username;

	@NotEmpty(message = "Must not be empty")
	String	password;

	boolean	enabled;
}
