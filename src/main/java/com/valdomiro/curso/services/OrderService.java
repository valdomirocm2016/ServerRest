package com.valdomiro.curso.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.valdomiro.curso.dto.CategoryDTO;
import com.valdomiro.curso.dto.OrderDTO;
import com.valdomiro.curso.dto.OrderItemDTO;
import com.valdomiro.curso.entities.Category;
import com.valdomiro.curso.entities.Order;
import com.valdomiro.curso.entities.OrderItem;
import com.valdomiro.curso.entities.Product;
import com.valdomiro.curso.entities.User;
import com.valdomiro.curso.entities.enums.OrderStatus;
import com.valdomiro.curso.repositories.OrderItemRepository;
import com.valdomiro.curso.repositories.OrderRepository;
import com.valdomiro.curso.repositories.ProductRepository;
import com.valdomiro.curso.repositories.UserRepository;
import com.valdomiro.curso.services.exceptions.ResourceNotFoundException;


@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private UserRepository userRepository;
	
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
	
	@Transactional(readOnly=true)
	public List<OrderDTO> findByClientId(Long clientId) {
		User client= userRepository.getOne(clientId);
		List<Order> list= repository.findByClient(client);
		
		return list.stream().map(e -> new OrderDTO(e)).collect(Collectors.toList());
	}
	
	@Transactional
	public OrderDTO placeOrder(List<OrderItemDTO> dto) {
		User client= authService.authenticated();
		Order order= new Order(null,Instant.now(),OrderStatus.WAITING_PAYMENT,client);
		
		for(OrderItemDTO it:dto) {
			Product product=productRepository.getOne(it.getProductId());
			OrderItem item= new OrderItem(order,product,it.getQuantity(),it.getPrice());
			order.getItens().add(item);
		}
		
		repository.save(order);
		orderItemRepository.saveAll(order.getItens());
		
		return new OrderDTO(order);
	}
	
	@Transactional
	public OrderDTO update(Long id, OrderDTO dto) {
		try {
		Order entity= repository.getOne(id);
		updateData(entity, dto);
		entity= repository.save(entity);
		return new OrderDTO(entity);
		
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
		
	
	}
	private void updateData(Order entity, OrderDTO dto) {
		// TODO Auto-generated method stub
		entity.setOrderStatus(dto.getOrderStatus());
		
	}

}
