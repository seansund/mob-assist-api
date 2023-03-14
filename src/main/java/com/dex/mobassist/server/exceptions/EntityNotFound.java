package com.dex.mobassist.server.exceptions;

public class EntityNotFound extends RuntimeException {
    private final String type;
    private final String field;
    private final String fieldValue;

    public EntityNotFound(String type, String id) {
        this(type, "id", id);
    }

    public EntityNotFound(String type, String field, String id) {
        super(String.format("%s not found for %s: %s", type, field, id));

        this.type = type;
        this.field = field;
        this.fieldValue = id;
    }

    public String getType() {
        return type;
    }

    public String getField() {
        return field;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
