package com.quanlychitieu.category;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quanlychitieu.common.category.Category;
import com.quanlychitieu.common.exception.CategoryNotFoundException;

@Service
public class CategoryService {
   
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	
	
	public List<Category> findAllByUserId(Integer userId){
		return categoryRepository.findByUserId(userId);
	}
	
	
	public void saveSingleCategory(Category category) {
		this.categoryRepository.save(category);
	}
	
	public Category findById(Integer categoryId) throws CategoryNotFoundException {
		Optional<Category> categoryOTP = categoryRepository.findById(categoryId);
		if(!categoryOTP.isPresent()) {
			throw new CategoryNotFoundException("Not exist category with id: " + categoryId);
		}
		return categoryOTP.get();
	}
	
	public Category findByUserIdAndCategoryId(Integer userId,Integer categoryId) throws CategoryNotFoundException {
		Optional<Category> categoryOTP = categoryRepository.findByUserIdAndCategoryId(userId, categoryId);
		if(!categoryOTP.isPresent()) {
			throw new CategoryNotFoundException("Not exist category with id: " + categoryId);
		}
		return categoryOTP.get();
	}
	
	public void updateSingleCategory(Category categoryInRequest) throws CategoryNotFoundException {
		
		
		Category categoryInDatabase = findByUserIdAndCategoryId(categoryInRequest.getUser().getUserId(),categoryInRequest.getCategoryId());
		
		if(categoryInRequest.getCategoryIdLocal() != null) {
			categoryInDatabase.setCategoryIdLocal(categoryInRequest.getCategoryIdLocal());
		}
		
		categoryInDatabase.setTitle(categoryInRequest.getTitle());
		categoryInDatabase.setType(categoryInRequest.getType());
		
		
		categoryRepository.save(categoryInDatabase);
	}
	
	public void deleteSingleCategory(Integer userId,Integer categoryId) throws CategoryNotFoundException {
		Category categoryInDatabase = findByUserIdAndCategoryId(userId, categoryId);
		categoryRepository.delete(categoryInDatabase);
	}
	
	
	
	
	
	public void saveManyCategories(List<Category> categories) {
		categoryRepository.saveAll(categories);
	}
	
	public void deleteManyCategories(List<Category> categories) {
		categoryRepository.deleteAll(categories);
	}
	
}
