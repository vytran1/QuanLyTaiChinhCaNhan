package com.quanlychitieu.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.quanlychitieu.common.category.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {
    
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	@Test
	public void firstTest() {
		List<Category> categories = categoryRepository.findAll();
		
		assertThat(categories.size()).isGreaterThan(0);
		
		categories.forEach(category -> {
			System.out.println(category.toString());
		});
	}
}
