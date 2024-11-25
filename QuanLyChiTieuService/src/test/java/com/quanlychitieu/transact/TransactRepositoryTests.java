package com.quanlychitieu.transact;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.quanlychitieu.common.transact.Transact;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TransactRepositoryTests {
   
	
	@Autowired
	private TransactRepository transactRepository;
	
	
	@Test
	public void firstTest() {
		List<Transact> transacts = transactRepository.findAll();
		assertThat(transacts.size()).isGreaterThan(0);
		transacts.forEach(transact -> {
			System.out.println(transact);
		});
	}
}
