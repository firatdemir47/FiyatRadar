package com.firatdemir.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleAllException(Exception ex) {
		logger.error("Unexpected error: ", ex); // Hata kaydını tutuyoruz
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"An unexpected error occurred", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Kaynak bulunamadı hatası
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
				"Detailed message about the resource.");
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	// Veri bütünlüğü ihlali hatası
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Data Integrity Violation",
				ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	// Geçersiz veya hatalı barkod verisi
	@ExceptionHandler(InvalidBarcodeException.class)
	public ResponseEntity<ErrorResponse> handleInvalidBarcodeException(InvalidBarcodeException ex) {
		// Hata kaydını daha açıklayıcı yaptık
		logger.error("Barkod doğrulama hatası: {}", ex.getMessage());

		// ErrorResponse oluşturuyoruz
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(),
				"Barkod geçerlilik kontrolünde hata oluştu.");

		// HTTP 400 (Bad Request) ile hata mesajını döndürüyoruz
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// Validation hatası (parametre doğrulama hatası)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		StringBuilder errorMessages = new StringBuilder();
		BindingResult result = ex.getBindingResult();
		result.getAllErrors().forEach(error -> {
			errorMessages.append(error.getDefaultMessage()).append(", ");
		});
		errorMessages.setLength(errorMessages.length() - 2); 
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation error",
				errorMessages.toString());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	// Diğer hata türlerine özel bir yanıt
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(),
				"Detailed bad request error message.");
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}
