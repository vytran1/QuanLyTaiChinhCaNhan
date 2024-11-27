package com.quanlychitieu.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quanlychitieu.common.exception.CurrentPasswordMisMatchException;
import com.quanlychitieu.common.exception.UserAlreadyExistException;
import com.quanlychitieu.common.exception.UserNotFoundException;
import com.quanlychitieu.common.user.User;

import net.bytebuddy.utility.RandomString;

@Service
public class UserService {
    
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> loadAll() {
		return userRepository.findAll();
	}
	
	public User saveUser(User user) {
	    return this.userRepository.save(user);	
	}
	
	
	
	public User register(User user) throws UserAlreadyExistException {
		//Kiểm Tra Email Đã Tồn Tại Hay Chưa?
		boolean isEmailExist = isEmailExist(user.getEmail());
		if(isEmailExist) {
			throw new UserAlreadyExistException("Email: " + user.getEmail() + " has existed");
		}
		
		
		encodePassword(user);
        User userSaved = this.saveUser(user);
        return userSaved;
	}
	
	
	public User findById(Integer id) throws UserNotFoundException {
		Optional<User> user = this.userRepository.findById(id);
		if(!user.isPresent()) {
			throw new UserNotFoundException("Not exist user in system with id: " + id);
		}
		return user.get();
	}
	
	
	public User findByEmailForGoogleLogin(String email) {
		Optional<User> user = this.userRepository.findByUsername(email);
		if(!user.isPresent()) {
			return null;
		}else {
			return user.get();
		}
	}
	
	
	public String updateForgotPasswordToken(String email) throws UserNotFoundException {
		Optional<User> user = this.userRepository.findByUsername(email);
		if(!user.isPresent()) {
			throw new UserNotFoundException("Not exist user with email: " + email);
		}
		User userFromDatabase = user.get();
		String resetPasswordToken = RandomString.make(20);
		userFromDatabase.setResetPasswordToken(resetPasswordToken);
		userRepository.save(userFromDatabase);
		return resetPasswordToken;
	}
	
	public void resetPassword(String token,String newPassword) throws UserNotFoundException {
		Optional<User> userOTP = this.userRepository.findByResetPasswordToken(token); 
	    if(!userOTP.isPresent()) {
	    	throw new UserNotFoundException("Token is invalid");
	    }
	    User user = userOTP.get();
	    String encodePassword = passwordEncoder.encode(newPassword);
	    user.setPassword(encodePassword);
	    user.setResetPasswordToken(null);
	    userRepository.save(user);
	}
	
	public void updatePersonalInformation(Integer id,User user) {
		User userFromDatabase = userRepository.findById(id).get();
		userFromDatabase.setFirstName(user.getFirstName());
		userFromDatabase.setLastName(user.getLastName());
		userRepository.save(userFromDatabase);
	}
	
	
	public void changePassword(Integer id,String currentPassword,String newPassword) throws CurrentPasswordMisMatchException {
		User userFromDatabase = userRepository.findById(id).get();
		
		boolean isMatches = passwordEncoder.matches(currentPassword,userFromDatabase.getPassword());
		if(!isMatches) {
			throw new CurrentPasswordMisMatchException("Password from request do not fit with password in the database");
		}
		
		String encodePassword = passwordEncoder.encode(newPassword);
		userFromDatabase.setPassword(encodePassword);
		userRepository.save(userFromDatabase);
	}
	
	/////////////////////////////////
	
	
	public void encodePassword(User user) throws UserAlreadyExistException {
		String rawPassword = user.getPassword();
		String encodePassword = passwordEncoder.encode(rawPassword);
		user.setPassword(encodePassword);
	}
	
	
	public boolean isEmailExist(String email) {
		Optional<User> user = userRepository.findByUsername(email);
		if(user.isPresent()) {
			return true;
		}else {
			return false;
		}
		
	}
}
