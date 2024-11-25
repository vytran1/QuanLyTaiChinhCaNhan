package com.quanlychitieu;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

import com.quanlychitieu.common.user.User;
import com.quanlychitieu.security.CustomUserDetails;

public class Utility {
  
	
	public static Integer getIdOfCurrentLoginUser() {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userDetails.getUser();
		return user.getUserId();
	}
	
	
	
	public static ErrorDTO createErrorObject(Integer httpStatus,String message) {
		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setStatus(httpStatus);
		errorDTO.setTimestamp(new Date());
		errorDTO.addError(message);
		return errorDTO;
	}
	
}
