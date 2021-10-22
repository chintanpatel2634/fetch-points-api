package com.chintanapi.service;

import java.util.List;

import com.chintanapi.domain.Transaction;

public interface UserPointService {
	public List<Transaction> spendPoints(Integer points);
}
