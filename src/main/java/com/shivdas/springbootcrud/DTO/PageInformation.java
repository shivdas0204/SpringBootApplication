package com.shivdas.springbootcrud.DTO;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class PageInformation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -310794936839925608L;
	private boolean next;
	private long totalRecords;
	private int offset;
	private int limit;
}
