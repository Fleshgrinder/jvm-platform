package com.fleshgrinder.platform;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("ResultOfMethodCallIgnored") final class UtilsTest {
    @Test void id() {
        assertAll(
            () -> assertThrows(StringIndexOutOfBoundsException.class, () -> Utils.id("")),
            () -> assertEquals(
                "0123456789-abcdefghijklmnopqrstuvwxyz-abcdefghijklmnopqrstuvwxyz",
                Utils.id("0123456789_ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz")
            )
        );
    }

    @Test void normalize() {
        assertAll(
            () -> assertEquals("", Utils.normalize("")),
            () -> assertEquals(
                "-0123456789--abcdefghijklmnopqrstuvwxyz--abcdefghijklmnopqrstuvwxyz-",
                Utils.normalize("/0123456789:@ABCDEFGHIJKLMNOPQRSTUVWXYZ[`abcdefghijklmnopqrstuvwxyz{")
            ),
            () -> assertEquals(
                "0123456789abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz",
                Utils.normalize("/0123456789:@ABCDEFGHIJKLMNOPQRSTUVWXYZ[`abcdefghijklmnopqrstuvwxyz{", true)
            )
        );
    }
}
