package io.github.project;

import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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
        var expectedJ8583Message = new J8583Message.Builder().mti("1200").bitmap(new Bitmap(Collections.emptyList()))
                .dataFields(Collections.singletonList(new DataField(IsoDataField.PRIMARY_ACCOUNT_NUMBER, "1".repeat(38)))).build();
        var j8583Message = new J8583Message.Builder().build();
        var messageToParse = DatatypeConverter.parseHexBinary("1200" + "4000000000000000" + "02" + "1".repeat(38));

        // When
        var actualJ8583Message = j8583Message.parse(messageToParse);

        // Then
        assertThat(actualJ8583Message.toString(), equalTo(expectedJ8583Message.toString()));
    }
}
