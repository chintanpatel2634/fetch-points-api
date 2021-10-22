package com.chintanapi.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chintanapi.domain.Transaction;

@Service
public class UserPointServiceImpl implements UserPointService {

	@Autowired
	TransactionService transactionService;

	@Override
	public List<Transaction> spendPoints(Integer points) {
		List<Transaction> allTransactions = transactionService.findAllOrderByTimestamp();

		Map<String, Integer> payerPointsUsedMapper = new HashMap<>();

		Map<String, List<Integer>> payerPointsUsedGroup = allTransactions.stream()
				.filter(transaction -> transaction.getPoints() < 0).collect(Collectors.groupingBy(Transaction::getPayer,
						Collectors.mapping(Transaction::getPoints, Collectors.toList())));

		payerPointsUsedGroup.forEach((payer, listOfNegPoints) -> {
			payerPointsUsedMapper.put(payer, listOfNegPoints.stream().mapToInt(point -> point).sum());
		});

		return getTransactionsUsed(allTransactions, payerPointsUsedMapper, points);
	}

	private List<Transaction> getTransactionsUsed(List<Transaction> allTransactions,
			Map<String, Integer> payerPointsUsedMapper, Integer points) {

		List<Transaction> latestTransactions = new ArrayList<>();

		for (Transaction transaction : allTransactions) {
			if (points <= 0)
				break;
			if (transaction.getPoints() <= 0)
				continue;

			String payer = transaction.getPayer();
			Integer pts = transaction.getPoints();
			Integer totalPointsSpend;
			if (!payerPointsUsedMapper.containsKey(payer) || payerPointsUsedMapper.get(payer) == 0) {
				points = saveTransaction(latestTransactions, points, pts, payer);
				continue;
			}

			totalPointsSpend = payerPointsUsedMapper.get(payer);
			if (-totalPointsSpend > pts) {
				payerPointsUsedMapper.put(payer, totalPointsSpend + pts);
			} else {
				payerPointsUsedMapper.put(payer, 0);
				Integer remainingPts = totalPointsSpend + pts;
				points = saveTransaction(latestTransactions, points, remainingPts, payer);
			}
		}

		return latestTransactions;
	}

	private Integer saveTransaction(List<Transaction> latestTransactions, Integer points, Integer pointsToRemoveFrom,
			String payer) {
		Transaction latestTransaction;
		if (pointsToRemoveFrom >= points) {
			latestTransaction = transactionService
					.save(new Transaction(payer, -points, Instant.now().truncatedTo(ChronoUnit.SECONDS)));
			points = 0;
		} else {
			latestTransaction = transactionService
					.save(new Transaction(payer, -pointsToRemoveFrom, Instant.now().truncatedTo(ChronoUnit.SECONDS)));
			points -= pointsToRemoveFrom;
		}
		latestTransactions.add(latestTransaction);
		return points;
	}

}
