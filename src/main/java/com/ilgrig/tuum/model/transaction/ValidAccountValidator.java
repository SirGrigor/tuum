package com.ilgrig.tuum.model.transaction;

import com.ilgrig.tuum.domain.Account;
import com.ilgrig.tuum.mapper.AccountMapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class ValidAccountValidator implements ConstraintValidator<ValidAccount, Long> {

    private AccountMapper accountMapper;

    @Override
    public boolean isValid(Long accountId, ConstraintValidatorContext context) {
        if (accountId == null) {
            return false;
        }

        Optional<Account> account = accountMapper.findById(accountId);
        if (account.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid account ID: No such account exists")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}