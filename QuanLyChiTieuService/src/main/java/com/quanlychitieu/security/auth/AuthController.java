package com.quanlychitieu.security.auth;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.quanlychitieu.SendMailUtility;

import com.quanlychitieu.common.exception.ApiError;
import com.quanlychitieu.common.exception.ErrorWhileSavingImageException;
import com.quanlychitieu.common.exception.UserAlreadyExistException;
import com.quanlychitieu.common.exception.UserNotFoundException;
import com.quanlychitieu.common.user.AuthenticationType;
import com.quanlychitieu.common.user.Currency;
import com.quanlychitieu.common.user.Language;
import com.quanlychitieu.common.user.User;
import com.quanlychitieu.security.CustomUserDetails;
import com.quanlychitieu.user.UserRepository;
import com.quanlychitieu.user.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/oauth")
public class AuthController {
    
	//private final static String defaultImage = "D:\\quanlychitieu_images\\anomyus.jpg";
	
	@Value("${spring.security.oauth2.client.registration.google.client-id}") 
	private String googleClientId; 
	
	@Value("${spring.security.oauth2.client.registration.google.client-secret}") 
	private String googleClientSecret;
	
	
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	
	
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	UserService userService;
	
	
	
	
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
	
	
	@PostMapping("/google")
	public ResponseEntity<?> loginGoogle(@RequestBody Map<String,String> request){
		String idToken = request.get("idToken");
		
		try {
			
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
					.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
					.setAudience(Collections.singletonList(googleClientId))
					.build(); 
			
			GoogleIdToken googleIdToken;
			googleIdToken = verifier.verify(idToken);
			GoogleIdToken.Payload payLoadData = googleIdToken.getPayload();
			String email = payLoadData.getEmail();
			User user = userService.findByEmailForGoogleLogin(email);
			if(user == null) {
				user = new User();
				user.setEmail(email);
				user.setFirstName((String) payLoadData.get("given_name"));
				user.setLastName((String) payLoadData.get("family_name"));
				user.setPhoto("Photo.png");
				user.setAuthenticationType(AuthenticationType.valueOf("GOOGLE"));
			}
			user.setAuthenticationType(AuthenticationType.GOOGLE);
			User savedUser = userService.saveUser(user);
			AuthResponse authResponse = tokenService.generateToken(savedUser);
			return ResponseEntity.ok(authResponse);
		}catch(GeneralSecurityException | IOException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user){
		//Lưu Thông Tin Cơ Bản
		user.setCurrency(Currency.vnd);
		user.setLang(Language.vn);
		user.setPhoto("Photo.png");
		try {
			User savedUser = userService.register(user);
			return ResponseEntity.ok().build();
		} catch (UserAlreadyExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(),e.getMessage(),LocalDateTime.now());
			return ResponseEntity.badRequest().body(error);
		} 
	}
	
	
	@PostMapping("/forgotPassword/{email}")
	public ResponseEntity<?> requestForgotPassword(@PathVariable("email") String email){
		try {
			String token =  userService.updateForgotPasswordToken(email);
			sendMail(token, email);
			return ResponseEntity.ok().build();
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
	
	
	@PostMapping("/resetPassword/{token}")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO request,@PathVariable("token") String token){
		try {
			userService.resetPassword(token,request.getNewPassword());
			return ResponseEntity.ok().build();
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}
	
	
	
	
	
	
	
	public void sendMail(String token,String email) throws MessagingException, UnsupportedEncodingException {
		JavaMailSenderImpl mailSender = SendMailUtility.preJavaMailSenderImpl();
		
		String toAddress = email;
		String subject = "Here Is Your Reset Password Token!.";
		String content = "<p>Dear User,</p>" 
                + "<p>You have requested to reset your password.</p>" 
                + "<p>Below is your reset password token:</p>"
                + "<br>"
                + "<h1 style='color: blue;'>" + token + "</h1>"
                + "<br>"
                + "<p>If you did not make this request, please ignore this email.</p>"
                + "<p>If you have any questions, feel free to contact us at support@example.com.</p>"
                + "<br>"
                + "<p>Best regards,</p>"
                + "<p>Your Support Team</p>";
		
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message);
		
		messageHelper.setFrom(SendMailUtility.MAILFROM,SendMailUtility.MAILSENDERNAME);
		messageHelper.setTo(toAddress);
		messageHelper.setSubject(subject);
		
		messageHelper.setText(content,true);
		mailSender.send(message);
	}
	
	
}
