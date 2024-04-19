package com.ilgrig.tuum.controller;

import com.ilgrig.tuum.model.account.CreationAccountDTO;
import com.ilgrig.tuum.model.account.ResponseAccountDTO;
import com.ilgrig.tuum.service.AccountService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final AccountService accountService;


    @GetMapping("/{id}")
    public ResponseEntity<ResponseAccountDTO> getAccount(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(accountService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<ResponseAccountDTO> createAccount(@RequestBody @Valid final CreationAccountDTO creationAccountDTO) {
        ResponseAccountDTO responseAccountDTO = accountService.create(creationAccountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseAccountDTO);
    }

}
