package com.digitalkhata.api.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            buildMessage(context, "Password cannot be null");
            return false;
        }

        // Rule 1: Minimum 8 characters
        if (password.length() < 8) {
            buildMessage(context, "Password must be at least 8 characters");
            return false;
        }

        // Rule 2: At least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            buildMessage(context, "Password must contain at least one uppercase letter");
            return false;
        }

        // Rule 3: At least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            buildMessage(context, "Password must contain at least one lowercase letter");
            return false;
        }

        // Rule 4: At least one digit
        if (!password.matches(".*[0-9].*")) {
            buildMessage(context, "Password must contain at least one number");
            return false;
        }

        // Rule 5: At least one special character
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|;':\",./<>?].*")) {
            buildMessage(context, "Password must contain at least one special character");
            return false;
        }

        // Rule 6: No spaces allowed
        if (password.contains(" ")) {
            buildMessage(context, "Password must not contain spaces");
            return false;
        }

        return true;
    }

    private void buildMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
