package io.github.project;

import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class J8583MessageTest {

    @Test
    void test() {
        // Given
        var expectedJ8583Message = new J8583Message.Builder().mti("1200").bitmap(new Bitmap(Collections.emptyList())).dataFields(Collections.emptyList()).build();
        var j8583Message = new J8583Message.Builder().build();
        var messageToParse = DatatypeConverter.parseHexBinary("120000000000");

        // When
        var actualJ8583Message = j8583Message.parse(messageToParse);

        // Then
        Assertions.assertEquals(expectedJ8583Message, actualJ8583Message);
    }
}
