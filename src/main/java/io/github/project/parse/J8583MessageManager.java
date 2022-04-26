package io.github.project.parse;

import io.github.project.J8583Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class J8583MessageManager {
    public J8583Message parse(final byte[] data) {
        return new J8583Message.Builder().build();
    }

    public byte[] toByteArray() throws IOException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        return buffer.toByteArray();
    }
}
