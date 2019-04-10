package com.teste.devjr.controller;

import com.teste.devjr.model.OrderItem;
import com.teste.devjr.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order_item")
public class OrderItemController {

    @Autowired
    private OrderItemRepository orderItemRepository;


    @GetMapping
    private List<OrderItem> all() {
        return orderItemRepository.findAll();
    }

    @PostMapping
    private OrderItem add(@RequestBody OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

}
