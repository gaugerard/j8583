package io.github.project;

import jakarta.xml.bind.DatatypeConverter;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public record J8583Message(Bitmap bitmap, List<DataField> dataFields, String mti) {

    private static final int MTI_LENGTH = 4;
    private static final int BITMAP_LENGTH = 8;

    public J8583Message parse(final byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data, 0, data.length);

        final String mti = parseMti(buffer);
        final Bitmap bitmap = parseBitmap(buffer);
        final List<DataField> dataFields = parseDataFields(buffer, bitmap);

        return new Builder()
                .mti(mti)
                .bitmap(bitmap)
                .dataFields(dataFields)
                .build();
    }

    private String parseMti(final ByteBuffer buffer) {
        final byte[] data = new byte[MTI_LENGTH];
        buffer.get(data);
        return new String(data, StandardCharsets.UTF_8);
    }

    private Bitmap parseBitmap(final ByteBuffer buffer) {
        final byte[] data = new byte[BITMAP_LENGTH];
        buffer.get(data);

        final String hexBinaryString = new String(data, StandardCharsets.UTF_8);
        final byte[] dataHexBinaryByteFormat = DatatypeConverter.parseHexBinary(hexBinaryString);

        return Bitmap.parse(dataHexBinaryByteFormat);
    }

    private static byte[] convertToHexBinaryByteFormat(final byte[] data) {
        final String hexBinaryString = new String(data, StandardCharsets.UTF_8);
        return DatatypeConverter.parseHexBinary(hexBinaryString);
    }

    private List<DataField> parseDataFields(final ByteBuffer buffer, final Bitmap bitmap) {
        final List<IsoDataField> isoDataFields = getIsoTypesFromBitmap(bitmap);
        return isoDataFields.stream().map(isoDataField -> readDataFieldFromData(buffer, isoDataField)).toList();
    }

    private List<IsoDataField> getIsoTypesFromBitmap(final Bitmap bitmap) {
        return Arrays.stream(IsoDataField.values()).sorted().filter(isoDataField -> bitmap.getFields().contains(isoDataField.getField())).toList();
    }

    private DataField readDataFieldFromData(final ByteBuffer buffer, final IsoDataField isoDataField) {
        return switch (isoDataField.getDataType()) {
            case ALPHA, NUMERIC, BINARY -> readFixedWidthDataFieldFromData(buffer, isoDataField);
            case LLVAR -> readVariableWidthDataFieldFromData(buffer, isoDataField);
        };
    }

    private String convertByteArrayToUTF8String(final ByteBuffer buffer, final int length) {
        final byte[] data = new byte[length];
        buffer.get(data);
        return new String(data, StandardCharsets.UTF_8);
    }

    private DataField readFixedWidthDataFieldFromData(final ByteBuffer buffer, final IsoDataField isoDataField) {
        final int length = isoDataField.getLength() * 2;

        final String isoDataFieldValueStr = convertByteArrayToUTF8String(buffer, length);

        return new DataField(isoDataField, isoDataFieldValueStr);
    }

    private DataField readVariableWidthDataFieldFromData(final ByteBuffer buffer, final IsoDataField isoDataField) {
        final Integer encodedLengthWidth = isoDataField.getDataType().getEncodedLengthWidth();
        final int length = readEncodedLengthWidthFromData(buffer, encodedLengthWidth);

        final String isoDataFieldValueStr = convertByteArrayToUTF8String(buffer, length);

        return new DataField(isoDataField, isoDataFieldValueStr);
    }

    private int readEncodedLengthWidthFromData(final ByteBuffer buffer, final Integer length) {
        final String encodedLengthWidth = convertByteArrayToUTF8String(buffer, length);

        return Integer.parseInt(encodedLengthWidth);
    }

    public static class Builder {
        private Bitmap bitmap;
        private List<DataField> dataFields;
        private String mti;

        public Builder bitmap(final Bitmap bitmap) {
            this.bitmap = bitmap;
            return this;
        }

        public Builder dataFields(final List<DataField> dataFields) {
            this.dataFields = dataFields;
            return this;
        }

        public Builder mti(final String mti) {
            this.mti = mti;
            return this;
        }

        public J8583Message build() {
            return new J8583Message(bitmap, dataFields, mti);
        }

    }
}