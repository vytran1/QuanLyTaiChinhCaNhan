package com.quanlychitieu.common.category;

import com.quanlychitieu.common.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {
   
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Integer categoryId;
	
	
	@Column(name = "category_id_local")
	private Integer categoryIdLocal;
	
	@Column(name = "title")
	private String title;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type",columnDefinition = "enum('SPENDING','EARNING') default 'EARNING'")
	private CategoryType type;
	
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id",referencedColumnName = "userId")
	private User user;
	

	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Category(Integer categoryId, Integer categoryIdLocal, String title, CategoryType type) {
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

	public CategoryType getType() {
		return type;
	}

	public void setType(CategoryType type) {
		this.type = type;
	}
	
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", categoryIdLocal=" + categoryIdLocal + ", title=" + title
				+ ", type=" + type + ", user=" + user + "]";
	}

	
	
	
	
}
