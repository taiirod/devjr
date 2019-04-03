package com.teste.devjr.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "order")
public class Order {
	
	@Id
	@Column(name = "id")
	private int id;
	
	@Column(name = "order_date")
	private Date orderDate;
	
	@OneToMany
	@Column(name = "order_items")
	private List<OrderItem> orderItems;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Date getOrderDate() {
		return orderDate;
	}
	
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
}
