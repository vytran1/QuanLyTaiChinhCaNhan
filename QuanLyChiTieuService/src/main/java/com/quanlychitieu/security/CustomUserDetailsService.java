package com.quanlychitieu.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.quanlychitieu.common.user.User;
import com.quanlychitieu.user.UserRepository;
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> findByUserName = userRepository.findByUsername(email);
		if(!findByUserName.isPresent()) {
			throw new UsernameNotFoundException("No user found with the given email");
		}
		return new CustomUserDetails(findByUserName.get());
	}

}
