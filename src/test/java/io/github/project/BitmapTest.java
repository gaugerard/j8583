package io.github.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BitmapTest {

    @Test
    @DisplayName("Should parse a first bit.")
    void shouldParseFirstBit() {
        // Given
        var bitmapToParse = new byte[]{0x30, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};

        // When
        var bitmap = Bitmap.parse(bitmapToParse);

        // Then
        System.out.println("b");
    }
}
