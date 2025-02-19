package com.tcs.admin.catalog.domain.validation.handler;

import com.tcs.admin.catalog.domain.exceptions.DomainException;
import com.tcs.admin.catalog.domain.validation.Error;
import com.tcs.admin.catalog.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(final Error anError) {
        throw DomainException.with(List.of(anError));
    }

    @Override
    public ValidationHandler append(final ValidationHandler aHandler) {
        throw DomainException.with(aHandler.getErrors());
    }

    @Override
    public <T> T validate(final Validation<T> aValidation) {
        try {
            return aValidation.validate();
        } catch (final Exception ex) {
            throw DomainException.with(List.of(new Error(ex.getMessage())));
        }
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}
