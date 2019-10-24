package com.valdomiro.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.valdomiro.curso.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
