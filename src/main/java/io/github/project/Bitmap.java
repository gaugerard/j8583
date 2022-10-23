package io.github.project;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record Bitmap(List<Integer> fields) {

    private static final char FIELD_PRESENT = '1';
    private static final char FIELD_MISSING = '0';
    private static final String BYTE_FORMAT = "%8s";

    public static Bitmap parse(final byte[] data) {
        final List<Integer> fields = new ArrayList<Integer>();
        for (var index = 0; index < data.length; index++) {
            final byte bits = data[index];
            fields.addAll(parse(index, bits));
        }
        return new Bitmap(Collections.unmodifiableList(fields));
    }

    private static List<Integer> parse(final int index, final byte bits) {
        // bits = 8 bit
        final String bitsAsString = String.format(BYTE_FORMAT, Integer.toBinaryString(bits & 0xff)).replace(' ', '0');
        final List<Integer> fields = new ArrayList<>();
        for (int i = 0; i < bitsAsString.length(); i++) {
            final char bit = bitsAsString.charAt(i);
            if (FIELD_PRESENT == bit) {
                fields.add(computeField(index, i));
            }
        }
        return fields;
    }

    private static Integer computeField(final int byteIndex, final int bitIndex) {
        return (byteIndex * 8) + (bitIndex + 1);
    }

    public List<Integer> getFields() {
        return fields;
    }

    public void addField(final Integer field) {
        this.fields.add(field);
    }

    public byte[] toByteArray(){
        final StringBuilder bitmapStringBuilder = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            StringBuilder byteStringBuilder = new StringBuilder();

            for (int j = 0; j < 4; j++){
                final int fieldIndex = (j + 1) + (i * 4);
                if (this.fields.contains(fieldIndex)) {
                    byteStringBuilder.append(FIELD_PRESENT);
                } else {
                    byteStringBuilder.append(FIELD_MISSING);
                }
            }
            final int value = Integer.parseInt(byteStringBuilder.toString(), 2);
            bitmapStringBuilder.append(value);
        }
        return bitmapStringBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }
}
