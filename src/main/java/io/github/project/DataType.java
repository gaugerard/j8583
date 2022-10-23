package io.github.project;

public enum DataType {
    /**
     * <b>NUMERIC</b> : Fixed-width numeric values, padded with zeroes to the left.
     */
    NUMERIC(false, null, "NUMERIC : Fixed-width numeric values, padded with zeroes to the left."),
    /**
     * <b>ALPHA</b> : Fixed-width alphanumeric values, padded with spaces to the right.
     */
    ALPHA(false, null, "ALPHA : Fixed-width alphanumeric values, padded with spaces to the right."),
    /**
     * <b>BINARY</b> : Similar to ALPHA, but stores byte arrays directly instead of text.
     */
    BINARY(false, null, "BINARY : Similar to ALPHA, but stores byte arrays directly instead of text."),
    /**
     * <b>LLVAR</b> : Variable-width alphanumeric value, up to 99 characters long. The length of the value is encoded in the first 2 characters of the value, for example "HEY" is encoded as 03HEY.
     */
    LLVAR(true, 2, "LLVAR : Variable-width alphanumeric value, up to 99 characters long. The length of the value is encoded in the first 2 characters of the value, for example 'HEY' is encoded as 03HEY");

    private final boolean isWidthVariable;
    private final Integer encodedLengthWidth;
    private final String description;

    DataType(final boolean isWidthVariable,
             final Integer encodedLengthWidth,
             final String description){
        this.isWidthVariable = isWidthVariable;
        this.encodedLengthWidth = encodedLengthWidth;
        this.description = description;
    }

    public boolean isWidthVariable() {
        return isWidthVariable;
    }

    public Integer getEncodedLengthWidth() {
        return encodedLengthWidth;
    }

    public String getDescription() {
        return description;
    }
}
