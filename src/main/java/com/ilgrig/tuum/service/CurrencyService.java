package com.ilgrig.tuum.service;

import com.ilgrig.tuum.model.CurrencyDTO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


public interface CurrencyService {

    List<CurrencyDTO> findAll(RowBounds rowBounds);

    CurrencyDTO get(Long id);

    void update(Long id, CurrencyDTO currencyDTO);

    boolean codeExists(String code);

    boolean nameExists(String name);

    boolean symbolExists(String symbol);

}
