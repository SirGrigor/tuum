package com.ilgrig.tuum.controller;

import com.ilgrig.tuum.model.transaction.CreationTransactionDTO;
import com.ilgrig.tuum.model.transaction.ResponseTransactionDTO;
import com.ilgrig.tuum.service.TransactionService;
import com.ilgrig.tuum.util.AccountNotFoundException;
import com.ilgrig.tuum.util.CustomApiResponse;
import com.ilgrig.tuum.util.InsufficientFundsException;
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Create a new transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction created successfully", content = @Content(schema = @Schema(implementation = ResponseTransactionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Insufficient funds", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<CustomApiResponse<ResponseTransactionDTO>> createTransaction(@Valid @RequestBody CreationTransactionDTO creationDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", String.join(", ", errors));
            return ResponseEntity.badRequest().body(CustomApiResponse.error(apiError));
        }

        try {
            ResponseTransactionDTO response = transactionService.createTransaction(creationDTO);
            return ResponseEntity.ok(CustomApiResponse.success(response));
        } catch (AccountNotFoundException ex) {
            ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.NOT_FOUND, "Account not found", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomApiResponse.error(apiError));
        } catch (InsufficientFundsException ex) {
            ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.CONFLICT, "Insufficient Funds", ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(CustomApiResponse.error(apiError));
        } catch (Exception ex) {
            ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CustomApiResponse.error(apiError));
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<CustomApiResponse<List<ResponseTransactionDTO>>> getTransactionsByAccountId(@PathVariable Long accountId, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10") int limit) {
        List<ResponseTransactionDTO> transactions = transactionService.findAllByAccountId(accountId, new RowBounds(offset, limit));
        return ResponseEntity.ok(CustomApiResponse.success(transactions));
    }

}
