package com.ironz.binaryprefs.serialization.serializer;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringSerializerImplTest {

    private static final byte INCORRECT_FLAG = 0;

    private final StringSerializer serializer = new StringSerializer();

    @Test
    public void stringConvert() {
        String value = "Some String";

        byte[] bytes = serializer.serialize(value);
        String restored = serializer.deserialize(bytes);

        assertTrue(serializer.isMatches(bytes[0]));
        assertEquals(serializer.bytesLength(), 1);
        assertEquals(value, restored);
    }

    @Test
    public void stringIncorrectFlag() {
        String value = "Some String";

        byte[] bytes = serializer.serialize(value);
        bytes[0] = INCORRECT_FLAG;

        assertFalse(serializer.isMatches(bytes[0]));
    }
}