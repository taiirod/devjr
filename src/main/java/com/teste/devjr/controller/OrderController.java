package com.teste.devjr.controller;

import com.teste.devjr.model.Order;
import com.teste.devjr.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;


    @GetMapping
    private List<Order> all () {
        return orderRepository.findAll();
    }

    @GetMapping("{id}")
    private Optional<Order> byId (@PathVariable int id) {
        return orderRepository.findById(id);
    }

    @PostMapping
    private Order add (@RequestBody Order order) {
        return orderRepository.save(order);
    }

}
