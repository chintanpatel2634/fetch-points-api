package com.chintanapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chintanapi.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	public List<Transaction> findByOrderByTimestampAsc();
}
