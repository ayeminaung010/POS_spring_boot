package com.example.demo.dto;

public class ResponseHelper {
    private	boolean status;
    private String message;
    private Object data;
    
	public boolean isStatus() {
		return status;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
    
}