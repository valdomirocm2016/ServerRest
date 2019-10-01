package com.valdomiro.curso.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.valdomiro.curso.entities.Order;
import com.valdomiro.curso.entities.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByClient(User client);
}
