package com.ilgrig.tuum.model.account;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Locale;

public class ValidCountryValidator implements ConstraintValidator<ValidCountry, String> {

    private final String[] isoCountries = Locale.getISOCountries();

    @Override
    public void initialize(ValidCountry constraintAnnotation) {
    }

    @Override
    public boolean isValid(String countryValue, ConstraintValidatorContext context) {
        if (countryValue == null || countryValue.isEmpty()) {
            return false;
        }

        return Arrays.asList(this.isoCountries).contains(countryValue);
    }
}
