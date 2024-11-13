package com.quanlychitieu.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quanlychitieu.common.user.User;
import com.quanlychitieu.security.jwt.JwtUtility;

@Service
public class TokenService {
	
   @Value("${app.security.jwt.refresh-token.expiration}")	
   private int refreshTokenExpiration;
   
   @Autowired
   private JwtUtility jwtUtility;
   
   @Autowired
   private PasswordEncoder passwordEncoder;
   
   public AuthResponse generateToken(User user) {
	   String accessToken = jwtUtility.generateAccessToken(user);
	   AuthResponse response = new AuthResponse();
	   response.setAccessToken(accessToken);
	   response.setRefreshToken("");
	   return response;
   }
}
