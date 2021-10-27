package com.psu.devboards.dbapi.utils;

public class NameUniqueViolationException extends RuntimeException {
    public NameUniqueViolationException() {
        super();
    }

    @Override
    public String getMessage() {
        return "An organization with this name already exists!";
    }
}
