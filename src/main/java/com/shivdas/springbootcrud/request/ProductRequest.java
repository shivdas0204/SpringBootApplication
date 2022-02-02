package com.shivdas.springbootcrud.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ProductRequest {

	@NotBlank(message = "name should not be null")
	private String name;
	
	private String description;
}
