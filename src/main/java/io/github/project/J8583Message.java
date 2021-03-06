package io.github.project;

import jakarta.xml.bind.DatatypeConverter;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public record J8583Message(Bitmap bitmap, List<DataField> dataFields, String mti) {

    private static final int MTI_LENGTH = 2;
    private static final int BITMAP_LENGTH = 4;

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
        return DatatypeConverter.printHexBinary(data);
    }

    private Bitmap parseBitmap(final ByteBuffer buffer) {
        final byte[] data = new byte[BITMAP_LENGTH];
        buffer.get(data);
        return Bitmap.parse(data);
    }

    private List<DataField> parseDataFields(final ByteBuffer buffer, final Bitmap bitmap) {
        final int length = buffer.remaining();
        final byte[] data = new byte[length];
        final ByteBuffer dataFieldsByteBuffer = ByteBuffer.wrap(data);

        final List<IsoType> isoTypes = getIsoTypesFromBitmap(bitmap);
        return isoTypes.stream().map(isoType -> readDataFieldFromData(dataFieldsByteBuffer, isoType)).toList();
    }

    private List<IsoType> getIsoTypesFromBitmap(final Bitmap bitmap) {
        return Arrays.stream(IsoType.values()).sorted().filter(isoType -> bitmap.getFields().contains(isoType.getField())).toList();
    }

    private DataField readDataFieldFromData(final ByteBuffer buffer, final IsoType isoType) {
        final int length = isoType.getLength();
        final byte[] data = new byte[length];
        final ByteBuffer bitMapByteBuffer = buffer.get(data);

        return new DataField(isoType, bitMapByteBuffer.toString());
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
