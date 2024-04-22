package com.ilgrig.tuum.unit;

import com.ilgrig.tuum.model.account.ValidCountryValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class ValidCountryValidatorTest {

    private ValidCountryValidator validator;

    @Mock
    private ConstraintValidatorContext context;
    @Mock
    private ConstraintViolationBuilder constraintViolationBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new ValidCountryValidator();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addConstraintViolation()).thenReturn(context);
    }


    @Test
    void shouldRejectInvalidCountryCode() {
        assertFalse(validator.isValid("XX", context));
    }

    @Test
    void shouldAcceptValidCountryCode() {
        assertTrue(validator.isValid("SE", context));
    }

    @Test
    void shouldRejectNullCountryCode() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void shouldRejectEmptyCountryCode() {
        assertFalse(validator.isValid("", context));
    }

    @Test
    void shouldRejectBlankCountryCode() {
        assertFalse(validator.isValid("  ", context));
    }

    @Test
    void shouldRejectCountryCodeWithInvalidLength() {
        assertFalse(validator.isValid("S", context));
        assertFalse(validator.isValid("SWE", context));
    }

    @Test
    void shouldRejectCountryCodeWithLowercaseLetters() {
        assertFalse(validator.isValid("se", context));
    }

    @Test
    void shouldRejectCountryCodeWithSpecialCharacters() {
        assertFalse(validator.isValid("S*", context));
        assertFalse(validator.isValid("S-E", context));
    }

    @Test
    void shouldRejectCountryCodeWithNumbers() {
        assertFalse(validator.isValid("S1", context));
        assertFalse(validator.isValid("88", context));
    }

    @Test
    void shouldAcceptAllValidISOCountryCodes() {
        for (String countryCode : Locale.getISOCountries()) {
            assertTrue(validator.isValid(countryCode, context), "Failed for country code: " + countryCode);
        }
    }
}
