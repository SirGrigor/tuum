package com.ilgrig.tuum.model.account;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Locale;

public class ValidCountryValidator implements ConstraintValidator<ValidCountry, String> {

    private final String[] isoCountries = Locale.getISOCountries();

    @Override
    public void initialize(ValidCountry constraintAnnotation) {
    }

    @Override
    public boolean isValid(String countryValue, ConstraintValidatorContext context) {
        if (countryValue == null || countryValue.trim().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Country cannot be null or empty.")
                    .addConstraintViolation();
            return false;
        }

        if (!Arrays.asList(isoCountries).contains(countryValue)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid country. Must be a valid ISO country code.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
