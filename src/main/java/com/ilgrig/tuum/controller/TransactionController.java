package com.ilgrig.tuum.controller;

import com.ilgrig.tuum.domain.Transaction;
import com.ilgrig.tuum.model.transaction.CreationTransactionDTO;
import com.ilgrig.tuum.model.transaction.ResponseTransactionDTO;
import com.ilgrig.tuum.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ResponseTransactionDTO> createTransaction(@RequestBody CreationTransactionDTO creationDTO) {
        ResponseTransactionDTO response = transactionService.createTransaction(creationDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<ResponseTransactionDTO>> getTransactionsByAccountId(@PathVariable Long accountId
            , @RequestParam(defaultValue = "0") int offset
            , @RequestParam(defaultValue = "10") int limit) {
        List<ResponseTransactionDTO> transactions = transactionService.findAll(accountId, new RowBounds(offset, limit));
        return ResponseEntity.ok(transactions);
    }

    private ResponseTransactionDTO convertToResponseDTO(Transaction transaction) {
        return new ResponseTransactionDTO();
    }
}
