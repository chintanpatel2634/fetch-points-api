package com.chintanapi.service;

import java.util.List;

import com.chintanapi.domain.Transaction;

public interface TransactionService {
	public Transaction save(Transaction s);
	public List<Transaction> findAllOrderByTimestamp();
	public List<Transaction> findAll();
}
