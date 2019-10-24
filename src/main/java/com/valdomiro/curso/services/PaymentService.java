package com.valdomiro.curso.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valdomiro.curso.dto.PaymentDTO;
import com.valdomiro.curso.entities.Payment;
import com.valdomiro.curso.repositories.PaymentRepository;
import com.valdomiro.curso.services.exceptions.ResourceNotFoundException;

@Service
public class PaymentService {
	
	@Autowired
	private PaymentRepository repository;
	
	public List<PaymentDTO> findAll(){
		List<Payment> list= repository.findAll();
		return list.stream().map(e -> new PaymentDTO(e)).collect(Collectors.toList());
	}
	public PaymentDTO findById(Long id) {
		
		Optional<Payment> obj= repository.findById(id);
		Payment entity= obj.orElseThrow(() -> new ResourceNotFoundException(id));
		return new PaymentDTO(entity);
	}

}
