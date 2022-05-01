package io.github.project;

import static io.github.project.DataType.NUMERIC;

public enum IsoType {

    PRIMARY_ACCOUNT_NUMBER(NUMERIC, 2, 19, true, "PAN"),
    PROCESSING_CODE(NUMERIC, 3, 6, false, "PAN");

    private final DataType dataType;
    private final int field;
    private final int length;
    private final boolean sensitive;
    private final String description;

    IsoType(final DataType dataType,
            final int field,
            final int length,
            final boolean sensitive,
            final String description) {
        this.dataType = dataType;
        this.field = field;
        this.length = length;
        this.sensitive = sensitive;
        this.description = description;
    }

    public DataType getDataType() {
        return dataType;
    }

    public int getField() {
        return field;
    }

    public int getLength() {
        return length;
    }

    public boolean isSensitive() {
        return sensitive;
    }

    public String getDescription() {
        return description;
    }
}
