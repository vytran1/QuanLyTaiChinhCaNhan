package com.quanlychitieu;

import org.springframework.security.core.context.SecurityContextHolder;

import com.quanlychitieu.common.user.User;
import com.quanlychitieu.security.CustomUserDetails;

public class Utility {
  
	
	public static Integer getIdOfCurrentLoginUser() {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userDetails.getUser();
		return user.getUserId();
	}
	
	
	
}
