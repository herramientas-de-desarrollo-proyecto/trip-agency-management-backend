package com.tripagencymanagement.template.general.entities.domainEntities;

import org.springframework.http.HttpStatus;

public class GeneralException extends RuntimeException {

    private String detail;
    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public GeneralException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneralException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public GeneralException(String message, HttpStatus status, Throwable cause, String detail) {
        super(message, cause);
        this.detail = detail;
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
