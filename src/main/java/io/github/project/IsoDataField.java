package io.github.project;

import static io.github.project.DataType.*;

public enum IsoDataField {

    PRIMARY_ACCOUNT_NUMBER(NUMERIC, 2, 19, true, "PAN"),
    PROCESSING_CODE(NUMERIC, 3, 6, false, "Processing Code"),
    BINARY_CODE(BINARY, 5, 8, false, "Binary Test Code"),
    RESERVED(LLVAR, 32, 99, false, "Reserved");

    private final DataType dataType;
    private final int field;
    private final int length;
    private final boolean sensitive;
    private final String description;

    IsoDataField(final DataType dataType,
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

    public byte[] writeValueToByteArray(final String value){
        return dataType.writeValueToByteArray(value);
    }

    @Override
    public String toString() {
        return "IsoDataField{" +
                "dataType=" + dataType +
                "field=" + field +
                "length=" + length +
                "sensitive=" + sensitive +
                "description=" + description +
                '}';
    }
}
