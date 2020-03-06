package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "meetings")
public class Meeting extends NamedEntity{

	@Column(name = "place")
	@NotBlank
	private String place;
	
	@Column(name = "start")
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm")
	@NotNull
	private LocalDateTime start;
	
	@Column(name = "end")
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm")
	@NotNull
	private LocalDateTime end;
	
	@Column(name = "capacity")
	@NotNull
	private Integer capacity;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "book_id")
	@NotNull
	private Book book;
	
	
}
