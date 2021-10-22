package com.chintanapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chintanapi.domain.Transaction;
import com.chintanapi.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	TransactionRepository transactionRepository;
	
	@Override
	public Transaction save(Transaction s) {
		return transactionRepository.save(s);
	}

	@Override
	public List<Transaction> findAll() {
		return transactionRepository.findAll();
	}

	@Override
	public List<Transaction> findAllOrderByTimestamp() {
		return transactionRepository.findByOrderByTimestampAsc();
	}
		
}
