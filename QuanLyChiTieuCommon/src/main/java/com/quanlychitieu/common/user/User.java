package com.quanlychitieu.common.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "photo")
	private String photo;

	@Enumerated(EnumType.STRING)
	@Column(name = "currency", columnDefinition = "enum('vnd','usd') default 'vnd'")
	private Currency currency = Currency.vnd;

	@Enumerated(EnumType.STRING)
	@Column(name = "lang", columnDefinition = "enum('vn','us') default 'vn'")
	private Language lang = Language.vn;
	
	@Column(name = "reset_password_token")
	private String resetPasswordToken;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

    

	public User(Integer userId) {
		super();
		this.userId = userId;
	}



	public Integer getUserId() {
		return userId;
	}



	public void setUserId(Integer userId) {
		this.userId = userId;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPhoto() {
		return photo;
	}



	public void setPhoto(String photo) {
		this.photo = photo;
	}



	public Currency getCurrency() {
		return currency;
	}



	public void setCurrency(Currency currency) {
		this.currency = currency;
	}



	public Language getLang() {
		return lang;
	}



	public void setLang(Language lang) {
		this.lang = lang;
	}

    

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}



	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}



	@Override
	public String toString() {
		return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", password="
				+ password + ", email=" + email + ", photo=" + photo + ", currency=" + currency.toString() + ", lang=" + lang.toString()
				+ "]";
	}
	
	
	@Transient
	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}
	

}
