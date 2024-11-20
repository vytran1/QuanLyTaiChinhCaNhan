package com.quanlychitieu.security.auth;

public class ResetPasswordDTO {
    private String newPassword;

	public ResetPasswordDTO(String newPassword) {
		super();
		this.newPassword = newPassword;
	}

	public ResetPasswordDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
    
    
}
