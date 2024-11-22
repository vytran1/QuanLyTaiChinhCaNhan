package com.quanlychitieu.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.quanlychitieu.common.category.Category;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
   
	
	
	@Query("SELECT c FROM Category c WHERE c.user.userId = ?1")
	public List<Category> findByUserId(Integer userId);
	
	@Query("SELECT c FROM Category c WHERE c.user.userId = ?1 AND c.categoryId = ?2")
	public Optional<Category> findByUserIdAndCategoryId(Integer userId,Integer categoryId);
}
