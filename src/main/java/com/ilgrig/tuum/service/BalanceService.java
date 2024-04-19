package com.ilgrig.tuum.service;

import com.ilgrig.tuum.model.BalanceDTO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


public interface BalanceService {

    List<BalanceDTO> findAll(RowBounds rowBounds);

    BalanceDTO get(Long id);

    Long create(BalanceDTO balanceDTO);

    void update(Long id, BalanceDTO balanceDTO);

    boolean currencyExists(String code);

}
