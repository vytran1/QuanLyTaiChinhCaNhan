package com.quanlychitieu.common.exception;

import java.time.LocalDateTime;

public class ApiError {
    private Integer status;
    private String message;
    private LocalDateTime timeStamp;
    
    
    
	
	public ApiError() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ApiError(Integer status, String message, LocalDateTime timeStamp) {
		super();
		this.status = status;
		this.message = message;
		this.timeStamp = timeStamp;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}
	
    
    
}
