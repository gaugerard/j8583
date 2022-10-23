package io.github.project.parse;

import io.github.project.Bitmap;
import io.github.project.DataField;
import io.github.project.IsoDataField;
import io.github.project.J8583Message;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

class J8583MessageManagerTest {

    @Test
    void shouldConvertJ8583MessageWithFixWidthElementToByteArray() {
        // Given
        var bitmap = new Bitmap(List.of(3));
        var dataFields = new DataField(IsoDataField.PROCESSING_CODE, "123456");
        var j8583Message = new J8583Message.Builder()
                .mti("1200")
                .bitmap(bitmap)
                .dataFields(List.of(dataFields))
                .build();

        var j8583MessageManager = new J8583MessageManager();

        // When
        var result = j8583MessageManager.toByteArray(j8583Message);

        // Then
        var expectedMessageString = "1200" + "20000000" + "123456";
        var expectedByteArray = expectedMessageString.getBytes(StandardCharsets.UTF_8);
        MatcherAssert.assertThat(result, equalTo(expectedByteArray));
    }

    @Test
    void shouldConvertJ8583MessageWithFixWidthBinaryElementToByteArray() {
        // Given
        var bitmap = new Bitmap(List.of(5));
        var dataFields = new DataField(IsoDataField.BINARY_CODE, "48505050");
        var j8583Message = new J8583Message.Builder()
                .mti("1200")
                .bitmap(bitmap)
                .dataFields(List.of(dataFields))
                .build();

        var j8583MessageManager = new J8583MessageManager();

        // When
        var result = j8583MessageManager.toByteArray(j8583Message);

        // Then
        var expectedMessageString = "1200" + "08000000";
        var expectedMtiBitmapByteArray = expectedMessageString.getBytes(StandardCharsets.UTF_8);
        var expectedBinaryCode = new byte[]{48, 50, 50, 50};
        var expectedByteArray = new byte[expectedMtiBitmapByteArray.length + expectedBinaryCode.length];
        final ByteBuffer byteBuffer = ByteBuffer.wrap(expectedByteArray);
        byteBuffer.put(expectedMtiBitmapByteArray);
        byteBuffer.put(expectedBinaryCode);
        MatcherAssert.assertThat(result, equalTo(expectedByteArray));
    }

    @Test
    void shouldParseByteArrayToJ8583MessageWithFixWidthElement() {
        // Given
        var messageString = "1200" + "20000000" + "123456";
        var byteArray = messageString.getBytes(StandardCharsets.UTF_8);

        var j8583MessageManager = new J8583MessageManager();

        // When
        var result = j8583MessageManager.parse(byteArray);

        // Then
        var expectedBitmap = new Bitmap(List.of(3));
        var expectedDataFields = new DataField(IsoDataField.PROCESSING_CODE, "123456");
        var expectedJ8583Message = new J8583Message.Builder()
                .mti("1200")
                .bitmap(expectedBitmap)
                .dataFields(List.of(expectedDataFields))
                .build();

        MatcherAssert.assertThat(result, equalTo(expectedJ8583Message));
    }

    @Test
    void shouldConvertJ8583MessageWithVariableWidthElementToByteArray() {
        // Given
        var bitmap = new Bitmap(List.of(32));
        var dataFields = new DataField(IsoDataField.RESERVED, "12345");
        var j8583Message = new J8583Message.Builder()
                .mti("1200")
                .bitmap(bitmap)
                .dataFields(List.of(dataFields))
                .build();

        var j8583MessageManager = new J8583MessageManager();

        // When
        var result = j8583MessageManager.toByteArray(j8583Message);

        // Then
        var expectedMessageString = "1200" + "00000001" + "05" + "12345";
        var expectedByteArray = expectedMessageString.getBytes(StandardCharsets.UTF_8);
        MatcherAssert.assertThat(result, equalTo(expectedByteArray));
    }

    @Test
    void shouldConvertComplexJ8583MessageToByteArray() {
        // Given
        var bitmap = new Bitmap(List.of(2, 3, 5, 32));
        var dataFields = List.of(
                new DataField(IsoDataField.PRIMARY_ACCOUNT_NUMBER, "4200000000000500016"),
                new DataField(IsoDataField.PROCESSING_CODE, "000000"),
                new DataField(IsoDataField.BINARY_CODE, "50484848"),
                new DataField(IsoDataField.RESERVED, "9".repeat(5))
        );
        var j8583Message = new J8583Message.Builder()
                .mti("0800")
                .bitmap(bitmap)
                .dataFields(dataFields)
                .build();

        var j8583MessageManager = new J8583MessageManager();

        // When
        var result = j8583MessageManager.toByteArray(j8583Message);

        // Then
        var expectedMtiBitmapPanAndProcessingCodeString = "0800" + "68000001" +
                "4200000000000500016" +
                "000000";
        var expectedMtiBitmapPanAndProcessingCode = expectedMtiBitmapPanAndProcessingCodeString.getBytes(StandardCharsets.UTF_8);
        var expectedBinaryCode = new byte[]{50, 48, 48, 48};
        var expectedReservedString = "05" + "9".repeat(5);
        var expectedReserved = expectedReservedString.getBytes(StandardCharsets.UTF_8);
        var expectedByteArray = new byte[expectedMtiBitmapPanAndProcessingCode.length + expectedBinaryCode.length + expectedReserved.length];

        final ByteBuffer byteBuffer = ByteBuffer.wrap(expectedByteArray);
        byteBuffer.put(expectedMtiBitmapPanAndProcessingCode);
        byteBuffer.put(expectedBinaryCode);
        byteBuffer.put(expectedReserved);
        MatcherAssert.assertThat(result, equalTo(expectedByteArray));
    }

    @Test
    void shouldParseByteArrayToJ8583MessageWithVariableWidthElement() {
        // Given
        var messageString = "1200" + "00000001" + "05" + "12345";
        var byteArray = messageString.getBytes(StandardCharsets.UTF_8);

        var j8583MessageManager = new J8583MessageManager();

        // When
        var result = j8583MessageManager.parse(byteArray);

        // Then
        var expectedBitmap = new Bitmap(List.of(32));
        var expectedDataFields = new DataField(IsoDataField.RESERVED, "12345");
        var expectedJ8583Message = new J8583Message.Builder()
                .mti("1200")
                .bitmap(expectedBitmap)
                .dataFields(List.of(expectedDataFields))
                .build();

        MatcherAssert.assertThat(result, equalTo(expectedJ8583Message));
    }
}
