package com.valdomiro.curso.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valdomiro.curso.dto.ProductDTO;
import com.valdomiro.curso.dto.UserDTO;
import com.valdomiro.curso.entities.Product;
import com.valdomiro.curso.entities.User;
import com.valdomiro.curso.repositories.ProductRepository;
import com.valdomiro.curso.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {


	@Autowired
	private ProductRepository repository;
	
	public List<ProductDTO> findAll(){
		List<Product> list= repository.findAll();
		return list.stream().map(e -> new ProductDTO(e)).collect(Collectors.toList());
	}
	/*public Product findById(Long id) {
		Optional<Product> obj= repository.findById(id);
		return obj.get();
	}*/
	public ProductDTO findById(Long id) {
		Optional<Product> obj= repository.findById(id);
		Product entity= obj.orElseThrow(() -> new ResourceNotFoundException(id));
		return new ProductDTO(entity);
	}
}
