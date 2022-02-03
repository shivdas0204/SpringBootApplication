package com.shivdas.springbootcrud.DTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserVO {

	private Integer id;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String phoneNumber;
}
