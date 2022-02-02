package com.shivdas.springbootcrud.DTO;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class Resource<T> implements Serializable {

	private static final long serialVersionUID = -1061877486470676126L;
	private T data;
	
}