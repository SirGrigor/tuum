package com.ilgrig.tuum.controller;

import com.ilgrig.tuum.model.CountryDTO;
import com.ilgrig.tuum.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/countries", produces = MediaType.APPLICATION_JSON_VALUE)
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public ResponseEntity<List<CountryDTO>> getAllCountries(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        RowBounds rowBounds = new RowBounds(page * size, size);
        List<CountryDTO> countries = countryService.findAll(rowBounds);
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryDTO> getCountry(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(countryService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCountry(@PathVariable(name = "id") final Long id,
                                              @RequestBody @Valid final CountryDTO countryDTO) {
        countryService.update(id, countryDTO);
        return ResponseEntity.ok(id);
    }

}
