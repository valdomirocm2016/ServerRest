package com.valdomiro.curso.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.valdomiro.curso.entities.Category;
import com.valdomiro.curso.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT obj FROM Product obj INNER JOIN obj.categories cats WHERE LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%')) AND cats IN :categories")
	Page<Product> findByNameContainingIgnoreCaseAndCategoriesIn(@Param("name") String name, @Param("categories") List<Category> categories, Pageable pageable);
	
	@Transactional(readOnly = true)
	@Query("SELECT obj FROM Product obj WHERE LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%'))")
	Page<Product> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
	
	@Transactional(readOnly = true)
	@Query("SELECT obj FROM Product obj INNER JOIN obj.categories cats WHERE :category IN cats")
	Page<Product>findByCategory(@Param("category") Category category,Pageable pageable);

}
