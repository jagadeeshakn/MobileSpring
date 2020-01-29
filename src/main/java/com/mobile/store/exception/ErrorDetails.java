package com.mobile.store.exception;

import java.util.Date;

import lombok.Data;

@Data
public class ErrorDetails {

	private Date timestamp;
    private String message;
    private String details;
    
    public ErrorDetails(Date date, String message, String details) {
    	super();
        this.timestamp = date;
        this.message = message;
        this.details = details;
	}
}
