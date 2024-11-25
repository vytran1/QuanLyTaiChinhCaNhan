package com.quanlychitieu.transact;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.quanlychitieu.ErrorDTO;
import com.quanlychitieu.Utility;
import com.quanlychitieu.category.CategoryService;
import com.quanlychitieu.common.Wallet;
import com.quanlychitieu.common.category.Category;
import com.quanlychitieu.common.exception.CategoryNotFoundException;
import com.quanlychitieu.common.exception.ExceedWalletBalanceException;
import com.quanlychitieu.common.exception.TransactionNotFoundException;
import com.quanlychitieu.common.exception.WalletNotFoundException;
import com.quanlychitieu.common.transact.Transact;
import com.quanlychitieu.wallet.WalletService;

@RestController
@RequestMapping("/transact")
public class TransactController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private TransactService transactService;

	@Autowired
	private WalletService walletService;

	@Autowired
	private CategoryService categoryService;

	// Get All Transactions Followed Wallet
	@GetMapping("/byWallet/{walletId}")
	public ResponseEntity<?> findAllTransactionByWallet(@PathVariable("walletId") Integer walletId) {
		Integer userId = Utility.getIdOfCurrentLoginUser();
		try {
			Wallet wallet = walletService.findByUserIdAndWalletId(walletId, userId);
			List<Transact> transacts = transactService.findAllTransactionByWallet(wallet);
			if (transacts.size() > 0) {
				List<TransactDTO> transactDTOs = transacts.stream().map(this::convertEntityToDTO).toList();
				return ResponseEntity.ok(transactDTOs);
			} else {
				return ResponseEntity.noContent().build();
			}
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

	// Get All Transactions Followed Category
	@GetMapping("/byCategory/{categoryId}")
	public ResponseEntity<?> findAllTransactionByCategory(@PathVariable("categoryId") Integer categoryId) {
		Integer userId = Utility.getIdOfCurrentLoginUser();
		try {
			Category category = this.categoryService.findByUserIdAndCategoryId(userId, categoryId);
			List<Transact> transacts = transactService.findAllTrasactionByCategory(category);
			if (transacts.size() > 0) {
				List<TransactDTO> transactDTOs = transacts.stream().map(this::convertEntityToDTO).toList();
				return ResponseEntity.ok(transactDTOs);
			} else {
				return ResponseEntity.noContent().build();
			}
		} catch (CategoryNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO errorDTO = new ErrorDTO();
			errorDTO.setStatus(HttpStatus.NOT_FOUND.value());
			errorDTO.setTimestamp(new Date());
			errorDTO.addError(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
		}
	}
	
	
	//Get All Transaction following a specific wallet and with a particular date range
	@GetMapping("/byWalletAndDate/{walletId}")
    public ResponseEntity<?> findAllTransactionByWalletAndDate(
    		@PathVariable("walletId") Integer walletId,
    		@RequestParam("startDate") String startDate,
    		@RequestParam("endDate") String endDate    		
    		)
	{
		try {
			Integer userId = Utility.getIdOfCurrentLoginUser();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date start = simpleDateFormat.parse(startDate);
			Date end =  simpleDateFormat.parse(endDate);
			
			Wallet wallet = walletService.findByUserIdAndWalletId(walletId, userId);
			List<Transact> transacts = transactService.findAllTransactionByWalletAndDateRange(wallet, start, end);
			if(transacts.size() > 0) {
				List<TransactDTO> transactDTOs = transacts.stream().map(this::convertEntityToDTO).toList();
				return ResponseEntity.ok(transactDTOs);				
			}else {
			    return ResponseEntity.noContent().build(); 	
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO errorDTO = new ErrorDTO();
			errorDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorDTO.setTimestamp(new Date());
			errorDTO.addError(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
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
	
	
	//Create Transaction
	@PostMapping("")
	public ResponseEntity<?> createTransaction(@RequestBody TransactDTO transactDTO){
	    
		try {
			//Map basic information
			Transact transact = convertDTOToEntity(transactDTO);
			
			//Map Wallet
			Integer userId = Utility.getIdOfCurrentLoginUser();
			Integer walletId = transactDTO.getWallet().getWalletId();
			Wallet wallet = walletService.findByUserIdAndWalletId(walletId, userId);
			transact.setWallet(wallet);
			
			//Map Category
			Integer categoryId = transactDTO.getCategory().getCategoryId();
			Category category = categoryService.findByUserIdAndCategoryId(userId, categoryId);
			transact.setCategory(category);
			
			transactService.createTransaction(transact);
			
			
			return  ResponseEntity.ok().build();
		} catch (WalletNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO errorDTO = new ErrorDTO();
			errorDTO.setStatus(HttpStatus.NOT_FOUND.value());
			errorDTO.setTimestamp(new Date());
			errorDTO.addError(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
		} catch (CategoryNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO errorDTO = new ErrorDTO();
			errorDTO.setStatus(HttpStatus.NOT_FOUND.value());
			errorDTO.setTimestamp(new Date());
			errorDTO.addError(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
		} catch (ExceedWalletBalanceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO errorDTO = new ErrorDTO();
			errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
			errorDTO.setTimestamp(new Date());
			errorDTO.addError(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
		} 
		
	}
	
	
	//Update Transaction basing transactId and walletId, Updating 3 field, namely tranTitle,tranDescription,autoTran
	@PostMapping("/{transactId}/wallet/{walletId}")
	public ResponseEntity<?> updateTransaction(@PathVariable("transactId") Integer transactId,@PathVariable("walletId") Integer walletId, @RequestBody TransactDTO transactDTO){
		try {
			Integer userId = Utility.getIdOfCurrentLoginUser();
			Wallet wallet = walletService.findByUserIdAndWalletId(walletId, userId);
			
			Transact transactFromDatabase = transactService.findTransactionByIdAndSpecificWallet(transactId, wallet);
			
			transactService.updateTransaction(transactFromDatabase,transactDTO);
			
			return ResponseEntity.ok().build();
		} catch (WalletNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO errorDTO = Utility.createErrorObject(HttpStatus.NOT_FOUND.value(),e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
		} catch (TransactionNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO errorDTO =  Utility.createErrorObject(HttpStatus.NOT_FOUND.value(),e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
		}
	}
	
	
	
	//Creating Many Transactions, this function use for syncing process from local database to remote database
	@PostMapping("/createMany")
	public ResponseEntity<?> createAndUpdateAllTransaction(@RequestBody List<TransactDTO> transactDTOs){
		try {
			
			List<Transact> transacts = transactDTOs.stream().map(this::convertDTOToEntity).toList();
			for(Transact transact : transacts) {
				//Map Wallet
				Integer userId = Utility.getIdOfCurrentLoginUser();
				Integer walletId = transact.getWallet().getWalletId();
				Wallet wallet = walletService.findByUserIdAndWalletId(walletId, userId);
				transact.setWallet(wallet);
				
				//Map Category
				Integer categoryId = transact.getCategory().getCategoryId();
				Category category = categoryService.findByUserIdAndCategoryId(userId, categoryId);
				transact.setCategory(category);
			}
			transactService.createManyTransaction(transacts);
			return ResponseEntity.ok().build();
		}catch(WalletNotFoundException e) {
			e.printStackTrace();
			ErrorDTO errorDTO = new ErrorDTO();
			errorDTO.setStatus(HttpStatus.NOT_FOUND.value());
			errorDTO.setTimestamp(new Date());
			errorDTO.addError(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
		}catch(CategoryNotFoundException e) {
			e.printStackTrace();
			ErrorDTO errorDTO = new ErrorDTO();
			errorDTO.setStatus(HttpStatus.NOT_FOUND.value());
			errorDTO.setTimestamp(new Date());
			errorDTO.addError(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
		}
	}
	
	//Deleting Many Transactions, this function use for synchronous process from local database to remote database
	@DeleteMapping("/deleteMany")
	public ResponseEntity<?> deleteManyTransactions(@RequestBody List<TransactDTO> transactDTOs){
		try {
			List<Transact> transacts = transactDTOs.stream().map(this::convertDTOToEntity).toList();
			for(Transact transact : transacts) {
				//Map Wallet
				Integer userId = Utility.getIdOfCurrentLoginUser();
				Integer walletId = transact.getWallet().getWalletId();
				Wallet wallet = walletService.findByUserIdAndWalletId(walletId, userId);
				transact.setWallet(wallet);
				
				//Map Category
				Integer categoryId = transact.getCategory().getCategoryId();
				Category category = categoryService.findByUserIdAndCategoryId(userId, categoryId);
				transact.setCategory(category);
			}
			transactService.deleteManyTransaction(transacts);
			return ResponseEntity.ok().build();
		} catch (WalletNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO errorDTO = new ErrorDTO();
			errorDTO.setStatus(HttpStatus.NOT_FOUND.value());
			errorDTO.setTimestamp(new Date());
			errorDTO.addError(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
		} catch (CategoryNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDTO errorDTO = new ErrorDTO();
			errorDTO.setStatus(HttpStatus.NOT_FOUND.value());
			errorDTO.setTimestamp(new Date());
			errorDTO.addError(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
		}
	}
	

	public TransactDTO convertEntityToDTO(Transact transact) {
		return modelMapper.map(transact, TransactDTO.class);
	}
	
	public Transact convertDTOToEntity(TransactDTO transactDTO) {
		return modelMapper.map(transactDTO,Transact.class);
	}

}
