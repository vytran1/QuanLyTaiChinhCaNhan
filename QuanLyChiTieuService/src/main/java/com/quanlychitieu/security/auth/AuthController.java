package com.quanlychitieu.security.auth;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quanlychitieu.common.exception.ApiError;
import com.quanlychitieu.common.exception.ErrorWhileSavingImageException;
import com.quanlychitieu.common.exception.UserAlreadyExistException;
import com.quanlychitieu.common.user.Currency;
import com.quanlychitieu.common.user.Language;
import com.quanlychitieu.common.user.User;
import com.quanlychitieu.security.CustomUserDetails;
import com.quanlychitieu.user.UserService;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/oauth")
public class AuthController {
    
	private final static String defaultImage = "D:\\quanlychitieu_images\\anomyus.jpg";
	private final static String directoryStoring = "D:\\quanlychitieu_images";

	@Value("${spring.servlet.multipart.max-file-size}")
	private Long maxFileSize;
	
	
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
	
	@PostMapping("/register")
	public ResponseEntity<?> register(
			@RequestPart("user") User user,
			@RequestPart("image") MultipartFile file
			){
		//Lưu Thông Tin Cơ Bản
		user.setCurrency(Currency.vnd);
		user.setLang(Language.vn);
		user.setPhoto("Photo.png");
		try {
			User savedUser = userService.register(user);
			this.saveImage(savedUser, file);
			return ResponseEntity.ok().build();
		} catch (UserAlreadyExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(),e.getMessage(),LocalDateTime.now());
			return ResponseEntity.badRequest().body(error);
		} catch (ErrorWhileSavingImageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage(),LocalDateTime.now());
			return ResponseEntity.internalServerError().body(error);

		}
	}
	
	
	
	
	private void saveImage(User user,MultipartFile file) throws ErrorWhileSavingImageException {
		if(!file.getContentType().startsWith("image/")) {
			throw new IllegalArgumentException("Vui Lòng Gửi File Là Ảnh Cho Chúng tôi");
		}
		
		if(file.getSize() > maxFileSize) {
			throw new IllegalArgumentException("Vui Lòng Gửi File Có Kích Cỡ Dưới 10MB");
		}
		
		//Example: "TranNguyenVy.png"
		String originalFileName = file.getOriginalFilename();
		System.out.println("Original File Name: " + originalFileName);
		
		//Example: ".png"
		String extensionFileName = originalFileName != null && originalFileName.contains(".") ? originalFileName.substring(originalFileName.lastIndexOf(".")) : ".png";
		System.out.println("Extension File Name: " +extensionFileName);
		
		//Example: "1_20112024.png"
		String date = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
		String officialFileName = user.getUserId() + "_" + date + extensionFileName;
		System.out.println("Official File Name: " + officialFileName);
		
		//Example: "D:\\quanlychitieu_images\\1"
		String officialDirectoryStoring = this.directoryStoring + File.separator + user.getUserId();
		System.out.println("DirectoryStoring: " + officialDirectoryStoring);
		
		File directoryStoringCheck = new File(officialDirectoryStoring); 
		
		if(!directoryStoringCheck.exists()) {
			directoryStoringCheck.mkdirs();
		}else {
			 //Clear all exist file
			 for (File oldFile : directoryStoringCheck.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".jpeg") || name.endsWith(".jpg"))) {
	                oldFile.delete();
	         }
		}
		
		//Example: "D:\\quanlychitieu_images\\1\\1_20112024.png"
		String filePath =  directoryStoringCheck + File.separator + officialFileName;
		
		try {
			file.transferTo(new File(filePath));
			user.setPhoto(filePath);
			this.userService.saveUser(user);
		}catch(IOException e){
			e.printStackTrace();
			throw new ErrorWhileSavingImageException(e.getMessage());
		}
	}
	
}
