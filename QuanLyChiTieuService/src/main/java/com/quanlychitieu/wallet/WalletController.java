package com.quanlychitieu.wallet;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.quanlychitieu.ErrorDTO;
import com.quanlychitieu.Utility;
import com.quanlychitieu.common.Wallet;
import com.quanlychitieu.common.exception.WalletNotFoundException;
import com.quanlychitieu.common.user.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/wallet")
public class WalletController {
   
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	//Get All Wallet Belong to One Specific User
	@GetMapping("")
	public ResponseEntity<?> getAllWalletBelongingToSpecificUser(){
		Integer userId = Utility.getIdOfCurrentLoginUser();
		List<Wallet> wallets = walletService.listByUserID(userId);
		if(wallets.size() > 0) {
			List<WalletDTO> walletDTOs = wallets.stream().map(this::convertEntityToDTO).toList();
			return ResponseEntity.status(HttpStatus.OK).body(walletDTOs);
		}else {
		    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();	
		}
	}
	
	
	//Create Wallet
	@PostMapping("")
	public ResponseEntity<?> createWallet(@RequestBody @Valid WalletDTO walletDTO){
       Integer userId = Utility.getIdOfCurrentLoginUser();
       Wallet newWallet = convertDTOToEntity(walletDTO);
       newWallet.setUser(new User(userId));
       walletService.createWallet(newWallet);
       return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	
	//Update All Information 
	@PostMapping("/{walletId}")
	public ResponseEntity<?> updateWallet(@PathVariable("walletId") Integer walletId, @RequestBody @Valid WalletDTO walletDTO ){
		try {
			Integer userId = Utility.getIdOfCurrentLoginUser();
			Wallet walletFromDatabase = walletService.findByUserIdAndWalletId(walletId, userId);
			
			
			walletService.updateWallet(walletFromDatabase,walletDTO);
			return ResponseEntity.ok().build();
		} catch (WalletNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO errorDTO = new ErrorDTO();
			errorDTO.setStatus(HttpStatus.NOT_FOUND.value());
			errorDTO.setTimestamp(new Date());
			errorDTO.addError(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
		}
	}
	
	
	@PostMapping("/{walletId}/update_amount")
	public ResponseEntity<?> updateOnlyAmount(
			@PathVariable("walletId") Integer walletId,  
			@RequestParam(value = "amount",required = true) Long amount,  
			@RequestParam(value = "type", required = true) String type
			){
		// Exception: MethodArgumentTypeMismatchException
		try {
			Integer userId = Utility.getIdOfCurrentLoginUser();
			Wallet walletFromDatabase = walletService.findByUserIdAndWalletId(walletId, userId);
			
			
			walletService.updateWalletAmount(walletFromDatabase,amount,type);
			return ResponseEntity.ok().build();
		} catch (WalletNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// TODO Auto-generated catch block
			e.printStackTrace();
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO errorDTO = new ErrorDTO();
			errorDTO.setStatus(HttpStatus.NOT_FOUND.value());
			errorDTO.setTimestamp(new Date());
			errorDTO.addError(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
		}
	}
	
	
	
	//Delete Wallet
	@DeleteMapping("/{walletId}")
    public ResponseEntity<?> deleteWallet(@PathVariable("walletId") Integer walletId){
		Integer userId = Utility.getIdOfCurrentLoginUser();
		try {
			walletService.deleteWallet(walletId,userId);
			return ResponseEntity.ok().build();
		} catch (WalletNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO errorDTO = new ErrorDTO();
			errorDTO.setStatus(HttpStatus.NOT_FOUND.value());
			errorDTO.setTimestamp(new Date());
			errorDTO.addError(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
		}
	}
	
	
	
	
	public WalletDTO convertEntityToDTO(Wallet entity) {
		return modelMapper.map(entity,WalletDTO.class);
	}
	
	public Wallet convertDTOToEntity(WalletDTO dto) {
		return modelMapper.map(dto,Wallet.class);
	}
}
