package com.quanlychitieu.transact;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quanlychitieu.common.Wallet;
import com.quanlychitieu.common.category.Category;
import com.quanlychitieu.common.category.CategoryType;
import com.quanlychitieu.common.exception.ExceedWalletBalanceException;
import com.quanlychitieu.common.exception.TransactionNotFoundException;
import com.quanlychitieu.common.transact.AutoTran;
import com.quanlychitieu.common.transact.Transact;
import com.quanlychitieu.wallet.WalletRepository;



@Service
public class TransactService {
    
	
	@Autowired
    private TransactRepository transactRepository;
	
	@Autowired
	private WalletRepository walletRepository;
	
 
	
	
	public List<Transact> findAllTransactionByWallet(Wallet wallet){
		return transactRepository.findAllTransactionBySpecificWallet(wallet.getWalletId());
	}
	
	
	public List<Transact> findAllTrasactionByCategory(Category category){
		return transactRepository.findAllTransactionBySpecificCategory(category.getCategoryId());
	}
	
	public List<Transact> findAllTransactionByWalletAndDateRange(Wallet wallet,Date start,Date end){
		return transactRepository.findAllTrasactionByWalletAndDateRange(wallet.getWalletId(), start, end);
	}
	
	
	@org.springframework.transaction.annotation.Transactional(rollbackFor = Throwable.class)
	public void createTransaction(Transact transact) throws ExceedWalletBalanceException {
		//Check Amount CASE SPENDING
		if(transact.getCategory().getType().equals(CategoryType.SPENDING)) {
			Long amountFromUser = transact.getTranAmount();
			Long amountFromChoosingWallet = transact.getWallet().getWalletAmount();
			
			Long newAmountInChoosingWallet = amountFromChoosingWallet - amountFromUser;
			
			if(newAmountInChoosingWallet < 0) {
				throw new ExceedWalletBalanceException("Chi tiêu vượt quá số tiền hiện có trong ví");
			}
			
			transact.getWallet().setWalletAmount(newAmountInChoosingWallet);
		}else if(transact.getCategory().getType().equals(CategoryType.EARNING)) {
			Long amountFromUser = transact.getTranAmount();
			Long amountFromChoosingWallet = transact.getWallet().getWalletAmount();
			
			Long newAmountInChoosingWallet = amountFromChoosingWallet + amountFromUser;
			
			transact.getWallet().setWalletAmount(newAmountInChoosingWallet);
			
		}
		
		walletRepository.save(transact.getWallet());
		transactRepository.save(transact);
		
		
	}
	
	
	public void updateTransaction(Transact tranFromDatabase, TransactDTO tranDTO ) {
		tranFromDatabase.setTranTitle(tranDTO.getTranTitle());
		tranFromDatabase.setTranDescription(tranDTO.getTranDescription());
		tranFromDatabase.setAutoTran(AutoTran.valueOf(tranDTO.getAutoTran()));
		transactRepository.save(tranFromDatabase);
	}
	
	
	public Transact findTransactionByIdAndSpecificWallet(Integer transactId,Wallet wallet) throws TransactionNotFoundException {
		Optional<Transact> transactOTP = transactRepository.findByTransactIdAndSpecificWallet(transactId,wallet.getWalletId());
		if(!transactOTP.isPresent()) {
			throw new TransactionNotFoundException("Not Exist Transaction with id: " + transactId + " and wallet with id: " + wallet.getWalletId());
		}
		return transactOTP.get();
	}
	
	
	public void createManyTransaction(List<Transact> transacts) {
		transactRepository.saveAll(transacts);
	}
	
	public void deleteManyTransaction(List<Transact> transacts) {
		transactRepository.deleteAll(transacts);
	}
	
	
	
}  
