package io.github.project.parse;

import io.github.project.J8583Message;

public class J8583MessageManager {
    public J8583Message parse(final byte[] data) {
        return new J8583Message.Builder().build().parse(data);
    }

    public byte[] toByteArray(final J8583Message j8583Message) {
        return j8583Message.toByteArray();
    }
}
