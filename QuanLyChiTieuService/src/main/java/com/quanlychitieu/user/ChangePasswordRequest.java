package com.quanlychitieu.user;

public class ChangePasswordRequest {
	private String currentPassword;
	private String newPassword;

	public ChangePasswordRequest(String currentPassword, String newPassword) {
		super();
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
	}

	public ChangePasswordRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
