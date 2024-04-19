package com.ilgrig.tuum.model.account;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import com.ilgrig.tuum.mapper.CountryMapper;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor

public class ValidCountryValidator implements ConstraintValidator<ValidCountry, String> {


    private CountryMapper countryMapper;

    @Override
    public void initialize(ValidCountry constraintAnnotation) {
    }

    @Override
    public boolean isValid(String countryValue, ConstraintValidatorContext context) {
        if (countryValue == null || !countryValue.matches("[A-Z]{2}")) {
            return false;
        }
        return countryMapper.existsByCode(countryValue);
    }
}
