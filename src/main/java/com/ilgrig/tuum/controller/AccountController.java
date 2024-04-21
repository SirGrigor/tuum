package com.ilgrig.tuum.controller;

import com.ilgrig.tuum.mapper.AccountMapper;
import com.ilgrig.tuum.model.account.CreationAccountDTO;
import com.ilgrig.tuum.model.account.ResponseAccountDTO;
import com.ilgrig.tuum.service.AccountService;
import com.ilgrig.tuum.util.CustomApiResponse;
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Validated
@RequestMapping(value = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @Operation(summary = "Retrieve an account", description = "Retrieves account details by ID")
    @ApiResponse(responseCode = "200", description = "Account retrieved successfully", content = @Content(schema = @Schema(implementation = ResponseAccountDTO.class)))
    @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<ResponseAccountDTO>> getAccount(@PathVariable Long id) {
        ResponseAccountDTO account = accountService.get(id);
        return ResponseEntity.ok(CustomApiResponse.success(account));
    }

    @Operation(summary = "Create an account", description = "Creates a new account with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created successfully", content = @Content(schema = @Schema(implementation = ResponseAccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters or account already exists", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<CustomApiResponse<ResponseAccountDTO>> createAccount(@Valid @RequestBody CreationAccountDTO creationAccountDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", String.join(", ", errors));
            return ResponseEntity.badRequest().body(CustomApiResponse.error(errorResponse));
        }

        if (accountMapper.existsByCustomerId(creationAccountDTO.getCustomerId())) {
            ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.CONFLICT, "Conflict", "Account already exists for customer id");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(CustomApiResponse.error(errorResponse));
        }
        ResponseAccountDTO responseAccountDTO = accountService.create(creationAccountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomApiResponse.success(responseAccountDTO));
    }
}


