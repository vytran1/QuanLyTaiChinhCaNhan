package com.quanlychitieu.user;

public class PersonalInformationDTO {
	private String firstName;
	private String lastName;
	private String email;
	private String photo;

	public PersonalInformationDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PersonalInformationDTO(String firstName, String lastName, String email, String photo) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.photo = photo;
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
   
}
