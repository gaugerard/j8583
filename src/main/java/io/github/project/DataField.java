package io.github.project;

public class DataField {
    private final IsoType isoType;
    private final String value;

    DataField(final IsoType isoType,
              final String value) {
        this.isoType = isoType;
        this.value = value;
    }
}
