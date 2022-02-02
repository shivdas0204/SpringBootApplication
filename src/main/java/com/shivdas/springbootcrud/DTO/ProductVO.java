package com.shivdas.springbootcrud.DTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductVO {

	private Integer id;
	
	private String name;
	
	private String description;
}
