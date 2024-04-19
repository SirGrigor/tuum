package com.ilgrig.tuum.service;

import com.ilgrig.tuum.model.balance.CreationBalanceDTO;
import com.ilgrig.tuum.model.balance.ResponseBalanceDTO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


public interface BalanceService {

    List<ResponseBalanceDTO> findAll();

    ResponseBalanceDTO get(Long id);

    Long create(CreationBalanceDTO creationBalanceDTO);

    boolean currencyExists(String code);

}
