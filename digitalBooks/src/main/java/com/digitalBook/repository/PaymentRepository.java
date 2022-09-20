package com.digitalBook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digitalBook.models.Payment;

@Repository
public interface  PaymentRepository extends JpaRepository<Payment,Integer> {
	List<Payment> findByUserId(String name);
}
