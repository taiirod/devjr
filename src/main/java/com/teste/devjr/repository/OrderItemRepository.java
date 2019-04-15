package com.teste.devjr.repository;

import com.teste.devjr.model.Order;
import com.teste.devjr.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

    List<OrderItem> findAllByIdOrder(int idOrder);

}
