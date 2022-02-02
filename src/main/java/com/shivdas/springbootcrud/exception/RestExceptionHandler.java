package com.shivdas.springbootcrud.exception;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletionException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.exc.InputCoercionException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.shivdas.springbootcrud.DTO.Response;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	public static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

	@ExceptionHandler(ResourceException.class)
	public ResponseEntity<Object> customHandleNotFound(final Exception exception, final WebRequest request) {
		return createResponse(exception);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> customHandleGlobalException(final Exception exception, final WebRequest request) {
		return createResponse(exception);
	}

	private ResponseEntity<Object> createResponse(Exception exception) {

		// logger.error(ExceptionUtils.getStackTrace(exception));
		final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		Locale locale = Locale.US;

		final String requestLocale = attr.getRequest().getHeader("locale");

		if (requestLocale != null && !requestLocale.isEmpty()) {
			locale = new Locale(requestLocale);
		}

		final Response<Object> errorResponse = new Response<>();
		if (exception instanceof CompletionException) {
			exception = (Exception) exception.getCause();
		}

		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		if (exception instanceof CompletionException) {
			exception = (Exception) exception.getCause();
		}

		if (exception instanceof ResourceNotFoundException) {

			errorResponse.setErrors(((ResourceNotFoundException) exception).getMessages());
			httpStatus = ((ResourceException) exception).getHttpStatus();
			errorResponse.setErrorMessage("Missing items or data");

		} else if (exception instanceof EmptyDataException) {

			errorResponse.setErrors(((EmptyDataException) exception).getMessages());
			httpStatus = ((ResourceException) exception).getHttpStatus();
			errorResponse.setErrorMessage("Missing or Invalid fields");

		} else if (exception instanceof OperationNotAllowedException) {

			errorResponse.setErrors(((OperationNotAllowedException) exception).getMessages());
			httpStatus = ((ResourceException) exception).getHttpStatus();
			errorResponse.setErrorMessage("Operation is not allowed.");

		} else if (exception instanceof BadRequestException) {

			errorResponse.setErrors(((BadRequestException) exception).getMessages());
			httpStatus = ((ResourceException) exception).getHttpStatus();
			errorResponse.setErrorMessage("Missing or Invalid fields");

		} else if (exception instanceof AccessDeniedException) {
			errorResponse.setErrors(Arrays.asList("You do not have permission to access this."));
			httpStatus = HttpStatus.FORBIDDEN;
			errorResponse.setErrorMessage("You do not have permission for this operation.");
		} else if (exception instanceof MaxUploadSizeExceededException) {
			errorResponse.setErrors(Arrays.asList("Maximum file size allowed is 750 MB."));
			httpStatus = HttpStatus.CONFLICT;
			errorResponse.setErrorMessage("Maximum upload size exceeded");
		} else if (exception instanceof MediaNotSupportedException) {

			errorResponse.setErrors(((MediaNotSupportedException) exception).getMessages());
			httpStatus = ((ResourceException) exception).getHttpStatus();
			errorResponse.setErrorMessage("Attached media not supported");
		} else {
			Set<String> errors = new HashSet<String>();
			errors.add(exception.getMessage());

			errorResponse.setErrors(errors);
			errorResponse.setErrorMessage("Oops, something went wrong. Try again");
		}

		return ResponseEntity.status(httpStatus).body(errorResponse);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Set<String> errors = new HashSet<String>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			Object[] placeHolderValues = { error.getField() };
			errors.add(error.getDefaultMessage());
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getDefaultMessage());
		}

		final Response<Object> errorResponse = new Response<>();
		errorResponse.setErrors(errors);
		errorResponse.setErrorMessage("Missing items or data");
		return ResponseEntity.badRequest().body(errorResponse);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Object[] placeHolderValues = { ex.getParameterName() };
		String error = "Parameter is missing";
		Set<String> errors = new HashSet<String>();
		errors.add(error);

		final Response<Object> errorResponse = new Response<>();
		errorResponse.setErrors(errors);
		errorResponse.setErrorMessage("Missing items or data");
		return ResponseEntity.status(status).body(errorResponse);
	}

	@ExceptionHandler(value = { CompletionException.class })
	public ResponseEntity<Object> handleResourceNotFoundException(CompletionException ex) {

		Throwable cause = ex.getCause();

		if (cause instanceof ConstraintViolationException) {

			return handleConstraintViolation((ConstraintViolationException) cause);

		} else {

			return createResponse(ex);
		}

	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
		Set<String> errors = new HashSet<String>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {

			Object[] placeHolderValues = { violation.getInvalidValue() };
			errors.add("value is not valid for the input field");
		}

		final Response<Object> errorResponse = new Response<>();
		errorResponse.setErrors(errors);
		errorResponse.setErrorMessage("Missing items or data");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {

		Set<String> errors = new HashSet<String>();
		errors.add("The parameter"+ex.getName()+"of value "+ex.getValue()+" could not be converted to type"+ex.getRequiredType().getSimpleName());

		final Response<Object> errorResponse = new Response<>();
		errorResponse.setErrors(errors);
		errorResponse.setErrorMessage("Missing items or data");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(value = { RequestRejectedException.class})
//	public ResponseEntity<Object> handleMaxPageLimitException(final Throwable e, final HttpServletRequest request) {
//		logger.warn("Bad request {} {}: {}", request.getMethod(), request.getRequestURL(), e.getMessage());
//		
//		List<String> errors = new ArrayList<String>();
//		errors.add(e.getMessage());
//		final Response<Object> errorResponse = new Response<>();
//		errorResponse.setErrors(errors);
//		errorResponse.setErrorMessage("Missing items or data");
//	
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Throwable mostSpecificCause = ex.getMostSpecificCause();
		Set<String> errors = new HashSet<String>();
		if (mostSpecificCause != null && mostSpecificCause instanceof InvalidFormatException) {
			InvalidFormatException formatException = ((InvalidFormatException) mostSpecificCause);
			Object value = formatException.getValue();
			Object key ="";
			if(!CollectionUtils.isEmpty( formatException.getPath())) {
				if(formatException.getPath().size()>=2) {
					key = formatException.getPath().get(2).getFieldName();
				}else {
					key = formatException.getPath().get(0).getFieldName();
				}
			}
			//CollectionUtils.isEmpty( formatException.getPath()) ? "" : formatException.getPath().get(0).getFieldName();
	        LOGGER.info("Error with key {}",key);
	        Object[] placeHolderValues = {!StringUtils.isEmpty(key)?key:value};
	        errors.add(!StringUtils.isEmpty(key)?(String) key:value+"is invalid.");
		} else if (mostSpecificCause != null && mostSpecificCause instanceof BadRequestException) {

			((BadRequestException) mostSpecificCause).getMessages().forEach(message ->{
				errors.add(message);
			});
			

		} else if (mostSpecificCause != null && mostSpecificCause instanceof MismatchedInputException) {
			List<Reference> path = ((MismatchedInputException) mostSpecificCause).getPath();
			Object value = "";
			for (Reference reference : path) {
				value += reference.getFieldName();
			}
			Object[] placeHolderValues = { value };
			errors.add(value+"is invalid");
		} else if (mostSpecificCause != null && mostSpecificCause instanceof JsonParseException) {
			errors.add(((JsonParseException) mostSpecificCause).getOriginalMessage());
		} else if (mostSpecificCause != null && mostSpecificCause instanceof InputCoercionException) {
			
			if (ex.getCause() instanceof JsonMappingException) {

				JsonMappingException formatException = ((JsonMappingException) ex.getCause());
				if(!CollectionUtils.isEmpty( formatException.getPath())) {
					Object[] placeHolderValues = { formatException.getPath().get(0).getFieldName() };
					errors.add(formatException.getPath().get(0).getFieldName()+"is invalid");
				}else {
					errors.add("Something went wrong. Please try again later.");
				}
			} else {
				errors.add("Something went wrong. Please try again later.");
						
			}
		}
		else {
			errors.add("Something went wrong. Please try again later.");
		}

		logger.error(ex.getMessage());
		final Response<Object> errorResponse = new Response<>();
		errorResponse.setErrors(errors);
		errorResponse.setErrorMessage("Missing items or data");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
}
