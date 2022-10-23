package io.github.project;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class J8583MessageTest {

    @Test
    void test() {
        // Given
        var j8583Message = new J8583Message.Builder().build();
        var inputString = "1200" + "00000000";
        var messageToParse = inputString.getBytes(StandardCharsets.UTF_8);

        // When
        var actualJ8583Message = j8583Message.parse(messageToParse);

        // Then
        var expectedJ8583Message = new J8583Message.Builder().mti("1200").bitmap(new Bitmap(Collections.emptyList())).dataFields(Collections.emptyList()).build();
        assertThat(actualJ8583Message.toString(), equalTo(expectedJ8583Message.toString()));
    }

    @Test
    void shouldParse() {
        // Given
        var j8583Message = new J8583Message.Builder().build();
        var inputString = "1200" + "40000000" + "1".repeat(19);
        var messageToParse = inputString.getBytes(StandardCharsets.UTF_8);

        // When
        var actualJ8583Message = j8583Message.parse(messageToParse);

        // Then
        var expectedJ8583Message = new J8583Message.Builder().mti("1200").bitmap(new Bitmap(List.of(2)))
                .dataFields(Collections.singletonList(new DataField(IsoDataField.PRIMARY_ACCOUNT_NUMBER, "1".repeat(19)))).build();
        assertThat(actualJ8583Message, equalTo(expectedJ8583Message));
    }

    @Test
    void shouldParseBinary() {
        // Given
        var j8583Message = new J8583Message.Builder().build();
        var inputString = "1200" + "08000000" + "50484851";
        var messageToParse = inputString.getBytes(StandardCharsets.UTF_8);

        // When
        var actualJ8583Message = j8583Message.parse(messageToParse);

        // Then
        var expectedJ8583Message = new J8583Message.Builder().mti("1200").bitmap(new Bitmap(List.of(5)))
                .dataFields(Collections.singletonList(new DataField(IsoDataField.BINARY_CODE, "50484851"))).build();
        assertThat(actualJ8583Message, equalTo(expectedJ8583Message));
    }

    @Test
    void shouldParseWith2Fields() {
        // Given
        var j8583Message = new J8583Message.Builder().build();
        var inputString = "1200" + "60000000" + "1".repeat(19) + "2".repeat(12);
        var messageToParse = inputString.getBytes(StandardCharsets.UTF_8);

        // When
        var actualJ8583Message = j8583Message.parse(messageToParse);

        // Then
        var expectedJ8583Message = new J8583Message.Builder().mti("1200").bitmap(new Bitmap(List.of(2, 3)))
                .dataFields(List.of(
                                new DataField(IsoDataField.PRIMARY_ACCOUNT_NUMBER, "1".repeat(19)),
                                new DataField(IsoDataField.PROCESSING_CODE, "2".repeat(6))
                        )
                )
                .build();
        assertThat(actualJ8583Message, equalTo(expectedJ8583Message));
    }

    @Test
    void shouldParseFieldWithVariableLength() {
        // Given
        var j8583Message = new J8583Message.Builder().build();
        var inputString = "1200" + "00000001" + "03" + "HEY";
        byte[] messageToParse = inputString.getBytes(StandardCharsets.UTF_8);

        // When
        var actualJ8583Message = j8583Message.parse(messageToParse);

        // Then
        var expectedJ8583Message = new J8583Message.Builder().mti("1200").bitmap(new Bitmap(List.of(32)))
                .dataFields(List.of(
                                new DataField(IsoDataField.RESERVED, "HEY")
                        )
                )
                .build();
        assertThat(actualJ8583Message, equalTo(expectedJ8583Message));
    }
}
