package com.shivdas.springbootcrud.exception;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;

import com.shivdas.springbootcrud.DTO.Message;

public abstract class ResourceException  extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private Set<String> messages;
	private Integer code;
	private String errorMessage;

	public ResourceException(final Message... messages) {
		this.messages = new HashSet<>();

		Stream.of(messages).collect(Collectors.toSet()).forEach(msg -> {
			this.messages.add(msg.getDetail());
			this.code = msg.getCode();
			this.errorMessage = msg.getErrorMessage();
		});
	}

	public Set<String> getMessages() {
		return this.messages;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	protected abstract HttpStatus getHttpStatus();

}

