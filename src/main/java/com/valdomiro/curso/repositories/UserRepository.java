package com.valdomiro.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.valdomiro.curso.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
