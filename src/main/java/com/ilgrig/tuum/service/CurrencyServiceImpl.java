package com.ilgrig.tuum.service;

import com.ilgrig.tuum.converter.CurrencyConverter;
import com.ilgrig.tuum.domain.Currency;
import com.ilgrig.tuum.mapper.CurrencyMapper;
import com.ilgrig.tuum.model.CurrencyDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import com.ilgrig.tuum.util.NotFoundException;
import java.util.List;


@RequiredArgsConstructor
@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyConverter currencyConverter;
    private final CurrencyMapper currencyMapper;

    @Override
    public List<CurrencyDTO> findAll(RowBounds rowBounds) {
        final List<Currency> currencies = currencyMapper.findAll(rowBounds);
        return currencies.stream()
                .map(currencyConverter::toCurrencyDTO)
                .toList();
    }

    @Override
    public CurrencyDTO get(final Long id) {
        return currencyMapper.findById(id)
                .map(currencyConverter::toCurrencyDTO)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void update(final Long id, final CurrencyDTO currencyDTO) {
        final Currency currency = currencyMapper.findById(id)
                .orElseThrow(NotFoundException::new);
        currencyConverter.fromCurrencyDTO(currencyDTO);
        currencyMapper.update(currency);
    }

    @Override
    public boolean codeExists(final String code) {
        return currencyMapper.existsByCode(code);
    }

    @Override
    public boolean nameExists(final String name) {
        return currencyMapper.existsByName(name);
    }

    @Override
    public boolean symbolExists(final String symbol) {
        return currencyMapper.existsBySymbol(symbol);
    }

}
