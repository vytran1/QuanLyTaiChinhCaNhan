package com.quanlychitieu.transact;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.quanlychitieu.common.transact.Transact;

public interface TransactRepository extends JpaRepository<Transact,Integer> {
   
	
	
	@Query("SELECT ts FROM Transact ts WHERE ts.wallet.walletId = ?1")
	public List<Transact> findAllTransactionBySpecificWallet(Integer walletId);
	
	
	@Query("SELECT ts FROM Transact ts WHERE ts.category.categoryId = ?1")
	public List<Transact> findAllTransactionBySpecificCategory(Integer categoryId);
	
	@Query("SELECT ts FROM Transact ts WHERE ts.category.user.userId = ?1 AND ts.category.type = ?2")
	public List<Transact> findAllTransactionByUserIdAndCategoryType(Integer userId,String categoryType);
	
	
	@Query("SELECT ts FROM Transact ts WHERE ts.wallet.walletId = ?1 AND ts.date BETWEEN ?2 AND ?3")
	public List<Transact> findAllTrasactionByWalletAndDateRange(Integer walletId,Date start,Date end);
	
	
	@Query("SELECT ts FROM Transact ts WHERE ts.transactId = ?1 AND ts.wallet.walletId = ?2")
	public Optional<Transact> findByTransactIdAndSpecificWallet(Integer transactId, Integer walletId);
}
