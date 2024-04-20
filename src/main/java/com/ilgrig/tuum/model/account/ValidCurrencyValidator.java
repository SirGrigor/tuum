package com.ilgrig.tuum.model.account;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidCurrencyValidator implements ConstraintValidator<ValidCurrency, List<String>> {

    private static final Set<String> ALLOWED_CURRENCIES = Set.of("SEK", "USD", "EUR", "GBP");
    private static final Set<String> ALL_AVAILABLE_CURRENCIES = Currency.getAvailableCurrencies().stream()
            .map(Currency::getCurrencyCode)
            .collect(Collectors.toSet());

    @Override
    public void initialize(ValidCurrency constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<String> currencies, ConstraintValidatorContext context) {
        if (currencies == null || currencies.isEmpty()) {
            return false;
        }

        return currencies.stream().allMatch(currency -> ALLOWED_CURRENCIES.contains(currency) && ALL_AVAILABLE_CURRENCIES.contains(currency));
    }
}
