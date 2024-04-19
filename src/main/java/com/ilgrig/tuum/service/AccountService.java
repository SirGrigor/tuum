package com.ilgrig.tuum.service;

import com.ilgrig.tuum.model.account.CreationAccountDTO;
import com.ilgrig.tuum.model.account.ResponseAccountDTO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


public interface AccountService {

    ResponseAccountDTO get(Long id);

    ResponseAccountDTO create(CreationAccountDTO creationAccountDTO);

    boolean countryExists(String code);

    boolean currencyExists(String code);

}
