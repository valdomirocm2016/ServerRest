package com.valdomiro.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.valdomiro.curso.entities.Category;
import com.valdomiro.curso.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
