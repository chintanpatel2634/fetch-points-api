package com.chintanapi.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chintanapi.domain.Transaction;


@Service
public class PayerPointServiceImpl implements PayerPointService{

	@Autowired
	TransactionService transactionService;
	
	@Override
	public Map<String, Integer> getPayerPoints() {
		Map<String,Integer> payerTotalPointsMap = new HashMap<>();

		List<Transaction> allTransactions = transactionService.findAll();

		Map<String, List<Integer>> payerPointsMapper = allTransactions.stream()
			.collect(Collectors
					.groupingBy(Transaction::getPayer, 
							Collectors.mapping(Transaction::getPoints, 
									Collectors.toList())));
		
		payerPointsMapper.forEach((payer, listOfPoints) -> {
			payerTotalPointsMap.put(payer, 
					listOfPoints.stream().mapToInt(point->point).sum());
		});
		return payerTotalPointsMap;
	}

}
