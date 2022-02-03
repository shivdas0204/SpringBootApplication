package com.shivdas.springbootcrud.request;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserReuqest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@NotBlank
	@Size(max = 50, message = "FirstName max length 50")
	private String firstName;
	
	@NotNull
	@NotBlank
	@Size(max = 50, message = "LastName max length 50")
	private String lastName;
	
	@NotNull
	@NotBlank
	@Email
	private String email;
	
	private String phoneNumber;
	
	private String birthDate;
}
