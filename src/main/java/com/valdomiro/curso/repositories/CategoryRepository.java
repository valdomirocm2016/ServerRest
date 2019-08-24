package com.valdomiro.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.valdomiro.curso.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
