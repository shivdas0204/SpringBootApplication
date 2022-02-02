package com.shivdas.springbootcrud.exception;

import org.springframework.http.HttpStatus;

import com.shivdas.springbootcrud.DTO.Message;

public class EmptyDataException extends ResourceException {

	private static final long serialVersionUID = 1L;
	private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

	public EmptyDataException(final Message... messages) {
		super(messages);
	}

	@Override
	protected HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
