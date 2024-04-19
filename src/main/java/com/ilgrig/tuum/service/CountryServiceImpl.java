package com.ilgrig.tuum.service;

import com.ilgrig.tuum.converter.CountryConverter;
import com.ilgrig.tuum.domain.Country;
import com.ilgrig.tuum.mapper.CountryMapper;
import com.ilgrig.tuum.model.CountryDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import com.ilgrig.tuum.util.NotFoundException;
import java.util.List;


@RequiredArgsConstructor

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryMapper countryMapper;
    private final CountryConverter countryConverter;

    @Override
    public List<CountryDTO> findAll(RowBounds rowBounds) {
        final List<Country> countries = countryMapper.findAll();
        return countries.stream()
                .map(countryConverter::toCountryDTO)
                .toList();
    }

    @Override
    public CountryDTO get(final Long id) {
        return countryMapper.findById(id)
                .map(countryConverter::toCountryDTO)
                .orElseThrow(NotFoundException::new);

    }

    @Override
    public void update(final Long id, final CountryDTO countryDTO) {
        final Country country = countryMapper.findById(id)
                .orElseThrow(NotFoundException::new);
        countryConverter.fromCountryDTO(countryDTO);
        countryMapper.update(country);
    }

    @Override
    public boolean codeExists(final String code) {
        return countryMapper.existsByCode(code);
    }

    @Override
    public boolean isoNumericExists(final String isoNumeric) {
        return countryMapper.existsByIsoNumeric(isoNumeric);
    }

}
