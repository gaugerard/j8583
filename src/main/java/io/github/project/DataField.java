package io.github.project;

public record DataField(IsoDataField isoDataField, String value) {

    @Override
    public String toString() {
        return "DataField{" +
                "isoDataField=" + isoDataField +
                "value=" + value +
                '}';
    }

}
