package com.ilgrig.tuum.model.transaction;

import com.ilgrig.tuum.util.InvalidCurrencyException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidTransactionCurrencyValidator implements ConstraintValidator<ValidTransactionCurrency, String> {

    private static final Set<String> ALLOWED_CURRENCIES = Set.of("SEK", "USD", "EUR", "GBP");
    private static final Set<String> ALL_AVAILABLE_CURRENCIES = Currency.getAvailableCurrencies().stream()
            .map(Currency::getCurrencyCode)
            .collect(Collectors.toSet());

    @Override
    public boolean isValid(String currency, ConstraintValidatorContext context) {
        if (currency == null) {
            throw new InvalidCurrencyException("Currency cannot be null.");
        }

        return ALLOWED_CURRENCIES.contains(currency) && ALL_AVAILABLE_CURRENCIES.contains(currency);
    }

}