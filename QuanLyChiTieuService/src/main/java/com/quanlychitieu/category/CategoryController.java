package com.quanlychitieu.category;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quanlychitieu.ErrorDTO;
import com.quanlychitieu.Utility;
import com.quanlychitieu.common.category.Category;
import com.quanlychitieu.common.exception.CategoryNotFoundException;
import com.quanlychitieu.common.user.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {
   
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	//Get List Category Created By Specific User
	@GetMapping("")
	public ResponseEntity<?> getAllCategoryBelongToSpecificUser(){
		Integer userId = Utility.getIdOfCurrentLoginUser();
		List<Category> categories = categoryService.findAllByUserId(userId);
		if(categories.size() > 0) {
			List<CategoryDTO> categoryDTOs = categories.stream().map(this::convertEntityToDTO).toList();
			return ResponseEntity.status(HttpStatus.OK).body(categoryDTOs);
		}else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}
	
	
	//
	@GetMapping("/{categoryId}")
	public ResponseEntity<?> getSingleCategory(@PathVariable("categoryId") Integer categoryId){
		try {
			Integer userId = Utility.getIdOfCurrentLoginUser();
			Category category = categoryService.findByUserIdAndCategoryId(userId, categoryId);
			CategoryDTO categoryDTO = this.convertEntityToDTO(category);
			return ResponseEntity.ok(categoryDTO);
		} catch (CategoryNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO error = new ErrorDTO();
			error.setStatus(HttpStatus.NOT_FOUND.value());
			error.setTimestamp(new Date());
			error.addError(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
		}

	}
	
	
	@PostMapping("")
	public ResponseEntity<?> createSingleCategory(@RequestBody CategoryDTO categoryDTO){
		Integer userId = Utility.getIdOfCurrentLoginUser();
		Category category = modelMapper.map(categoryDTO,Category.class);
		category.setUser(new User(userId));
		System.out.println(category);
		categoryService.saveSingleCategory(category);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/{categoryId}")
	public ResponseEntity<?> updateSingleCategory(@PathVariable("categoryId") Integer categoryId,@RequestBody @Valid CategoryDTO dto){
		Category category = this.convertDTOToEntity(dto);
		category.setCategoryId(categoryId);
		
		Integer userId = Utility.getIdOfCurrentLoginUser();
		category.setUser(new User(userId));
		//System.out.println(category);
		try {
			categoryService.updateSingleCategory(category);
			return ResponseEntity.ok().build();
		} catch (CategoryNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO error = new ErrorDTO();
			error.setStatus(HttpStatus.NOT_FOUND.value());
			error.setTimestamp(new Date());
			error.addError(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);			
		}
		
	}
	
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<?> deleteSingleCategory(@PathVariable("categoryId") Integer categoryId){
        try {
        	Integer userId = Utility.getIdOfCurrentLoginUser();
			categoryService.deleteSingleCategory(userId,categoryId);
			return ResponseEntity.ok().build();
		} catch (CategoryNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO error = new ErrorDTO();
			error.setStatus(HttpStatus.NOT_FOUND.value());
			error.setTimestamp(new Date());
			error.addError(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);	
		}
	}
	
	
	@PostMapping("/createAll")
    public ResponseEntity<?> createManyCategories(@RequestBody @Valid List<CategoryDTO> categoryDTOs){
		if(categoryDTOs.isEmpty()) {
			ErrorDTO dto = new ErrorDTO();
			dto.setStatus(HttpStatus.BAD_REQUEST.value());
			dto.setTimestamp(new Date());
			dto.addError("Danh sách category phải có ít nhất 1 phần tử");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
		}
    	Integer userId = Utility.getIdOfCurrentLoginUser();
    	List<Category> categories = categoryDTOs.stream().map(this::convertDTOToEntity).toList();
    	for(Category category : categories) {
    		category.setUser(new User(userId));
    	}
    	categoryService.saveManyCategories(categories);
    	return ResponseEntity.ok().build();
    }
	
	
	
	@PostMapping("/updateAll")
	public ResponseEntity<?> updateManyCategories(@RequestBody @Valid List<CategoryDTO> categoryDTOs){
		if(categoryDTOs.isEmpty()) {
			ErrorDTO dto = new ErrorDTO();
			dto.setStatus(HttpStatus.BAD_REQUEST.value());
			dto.setTimestamp(new Date());
			dto.addError("Danh sách category phải có ít nhất 1 phần tử");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
		}
		Integer userId = Utility.getIdOfCurrentLoginUser();
		List<Category> categories = categoryDTOs.stream().map(this::convertDTOToEntity).toList();
		for(Category category : categories) {
			category.setUser(new User(userId));
		}
		categoryService.saveManyCategories(categories);
		return ResponseEntity.ok().build();
	}
	
	
	
	@DeleteMapping("/deleteAll")
	public ResponseEntity<?> deleteManyCategories(@RequestBody List<CategoryDTO> categoryDTOs){
		if(categoryDTOs.isEmpty()) {
			ErrorDTO dto = new ErrorDTO();
			dto.setStatus(HttpStatus.BAD_REQUEST.value());
			dto.setTimestamp(new Date());
			dto.addError("Danh sách category phải có ít nhất 1 phần tử");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
		}
		Integer userId = Utility.getIdOfCurrentLoginUser();
		List<Category> categories = categoryDTOs.stream().map(this::convertDTOToEntity).toList();
    	for(Category category : categories) {
    		category.setUser(new User(userId));
    	}
    	categoryService.deleteManyCategories(categories);
    	return ResponseEntity.ok().build();
	}
	
	public CategoryDTO convertEntityToDTO(Category category) {
		return modelMapper.map(category,CategoryDTO.class); 
	}
	
	public Category convertDTOToEntity(CategoryDTO categoryDTO) {
		return modelMapper.map(categoryDTO,Category.class);
	}
	
}
