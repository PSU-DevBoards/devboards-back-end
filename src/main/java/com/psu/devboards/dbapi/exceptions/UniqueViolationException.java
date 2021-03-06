package com.psu.devboards.dbapi.exceptions;

/**
 * Exception representing a unique database column restraint violation.
 */
public class UniqueViolationException extends RuntimeException {

    private final String entity;
    private final String field;

    public UniqueViolationException(String entity, String field) {
        super();

        this.entity = entity;
        this.field = field;
    }

    @Override
    public String getMessage() {
        return "An " + entity + " with this " + field + " already exists.";
    }
}
