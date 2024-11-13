package com.quanlychitieu.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import com.quanlychitieu.common.user.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
    
	@Autowired
	private UserRepository userRepository;
	
	
	@Test
	public void testLoadAll() {
		List<User> results = userRepository.findAll();
		assertThat(results.size()).isGreaterThan(0);
		results.forEach(user -> {
			System.out.println(user.toString());
		});
	}
	
	@Test
	public void findByEmail() {
		String email = "vy.tn171003@gmail.com";
		Optional<User> user = userRepository.findByUsername(email);
		assertThat(user).isPresent();
		System.out.println(user);
	}
	
	@Test
	public void updatePassword() {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Optional<User> user = userRepository.findByUsername("vy.tn171003@gmail.com");
		User result = user.get();
		String rawPassowrd = result.getPassword();
		String encodedPassword = passwordEncoder.encode(rawPassowrd);
		result.setPassword(encodedPassword);
		User saveduser = userRepository.save(result);
		assertThat(saveduser.getPassword()).isEqualTo(encodedPassword);
	}
}
