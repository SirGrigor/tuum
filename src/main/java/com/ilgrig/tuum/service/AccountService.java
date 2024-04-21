package com.ilgrig.tuum.service;

import com.ilgrig.tuum.model.account.CreationAccountDTO;
import com.ilgrig.tuum.model.account.ResponseAccountDTO;


public interface AccountService {

    ResponseAccountDTO get(Long id);

    ResponseAccountDTO create(CreationAccountDTO creationAccountDTO);

    boolean validateAccountExistence(Long accountId);
}
