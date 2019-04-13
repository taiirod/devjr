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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "order_date")
	private LocalDateTime orderDate;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "order_item", joinColumns = @JoinColumn(name = "id"),
			inverseJoinColumns = @JoinColumn(name ="id_order"))
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Order order = (Order) o;
		return id == order.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}