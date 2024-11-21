package com.quanlychitieu.wallet;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class WalletDTO {
    private Integer walletId;
    
    @Length(min = 10, max = 70, message = "Wallet Title must contains at least 10 characters ")
    @NotNull(message = "Wallet Title is required")
    private String walletTitle;
    
    @NotNull(message = "Wallet Amount is required")
    //@Positive(message = "Wallet Amount must be greater than zero")
    private Long walletAmount;
    
    @NotNull(message = "Wallet Description is required")
    @Size(max = 65535, message = "Wallet Descrtiption must not exceed 65535 character")
    private String walletDescription;
	public WalletDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WalletDTO(Integer walletId, String walletTitle, Long walletAmount, String walletDescription) {
		super();
		this.walletId = walletId;
		this.walletTitle = walletTitle;
		this.walletAmount = walletAmount;
		this.walletDescription = walletDescription;
	}
	public Integer getWalletId() {
		return walletId;
	}
	public void setWalletId(Integer walletId) {
		this.walletId = walletId;
	}
	public String getWalletTitle() {
		return walletTitle;
	}
	public void setWalletTitle(String walletTitle) {
		this.walletTitle = walletTitle;
	}
	public Long getWalletAmount() {
		return walletAmount;
	}
	public void setWalletAmount(Long walletAmount) {
		this.walletAmount = walletAmount;
	}
	public String getWalletDescription() {
		return walletDescription;
	}
	public void setWalletDescription(String walletDescription) {
		this.walletDescription = walletDescription;
	}
	@Override
	public String toString() {
		return "WalletDTO [walletId=" + walletId + ", walletTitle=" + walletTitle + ", walletAmount=" + walletAmount
				+ ", walletDescription=" + walletDescription + "]";
	}
    
    
}
