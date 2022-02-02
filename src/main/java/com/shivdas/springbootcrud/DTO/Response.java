package com.shivdas.springbootcrud.DTO;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Response<T> extends Resource<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4588027841988660597L;
	private Collection<String> errors;
	private Set<Message> meta;
	private PageInformation page;
	private String errorMessage;
	private Integer errorCode;
	
	private Object adminNotificationSNSRequest;
	
	@Builder.Default
	private Boolean canSubmit =  null;
}