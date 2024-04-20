package com.ilgrig.tuum.controller;

import com.ilgrig.tuum.model.transaction.CreationTransactionDTO;
import com.ilgrig.tuum.model.transaction.ResponseTransactionDTO;
import com.ilgrig.tuum.service.TransactionService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

//    private final TransactionService transactionService;

//    @GetMapping
//    public ResponseEntity<List<ResponseTransactionDTO>> getAllTransactions(
//            @RequestParam(name = "page", defaultValue = "0") int page,
//            @RequestParam(name = "size", defaultValue = "20") int size) {
//        RowBounds rowBounds = new RowBounds(page * size, size);
//        List<ResponseTransactionDTO> transactions = transactionService.findAll(rowBounds);
//        return ResponseEntity.ok(transactions);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ResponseTransactionDTO> getTransaction(@PathVariable(name = "id") final Long id) {
//        return ResponseEntity.ok(transactionService.get(id));
//    }
//
//    @PostMapping
//    @ApiResponse(responseCode = "201")
//    public ResponseEntity<ResponseTransactionDTO> createTransaction(
//            @RequestBody @Valid final CreationTransactionDTO creationTransactionDTO) {
//        ResponseTransactionDTO responseTransactionDTO = transactionService.create(creationTransactionDTO);
//        return new ResponseEntity<>(responseTransactionDTO, HttpStatus.CREATED);
//    }

}
