package com.chintanapi.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chintanapi.domain.Transaction;
import com.chintanapi.service.PayerPointService;
import com.chintanapi.service.TransactionService;
import com.chintanapi.service.UserPointService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("api")
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	UserPointService userPointService;
	
	@Autowired
	PayerPointService payerPointService;
	
	@PostMapping(value="addTransaction")
	public ResponseEntity<?> addNewTransaction(@RequestBody JsonNode request) {
		String payer = request.get("payer").asText();
		Integer points = request.get("points").asInt();
		Instant timestamp = Instant.parse(request.get("timestamp").asText()).truncatedTo(ChronoUnit.SECONDS);
		Transaction transaction = new Transaction(payer, points, timestamp);
		transactionService.save(transaction);
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping(value="point/spend")
	public ResponseEntity<ArrayNode> spendPoints(@RequestBody JsonNode request) {
		Integer points = request.get("points").asInt();
		List<Transaction> transactions = userPointService.spendPoints(points);
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode arrayNode = mapper.createArrayNode();
		transactions.forEach(transaction -> {
			ObjectNode jsonTransaction = mapper.createObjectNode();
			jsonTransaction.put("payer", transaction.getPayer());
			jsonTransaction.put("points", transaction.getPoints());
			arrayNode.add(jsonTransaction);
		});
		return new ResponseEntity<>(arrayNode, HttpStatus.ACCEPTED);
	}
	
	@GetMapping(value="payer/balance")
	public ResponseEntity<JsonNode> allPayersBalance() {
		Map<String, Integer> payerPointsMapper = payerPointService.getPayerPoints();
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode payerPointsJson = mapper.createObjectNode();
		
		payerPointsMapper.forEach((payer, points) -> {
			payerPointsJson.put(payer, points);   
		});
		
		return new ResponseEntity<JsonNode>(payerPointsJson, HttpStatus.OK);
	}
}
 