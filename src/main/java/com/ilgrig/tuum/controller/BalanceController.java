package com.ilgrig.tuum.controller;

import com.ilgrig.tuum.model.BalanceDTO;
import com.ilgrig.tuum.service.BalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping(value = "/api/balances", produces = MediaType.APPLICATION_JSON_VALUE)
public class BalanceController {

    private final BalanceService balanceService;

    @Operation(
            parameters = {
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "sort",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = String.class)
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<BalanceDTO>> getAllBalances(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        RowBounds rowBounds = new RowBounds(page * size, size);
        List<BalanceDTO> balances = balanceService.findAll(rowBounds);
        return ResponseEntity.ok(balances);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BalanceDTO> getBalance(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(balanceService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createBalance(@RequestBody @Valid final BalanceDTO balanceDTO) {
        final Long createdId = balanceService.create(balanceDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateBalance(@PathVariable(name = "id") final Long id,
                                              @RequestBody @Valid final BalanceDTO balanceDTO) {
        balanceService.update(id, balanceDTO);
        return ResponseEntity.ok(id);
    }

}
