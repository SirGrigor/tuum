package com.ilgrig.tuum.service;

import com.ilgrig.tuum.model.AccountDTO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


public interface AccountService {

    List<AccountDTO> findAll(RowBounds rowBounds);

    AccountDTO get(Long id);

    Long create(AccountDTO accountDTO);

    void update(Long id, AccountDTO accountDTO);

    boolean countryExists(String code);

    boolean currencyExists(String code);

}
