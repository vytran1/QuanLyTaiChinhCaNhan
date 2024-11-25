package com.quanlychitieu.user;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quanlychitieu.ErrorDTO;
import com.quanlychitieu.Utility;

import com.quanlychitieu.common.exception.CurrentPasswordMisMatchException;
import com.quanlychitieu.common.exception.ErrorWhileSavingImageException;
import com.quanlychitieu.common.exception.UserNotFoundException;
import com.quanlychitieu.common.user.User;

@RestController
@RequestMapping("/user")
public class UserController {
    
	private final static String directoryStoring = "D:\\quanlychitieu_images\\user_image";
	
	@Value("${spring.servlet.multipart.max-file-size}")
	private Long maxFileSize;
	
	@Autowired
	private UserService userService;

//	@GetMapping("")
//	public ResponseEntity<?> loadAllUser() {
//		List<User> users = userService.loadAll();
//		if (users.size() > 0) {
//            System.out.println(users);
//			return ResponseEntity.ok(users);
//		} else {
//			return ResponseEntity.noContent().build();
//		}
//	}
	
	//Lấy Thông Tin Cá Nhân
	@GetMapping("/personal_information")
	public ResponseEntity<?> getPersonalInfo(){
		Integer currentId = Utility.getIdOfCurrentLoginUser();
		try {
			User user = userService.findById(currentId);
            PersonalInformationDTO result = convertUserToPersonalInfoDTO(user);
            return ResponseEntity.ok(result);
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO error = new ErrorDTO();
			error.setStatus(HttpStatus.NOT_FOUND.value());
			error.setTimestamp(new Date());
			error.addError(e.getMessage());
			return new ResponseEntity(error, HttpStatus.NOT_FOUND);
		}
	}
	
	//Cập Nhật Thông Tin Cá Nhân
	@PostMapping("")
	public ResponseEntity<?> updatePersonalInfo(@RequestBody User userFromRequest){
		Integer currentLoginUserId = Utility.getIdOfCurrentLoginUser();
		userService.updatePersonalInformation(currentLoginUserId,userFromRequest);
		return ResponseEntity.ok().build();
	}
	
	//Đổi Mật Khẩu
	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request){
		Integer currentLoginUserId = Utility.getIdOfCurrentLoginUser();
		try {
			userService.changePassword(currentLoginUserId,request.getCurrentPassword(),request.getNewPassword());
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (CurrentPasswordMisMatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO errorDTO = new ErrorDTO();
			errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
			errorDTO.setTimestamp(new Date());
			errorDTO.addError(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
		}
		
	}
	
	
	
	@PostMapping(value = "/updateImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadProfileImage(@RequestParam("photo") MultipartFile photo){
		Integer currentUserLoginId = Utility.getIdOfCurrentLoginUser();
		try {
			User user = userService.findById(currentUserLoginId);
			saveImage(user, photo);
			return ResponseEntity.ok().build();
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		} catch (ErrorWhileSavingImageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
	
	
	
	public PersonalInformationDTO convertUserToPersonalInfoDTO(User user) {
		PersonalInformationDTO dto = new PersonalInformationDTO();
		dto.setEmail(user.getEmail());
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setPhoto(user.getPhoto());
		return dto;
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
