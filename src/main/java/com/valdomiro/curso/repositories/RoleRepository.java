package com.valdomiro.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.valdomiro.curso.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
