package com.valdomiro.curso.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.valdomiro.curso.dto.OrderDTO;
import com.valdomiro.curso.dto.OrderItemDTO;
import com.valdomiro.curso.entities.Order;
import com.valdomiro.curso.entities.OrderItem;
import com.valdomiro.curso.entities.User;
import com.valdomiro.curso.repositories.OrderRepository;
import com.valdomiro.curso.services.exceptions.ResourceNotFoundException;


@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private AuthService authService;
	
	public List<OrderDTO> findAll(){
		List<Order> list= repository.findAll();
		return list.stream().map(e -> new OrderDTO(e)).collect(Collectors.toList());
	}
	public OrderDTO findById(Long id) {
		Optional<Order> obj= repository.findById(id);
		Order entity= obj.orElseThrow(() -> new ResourceNotFoundException(id));
		
		authService.validadeOwnOrderOrAdmin(entity);
		
		return new OrderDTO(entity);
	}
	
	public List<OrderDTO> findByClient() {
		User client= authService.authenticated();
		List<Order> list= repository.findByClient(client);
		
		return list.stream().map(e -> new OrderDTO(e)).collect(Collectors.toList());
		
	}
	
	@Transactional(readOnly=true)
	public List<OrderItemDTO> findItems(Long id) {
		Order order= repository.getOne(id);
		authService.validadeOwnOrderOrAdmin(order);
		Set<OrderItem> set = order.getItens();
		
		return set.stream().map(e -> new OrderItemDTO(e)).collect(Collectors.toList());
	}

}
