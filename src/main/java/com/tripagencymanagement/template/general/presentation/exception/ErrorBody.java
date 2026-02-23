package com.tripagencymanagement.template.general.presentation.exception;

import java.util.Optional;

import org.springframework.http.HttpStatus;

public record ErrorBody(String message, HttpStatus status, Optional<String> detail, Optional<String> callstack) {
}
