package com.quanlychitieu.category;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;

public class CategoryDTO {
    private Integer categoryId;
    private Integer categoryIdLocal;
    
    @Length(min = 3, max = 50, message = "Title length must range from 10 to 50 characters")
    @NotNull(message = "Category Title is required")
    private String  title;
    
    @NotNull(message = "Category Type is required")
    private String  type;
    
    
	public CategoryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CategoryDTO(Integer categoryId, Integer categoryIdLocal, String title, String type) {
		super();
		this.categoryId = categoryId;
		this.categoryIdLocal = categoryIdLocal;
		this.title = title;
		this.type = type;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public Integer getCategoryIdLocal() {
		return categoryIdLocal;
	}
	public void setCategoryIdLocal(Integer categoryIdLocal) {
		this.categoryIdLocal = categoryIdLocal;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
    
    
}
