package com.tripagencymanagement.template.general.presentation.exception;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.tripagencymanagement.template.general.entities.domainEntities.GeneralException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ErrorBody> handleGeneralException(GeneralException ex) {
        ErrorBody errorBody = new ErrorBody(ex.getMessage(), ex.getStatus(), Optional.ofNullable(ex.getDetail()),
                Optional.ofNullable(ex.getCause()).map(Throwable::toString));
        return ResponseEntity.status(ex.getStatus()).body(errorBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorBody> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder("<ul>");
        for (org.springframework.validation.FieldError fe : ex.getBindingResult().getFieldErrors()) {
            sb.append("<li>").append(fe.getField()).append(": ").append(fe.getDefaultMessage()).append("</li>");
        }
        sb.append("</ul>");
        ErrorBody errorBody = new ErrorBody("Validación fallida", HttpStatus.BAD_REQUEST, Optional.of(sb.toString()),
                Optional.ofNullable(ex.getCause()).map(Throwable::toString));
        return ResponseEntity.badRequest().body(errorBody);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorBody> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Class<?> requiredType = ex.getRequiredType();
        String typeName = requiredType != null ? requiredType.getSimpleName() : "desconocido";
        String detail = String.format("El parámetro '%s' con valor '%s' no es del tipo esperado '%s'.",
                ex.getName(), ex.getValue(), typeName);
        ErrorBody errorBody = new ErrorBody("Tipo de argumento inválido", HttpStatus.BAD_REQUEST, Optional.of(detail),
                Optional.ofNullable(ex.getCause()).map(Throwable::toString));
        return ResponseEntity.badRequest().body(errorBody);
    }

    // @ExceptionHandler(IllegalArgumentException.class)
    // public ResponseEntity<ErrorBody>
    // handleIllegalArgumentException(IllegalArgumentException ex) {
    // ErrorBody errorBody = new ErrorBody(ex.getMessage(), HttpStatus.BAD_REQUEST,
    // Optional.empty(),
    // Optional.ofNullable(ex.getCause()).map(Throwable::toString));
    // return ResponseEntity.badRequest().body(errorBody);
    // }
}
