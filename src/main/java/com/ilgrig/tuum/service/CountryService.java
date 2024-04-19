package com.ilgrig.tuum.service;

import com.ilgrig.tuum.model.CountryDTO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


public interface CountryService {

    List<CountryDTO> findAll(RowBounds rowBounds);

    CountryDTO get(Long id);

    void update(Long id, CountryDTO countryDTO);

    boolean codeExists(String code);

    boolean isoNumericExists(String isoNumeric);

}
