package com.ilgrig.tuum.controller;

import com.ilgrig.tuum.mapper.AccountMapper;
import com.ilgrig.tuum.model.account.CreationAccountDTO;
import com.ilgrig.tuum.model.account.ResponseAccountDTO;
import com.ilgrig.tuum.service.AccountService;
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;


    @GetMapping("/{id}")
    public ResponseEntity<ResponseAccountDTO> getAccount(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(accountService.get(id));
    }

    @Operation(summary = "Create a new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "409", description = "Account already exists for customer id"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<?> createAccount(@Valid @RequestBody CreationAccountDTO creationAccountDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", String.join(", ", errors)));
        }

        if (accountMapper.existsByCustomerId(creationAccountDTO.getCustomerId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorResponse(HttpStatus.CONFLICT, "Conflict", "Account already exists for customer id"));
        }

        ResponseAccountDTO responseAccountDTO = accountService.create(creationAccountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseAccountDTO);
    }
}
