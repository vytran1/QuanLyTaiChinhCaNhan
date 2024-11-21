package com.quanlychitieu.common;

import com.quanlychitieu.common.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "wallet")
public class Wallet {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wallet_id")
	private Integer walletId;
	
	@Column(name = "wallet_title")
	private String walletTitle;
	
	@Column(name = "wallet_amount")
	private Long walletAmount;
	
	@Column(name = "wallet_description")
	private String walletDescription;
	
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id", referencedColumnName = "userId")
	private User user;
	
	
	public Wallet() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public Wallet(Integer walletId, String walletTitle, Long walletAmount, String walletDescription, User user) {
		super();
		this.walletId = walletId;
		this.walletTitle = walletTitle;
		this.walletAmount = walletAmount;
		this.walletDescription = walletDescription;
		this.user = user;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}



	@Override
	public String toString() {
		return "Wallet [walletId=" + walletId + ", walletTitle=" + walletTitle + ", walletAmount=" + walletAmount
				+ ", walletDescription=" + walletDescription + ", user=" + user + "]";
	}
	
	
}
