package com.teste.devjr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {
	
	@Id
	@Column(name = "sku")
	private String sku;
	
	@Column(name = "product_name")
	private String name;
	
	@Column(name = "quantity_available")
	private int quantityAvailable;
	
	@Column(name = "industry_price")
	private double industryPrice;
	
	@Column(name = "discount")
	private double discount;
	
	public String getSku() {
		return sku;
	}
	
	public void setSku(String sku) {
		this.sku = sku;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getQuantityAvailable() {
		return quantityAvailable;
	}
	
	public void setQuantityAvailable(int quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
	
	public double getIndustryPrice() {
		return industryPrice;
	}
	
	public void setIndustryPrice(double industryPrice) {
		this.industryPrice = industryPrice;
	}
	
	public double getDiscount() {
		return discount;
	}
	
	public void setDiscount(double discount) {
		this.discount = discount;
	}
}
