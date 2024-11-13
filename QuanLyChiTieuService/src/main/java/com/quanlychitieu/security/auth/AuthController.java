package com.quanlychitieu.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quanlychitieu.common.user.User;
import com.quanlychitieu.security.CustomUserDetails;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quanlychitieu/oauth")
public class AuthController {
     
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	TokenService tokenService;
	
	@PostMapping("/token")
	public ResponseEntity<?> getAccessToken(@RequestBody @Valid AuthRequest request){
		String username = request.getUsername();
		String password = request.getPassword();
		try {
			
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
			CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
			User user = customUserDetails.getUser();
			AuthResponse authResponse = tokenService.generateToken(user);
			return ResponseEntity.ok(authResponse);
		}catch(BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}
}
