package io.github.project;

import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;

class J8583MessageTest {

    @Test
    void test() {
        // Given
        var expectedJ8583Message = new J8583Message.Builder().mti("1200").bitmap(new Bitmap(Collections.emptyList())).dataFields(Collections.emptyList()).build();
        var j8583Message = new J8583Message.Builder().build();
        var messageToParse = DatatypeConverter.parseHexBinary("1200" + "00000000" + "02" + "1".repeat(38));

        // When
        var actualJ8583Message = j8583Message.parse(messageToParse);

        // Then
        assertThat(actualJ8583Message.toString(), equalTo(expectedJ8583Message.toString()));
    }

    @Test
    void shouldParse() {
        // Given
        var j8583Message = new J8583Message.Builder().build();
        var messageToParse = DatatypeConverter.parseHexBinary("1200" + "40000000" + "02" + "1".repeat(38));
        // When
        var actualJ8583Message = j8583Message.parse(messageToParse);

        // Then
        var expectedJ8583Message = new J8583Message.Builder().mti("1200").bitmap(new Bitmap(List.of(2)))
                .dataFields(Collections.singletonList(new DataField(IsoDataField.PRIMARY_ACCOUNT_NUMBER, "1".repeat(38)))).build();
        assertThat(actualJ8583Message, equalTo(expectedJ8583Message));
    }

    @Test
    void shouldParseWith2Fields() {
        // Given
        var j8583Message = new J8583Message.Builder().build();
        var messageToParse = DatatypeConverter.parseHexBinary("1200" + "60000000" + "02" + "1".repeat(38)+ "03" + "2".repeat(12));
        // When
        var actualJ8583Message = j8583Message.parse(messageToParse);

        // Then
        var expectedJ8583Message = new J8583Message.Builder().mti("1200").bitmap(new Bitmap(List.of(2, 3)))
                .dataFields(List.of(
                        new DataField(IsoDataField.PRIMARY_ACCOUNT_NUMBER, "1".repeat(38)),
                        new DataField(IsoDataField.PROCESSING_CODE, "2".repeat(12))
                        )
                )
                .build();

        assertThat(actualJ8583Message, equalTo(expectedJ8583Message));
    }
}
