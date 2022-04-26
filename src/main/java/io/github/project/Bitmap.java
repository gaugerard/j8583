package io.github.project;

import java.util.ArrayList;
import java.util.List;

public class Bitmap {

    private final List<Integer> fields;

    Bitmap(final List<Integer> fields) {
        this.fields = fields;
    }

    public static Bitmap parse(final byte[] data) {
        final List<Integer> fields = new ArrayList<Integer>();
        for (var index = 0; index < data.length; index++) {
            final byte bits = data[index];
            fields.addAll(parse(index, bits));
        }
        return new Bitmap(fields);
    }

    private static List<Integer> parse(final int index, final byte bits) {
        // bits = 8 bit
        final String bitsAsString = Integer.toBinaryString(bits);
        final String aaa = String.format("%8s", Integer.toBinaryString(bits)).replace(' ', '0');

        final List<Integer> fields = new ArrayList<Integer>();
        for (int i = 0; i < bitsAsString.length(); i++) {
            final char bit = bitsAsString.charAt(i);
            if (bit == '1') {
                fields.add(computeField(index, i));
            }
        }
        return fields;
    }

    private static Integer computeField(final int byteIndex, final int bitIndex) {
        return (byteIndex * 8) + bitIndex;
    }

    public List<Integer> getFields() {
        return fields;
    }

    public void addField(final Integer field) {
        this.fields.add(field);
    }
}
