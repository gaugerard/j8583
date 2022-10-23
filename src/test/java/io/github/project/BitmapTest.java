package io.github.project;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;

class BitmapTest {

    private static Stream<Arguments> getBitmapToParse() {
        return Stream.of(
                Arguments.of(new byte[]{0x30, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, List.of(3, 4)),
                Arguments.of(new byte[]{0x0, (byte) 0xF8, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0}, List.of(9, 10, 11, 12, 13)),
                Arguments.of(new byte[]{0x0, 0x0, 0x0, 0x40, 0x0, 0x0, 0x0, 0x0}, List.of(26)),
                Arguments.of(new byte[]{0x0, 0x0, 0x0, 0x0, 0x01, 0x0, 0x0, 0x0}, List.of(40)),
                Arguments.of(new byte[]{0x0, 0x0, 0x0, 0x0, 0x0, 0x23, 0x0, 0x0}, List.of(43, 47, 48)),
                Arguments.of(new byte[]{0x0, 0x0, 0x0, 0x0, 0x0, 0x0, (byte) 0xFF, 0x0}, List.of(49, 50, 51, 52, 53, 54, 55, 56)),
                Arguments.of(new byte[]{0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x30}, List.of(59, 60))
        );
    }

    @ParameterizedTest(name = "Should parse a first byte.")
    @MethodSource("getBitmapToParse")
    void shouldParseBitmap(byte[] bitmapToParse, List<Integer> expectedListOfFields) {
        // Given
        var expectedBitmap = new Bitmap(expectedListOfFields);

        // When
        var bitmap = Bitmap.parse(bitmapToParse);

        // Then
        Assertions.assertEquals(expectedBitmap, bitmap);
    }

    @Test
    void shouldConvertBitmapToByteArrayRepresentation() {
        // Given
        var bitmap = new Bitmap(List.of(2, 32));

        // When
        var result = bitmap.toByteArray();

        // Then
        var expectedStringBitmap = "40000001";
        var expectedByteArray = expectedStringBitmap.getBytes(StandardCharsets.UTF_8);
        MatcherAssert.assertThat(result, equalTo(expectedByteArray));
    }
}
