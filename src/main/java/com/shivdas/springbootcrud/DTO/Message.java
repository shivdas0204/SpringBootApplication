package com.shivdas.springbootcrud.DTO;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2543339597896059505L;
	private String detail;
	private Integer code;
	private String errorMessage;
	 
}
