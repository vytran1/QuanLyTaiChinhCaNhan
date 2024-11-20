package com.quanlychitieu.user;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quanlychitieu.ErrorDTO;
import com.quanlychitieu.Utility;
import com.quanlychitieu.common.exception.ApiError;
import com.quanlychitieu.common.exception.UserNotFoundException;
import com.quanlychitieu.common.user.User;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("")
	public ResponseEntity<?> loadAllUser() {
		List<User> users = userService.loadAll();
		if (users.size() > 0) {
            System.out.println(users);
			return ResponseEntity.ok(users);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/personal_information")
	public ResponseEntity<?> getPersonalInfo(){
		Integer currentId = Utility.getIdOfCurrentLoginUser();
		try {
			User user = userService.findById(currentId);
            PersonalInformationDTO result = convertUserToPersonalInfoDTO(user);
            return ResponseEntity.ok(result);
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO error = new ErrorDTO();
			error.setStatus(HttpStatus.NOT_FOUND.value());
			error.setTimestamp(new Date());
			error.addError(e.getMessage());
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
	   
	}
	
	public PersonalInformationDTO convertUserToPersonalInfoDTO(User user) {
		PersonalInformationDTO dto = new PersonalInformationDTO();
		dto.setEmail(user.getEmail());
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setPhoto(user.getPhoto());
		return dto;
	}
}
