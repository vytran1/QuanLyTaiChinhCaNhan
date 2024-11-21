package com.quanlychitieu.wallet;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.quanlychitieu.common.Wallet;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class WalletRepositoryTests {
   
	@Autowired
	private WalletRepository walletRepository;
	
	
	@Test
	public void firstTest() {
		List<Wallet> listWallets = walletRepository.findAll();
		assertThat(listWallets.size()).isGreaterThan(0);
		listWallets.forEach(wallet -> {
			System.out.println(wallet.toString());
		});
	}
}
