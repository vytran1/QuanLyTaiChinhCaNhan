package com.quanlychitieu.wallet;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.quanlychitieu.common.Wallet;

public interface WalletRepository extends JpaRepository<Wallet,Integer> {
   
	
	@Query("SELECT w FROM Wallet w WHERE w.user.userId = ?1")
	public List<Wallet> findByUserId(Integer userId);
	
	
	@Query("SELECT w FROM Wallet w WHERE w.walletId = ?1 AND w.user.userId = ?2")
	public Optional<Wallet> findByUserIdAndWalletId(Integer walletId,Integer userId);
}
