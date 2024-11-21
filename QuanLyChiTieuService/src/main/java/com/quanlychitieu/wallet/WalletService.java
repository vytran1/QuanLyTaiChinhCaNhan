package com.quanlychitieu.wallet;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quanlychitieu.common.Wallet;
import com.quanlychitieu.common.exception.WalletNotFoundException;

@Service
public class WalletService {
   
	@Autowired
	private WalletRepository walletRepository;
	
	
	
	public List<Wallet> listByUserID(Integer userId){
		List<Wallet> wallets = walletRepository.findByUserId(userId);
		return wallets;
	}
	
	
	public void createWallet(Wallet wallet) {
		this.walletRepository.save(wallet);
	}
	
	public void updateWallet(Wallet walletInDatabase,WalletDTO walletInRequest) {
		walletInDatabase.setWalletTitle(walletInRequest.getWalletTitle());
		walletInDatabase.setWalletDescription(walletInRequest.getWalletDescription());
		walletInDatabase.setWalletAmount(walletInRequest.getWalletAmount());
		this.walletRepository.save(walletInDatabase);
	}
	
	public void updateWalletAmount(Wallet walletInDatabase,Long amount,String type) {
		if(type.equals("EXPENSE")) {
			walletInDatabase.setWalletAmount(walletInDatabase.getWalletAmount() - amount);
		}else if(type.equals("INCOME")) {
			walletInDatabase.setWalletAmount(walletInDatabase.getWalletAmount() + amount);
		}
		
		walletRepository.save(walletInDatabase);
		
	}
	
	
	public void deleteWallet(Integer walletId,Integer userId) throws WalletNotFoundException {
		Wallet wallet = this.findByUserIdAndWalletId(walletId, userId);
		walletRepository.delete(wallet);
	}
	
	
	public Wallet findByUserIdAndWalletId(Integer walletId,Integer userId) throws WalletNotFoundException{
		Optional<Wallet> walletOTP = walletRepository.findByUserIdAndWalletId(walletId, userId);
		if(walletOTP.isPresent()) {
			return walletOTP.get();
		}else {
			throw new WalletNotFoundException("Not exist wallet with id: " + walletId + " and User Id: " + userId);
		}
	}
}
