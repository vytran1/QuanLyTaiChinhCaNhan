package com.quanlychitieu.common.transact;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;

import com.quanlychitieu.common.Wallet;
import com.quanlychitieu.common.category.Category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "transact")
public class Transact {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tran_id")
	private Integer transactId;
	
	@Column(name = "tran_title")
	private String tranTitle;
	
	@Column(name = "date_")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@Column(name = "tran_amount")
	private Long tranAmount;
	
	@Column(name = "tran_description")
	private String tranDescription;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "auto_tran", columnDefinition = "enum('none','date','month','year') DEFAULT 'none'")
	private AutoTran autoTran;
	
	@ManyToOne(targetEntity = Wallet.class)
	@JoinColumn(name = "wallet_id")
	private Wallet wallet;
	
	@ManyToOne(targetEntity = Category.class)
	@JoinColumn(name = "category_id")
	private Category category;

	public Transact() {
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

	public AutoTran getAutoTran() {
		return autoTran;
	}

	public void setAutoTran(AutoTran autoTran) {
		this.autoTran = autoTran;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Transact [transactId=" + transactId + ", tranTitle=" + tranTitle + ", date=" + date + ", tranAmount="
				+ tranAmount + ", tranDescription=" + tranDescription + ", autoTran=" + autoTran + ", wallet=" + wallet
				+ ", category=" + category + "]";
	}
	
	@PrePersist
	public void updateDate() {
		this.date = new Date();
	}
	
	
}
