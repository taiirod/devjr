package com.teste.devjr.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "\"order\"")
public class Order {

	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "order_date")
	private LocalDateTime orderDate;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "order_item", joinColumns = @JoinColumn(name = "id_order"),
			inverseJoinColumns = @JoinColumn(name ="id"))
	private List<OrderItem> orderItems;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
}