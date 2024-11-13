package com.quanlychitieu.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quanlychitieu.common.user.User;

@Service
public class UserService {
    
	@Autowired
	private UserRepository userRepository;
	
	
	public List<User> loadAll() {
		return userRepository.findAll();
	}
}
