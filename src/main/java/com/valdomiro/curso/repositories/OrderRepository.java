package com.valdomiro.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.valdomiro.curso.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
