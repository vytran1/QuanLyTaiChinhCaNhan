package com.quanlychitieu.transact;

import java.util.Date;

import com.quanlychitieu.category.CategoryDTO;
import com.quanlychitieu.wallet.WalletDTO;

public class TransactDTO {
    private Integer transactId;
    
//    @Length(min = 5, max = 50, message = "transaction's tile lenght must range from 5 to 50 characters")
//    @NotNull(message = "Transaction title is required")
    private String  tranTitle;
    private Date    date;
    
//    @Min(1)
//    @NotNull(message = "Transaction amount is required")
    private Long    tranAmount;
    
//    @NotNull(message = "Wallet Description is required")
//    @Size(max = 65535, message = "Wallet Descrtiption must not exceed 65535 character")
    private String  tranDescription;
    private String  autoTran;
    private WalletDTO wallet;
    private CategoryDTO category;
    
    
	


	public TransactDTO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Integer getTransactId() {
		return transactId;
	}


	public void setTransactId(Integer transactId) {
		this.transactId = transactId;
	}


	public String getTranTitle() {
		return tranTitle;
	}


	public void setTranTitle(String tranTitle) {
		this.tranTitle = tranTitle;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public Long getTranAmount() {
		return tranAmount;
	}


	public void setTranAmount(Long tranAmount) {
		this.tranAmount = tranAmount;
	}


	public String getTranDescription() {
		return tranDescription;
	}


	public void setTranDescription(String tranDescription) {
		this.tranDescription = tranDescription;
	}


	public String getAutoTran() {
		return autoTran;
	}


	public void setAutoTran(String autoTran) {
		this.autoTran = autoTran;
	}


	public WalletDTO getWallet() {
		return wallet;
	}


	public void setWallet(WalletDTO wallet) {
		this.wallet = wallet;
	}


	public CategoryDTO getCategory() {
		return category;
	}


	public void setCategory(CategoryDTO category) {
		this.category = category;
	}


	
    
    
    
}
