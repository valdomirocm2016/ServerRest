package com.valdomiro.curso.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.valdomiro.curso.dto.CategoryDTO;
import com.valdomiro.curso.dto.UserDTO;
import com.valdomiro.curso.entities.Category;
import com.valdomiro.curso.entities.User;
import com.valdomiro.curso.repositories.CategoryRepository;
import com.valdomiro.curso.services.exceptions.DatabaseException;
import com.valdomiro.curso.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	public List<CategoryDTO> findAll(){
		List<Category> list= repository.findAll();
		return list.stream().map(e -> new CategoryDTO(e)).collect(Collectors.toList());
	}
	public CategoryDTO findById(Long id) {
		
		Optional<Category> obj= repository.findById(id);
		Category entity= obj.orElseThrow(() -> new ResourceNotFoundException(id));
		return new CategoryDTO(entity);
	}
	
	public CategoryDTO insert(CategoryDTO obj) {
				
		Category entity= obj.toEntity();
		entity= repository.save(entity);	
		return new CategoryDTO(entity);	
	}
	public void delete(Long id) {
		try {
		repository.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			
			throw new ResourceNotFoundException(id);
		
		}catch(DataIntegrityViolationException ex){
			throw new DatabaseException(ex.getMessage());
		}
	}
	
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
		Category entity= repository.getOne(id);
		updateData(entity, dto);
		entity= repository.save(entity);
		return new CategoryDTO(entity);
		
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
		
	
	}
	private void updateData(Category entity, CategoryDTO dto) {
		// TODO Auto-generated method stub
		entity.setName(dto.getName());
		
	}

}
