package com.fleshgrinder.platform

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

private class UtilsTest {
    @Test fun `id throws exception if string is empty`() {
        assertThrows<StringIndexOutOfBoundsException> { Utils.id("") }
    }

    @Test fun `id turns string into lower-dash-case`() {
        assertEquals(
            "0123456789-abcdefghijklmnopqrstuvwxyz-abcdefghijklmnopqrstuvwxyz",
            Utils.id("0123456789_ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz"),
        )
    }

    @Test fun `normalize returns empty string if string is empty`() {
        assertEquals("", Utils.normalize(""))
    }

    @Test fun `normalize replaces all non-alphanumeric chars with dashes if strip is false`() {
        assertEquals(
            "-0123456789--abcdefghijklmnopqrstuvwxyz--abcdefghijklmnopqrstuvwxyz-",
            Utils.normalize("/0123456789:@ABCDEFGHIJKLMNOPQRSTUVWXYZ[`abcdefghijklmnopqrstuvwxyz{"),
        )
    }

    @Test fun `normalize removes all non-alphanumeric chars if strip is true`() {
        assertEquals(
            "0123456789abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz",
            Utils.normalize("/0123456789:@ABCDEFGHIJKLMNOPQRSTUVWXYZ[`abcdefghijklmnopqrstuvwxyz{", true),
        )
    }
}
