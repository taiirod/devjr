package com.teste.devjr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

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
	private BigDecimal industryPrice;
	
	@Column(name = "discount")
	private BigDecimal discount;

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

	public BigDecimal getIndustryPrice() {
		return industryPrice;
	}

	public void setIndustryPrice(BigDecimal industryPrice) {
		this.industryPrice = industryPrice;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Product product = (Product) o;
		return quantityAvailable == product.quantityAvailable &&
				Objects.equals(sku, product.sku) &&
				Objects.equals(name, product.name) &&
				Objects.equals(industryPrice, product.industryPrice) &&
				Objects.equals(discount, product.discount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sku, name, quantityAvailable, industryPrice, discount);
	}
}
