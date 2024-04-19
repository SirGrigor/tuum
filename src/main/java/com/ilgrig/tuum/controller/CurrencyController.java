package com.ilgrig.tuum.controller;

import com.ilgrig.tuum.model.CurrencyDTO;
import com.ilgrig.tuum.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/currencies", produces = MediaType.APPLICATION_JSON_VALUE)
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyDTO>> getAllCurrencies(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        RowBounds rowBounds = new RowBounds(page * size, size);
        List<CurrencyDTO> currencies = currencyService.findAll(rowBounds);
        return ResponseEntity.ok(currencies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyDTO> getCurrency(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(currencyService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCurrency(@PathVariable(name = "id") final Long id,
                                               @RequestBody @Valid final CurrencyDTO currencyDTO) {
        currencyService.update(id, currencyDTO);
        return ResponseEntity.ok(id);
    }

}
