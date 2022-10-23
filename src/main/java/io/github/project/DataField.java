package io.github.project;

public record DataField(IsoDataField isoDataField, String value) {

    public byte[] toByteArray(){
        return isoDataField.writeValueToByteArray(value);
    }
}
