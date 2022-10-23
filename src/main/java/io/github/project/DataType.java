package io.github.project;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
    
    public byte[] writeValueToByteArray(final String value){
        return switch (this){
            case NUMERIC, ALPHA  -> value.getBytes(StandardCharsets.UTF_8);
            case BINARY -> convertStringRepresentationOfByteArrayToByteArray(value);
            case LLVAR -> (padded(value.length()) + value).getBytes(StandardCharsets.UTF_8);
        };
    }

    private String padded(final int length){
        return length > 9 ? String.valueOf(length) : "0" + length;
    }

    private byte[] convertStringRepresentationOfByteArrayToByteArray(final String value){
        final List<String> listOfStringRepresentationOfBytes = new ArrayList<>();
        final int length = value.length();

        for (int i = 0; i < length; i += 2) {
            listOfStringRepresentationOfBytes.add(value.substring(i, Math.min(length, i + 2)));
        }

        final byte[] result = new byte[listOfStringRepresentationOfBytes.size()];
        for (int i = 0; i < listOfStringRepresentationOfBytes.size(); i++)
        {
            result[i] = Byte.parseByte(listOfStringRepresentationOfBytes.get(i));
        }

        return result;
    }
}
