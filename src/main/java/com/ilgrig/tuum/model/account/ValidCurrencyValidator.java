package com.ilgrig.tuum.model.account;

import com.ilgrig.tuum.mapper.CurrencyMapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
public class ValidCurrencyValidator implements ConstraintValidator<ValidCurrency, List<String>> {

    private CurrencyMapper currencyMapper;

    @Override
    public void initialize(ValidCurrency constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<String> currencies, ConstraintValidatorContext context) {
        if (currencies == null || currencies.isEmpty()) {
            return false;
        }
        return currencies.stream()
                .allMatch(currency ->
                        currency != null &&
                                currency.matches("[A-Z]{3}") &&
                                currencyMapper.existsByCode(currency)
                );
    }
}
