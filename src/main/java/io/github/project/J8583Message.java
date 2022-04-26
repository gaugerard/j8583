package io.github.project;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class J8583Message {

    private static final int MTI_LENGTH = 4;
    private static final int BITMAP_LENGTH = 8;

    private final Bitmap bitmap;
    private final List<DataField> dataFields;
    private final String mti;

    J8583Message(final Bitmap bitmap,
                 final List<DataField> dataFields,
                 final String mti) {
        this.bitmap = bitmap;
        this.dataFields = dataFields;
        this.mti = mti;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public List<DataField> getDataFields() {
        return dataFields;
    }

    public String getMti() {
        return mti;
    }

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
        final ByteBuffer bitMapByteBuffer = buffer.get(data);
        return "0100";
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
        return isoTypes.stream().map(isoType -> readDataFieldFromData(dataFieldsByteBuffer, isoType)).collect(Collectors.toUnmodifiableList());
    }

    private List<IsoType> getIsoTypesFromBitmap(final Bitmap bitmap) {
        return Arrays.stream(IsoType.values()).sorted().filter(isoType -> bitmap.getFields().contains(isoType.getField())).collect(Collectors.toUnmodifiableList());
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
