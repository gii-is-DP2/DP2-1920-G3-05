
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	@NotBlank(message = "Must not be empty")
	String	username;

	@NotBlank(message = "Must not be empty")
	String	password;

	boolean	enabled;
}
