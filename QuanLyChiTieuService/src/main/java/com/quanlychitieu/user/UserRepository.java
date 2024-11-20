package com.quanlychitieu.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.quanlychitieu.common.user.User;

public interface UserRepository extends JpaRepository<User,Integer> {
  
	
	@Query("SELECT u FROM User u WHERE u.email = ?1 ")
	public Optional<User> findByUsername(String email);
	
	
	@Query("SELECT u FROM User u WHERE u.resetPasswordToken = ?1")
	public Optional<User> findByResetPasswordToken(String token);
}
