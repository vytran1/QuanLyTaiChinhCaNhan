package com.quanlychitieu.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quanlychitieu.common.exception.UserAlreadyExistException;
import com.quanlychitieu.common.exception.UserNotFoundException;
import com.quanlychitieu.common.user.User;

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
