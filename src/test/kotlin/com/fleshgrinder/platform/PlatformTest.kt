package com.fleshgrinder.platform

import com.fleshgrinder.junit.generexWithGarbage
import com.fleshgrinder.junit.toTests
import com.fleshgrinder.junit.withSystemProperties
import com.fleshgrinder.platform.Arch.ARM_64
import com.fleshgrinder.platform.Arch.X86_32
import com.fleshgrinder.platform.Arch.X86_64
import com.fleshgrinder.platform.Os.ANDROID
import com.fleshgrinder.platform.Os.DARWIN
import com.fleshgrinder.platform.Os.LINUX
import com.fleshgrinder.platform.Os.WINDOWS
import com.fleshgrinder.platform.Platform.current
import com.fleshgrinder.platform.Platform.currentOrNull
import com.fleshgrinder.platform.Platform.fromString
import com.fleshgrinder.platform.Platform.fromStringOrNull
import com.fleshgrinder.platform.Platform.parse
import com.fleshgrinder.platform.Platform.parseOrNull
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.TreeSet
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.parallel.ResourceLock
import org.junit.jupiter.api.parallel.Resources.SYSTEM_PROPERTIES

private class PlatformTest {
    @Test fun `current Platform must be known`() {
        assertNotNull(currentOrNull())
    }

    @Test fun `Platform can be serialized and deserialized`() {
        val expected = Platform(LINUX, X86_64)
        val buf = ByteArrayOutputStream()
        ObjectOutputStream(buf).use { it.writeObject(expected) }
        val actual = ObjectInputStream(ByteArrayInputStream(buf.toByteArray())).use { it.readObject() }
        assertEquals(expected, actual)
    }

    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun ids() = sequence {
        Os.values().forEach { os ->
            Arch.values().forEach { arch ->
                yield(Platform(os, arch))
            }
        }
    }.toTests {
        withSystemProperties(
            "file.separator" to if (it.os == WINDOWS) "\\" else "/",
            "java.vm.name" to if (it.os == ANDROID) "Dalvik" else "unknown",
            "os.arch" to it.arch.toString(),
            "os.name" to it.os.toString(),
        ) {
            val id = it.toString()
            assertAll(
                { assertEquals(it, current(), "current") },
                { assertEquals(it, currentOrNull(), "currentOrNull") },
                { assertEquals(it, fromString(id), "fromString") },
                { assertEquals(it, fromStringOrNull(id), "fromStringOrNull") },
                { assertEquals(it, parse(id), "parse") },
                { assertEquals(it, parseOrNull(id), "parseOrNull") },
            )
        }
    }

    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `invalid input`() =
        (sequenceOf("") + generexWithGarbage("-{1,4}", "a( ?b( ?c( ?d)?)?)?", "unknown( ?unknown){1,3}")).toTests {
            withSystemProperties("file.separator" to "/", "java.vm.name" to "unknown", "os.arch" to it, "os.name" to it) {
                assertAll(
                    { assertThrows<IllegalStateException>("current") { current() } },
                    { assertNull(currentOrNull(), "currentOrNull") },
                    { assertThrows<IllegalArgumentException>("fromString") { fromString(it) } },
                    { assertNull(fromStringOrNull(it), "fromStringOrNull") },
                    { assertThrows<IllegalArgumentException>("parse") { parse(it) } },
                    { assertNull(parseOrNull(it), "parseOrNull") },
                )
            }
        }

    @Test
    @ResourceLock(SYSTEM_PROPERTIES)
    fun `input with invalid Arch fails`() {
        val os = "linux"
        val arch = "unknown-64"
        val id = "$os-$arch"
        withSystemProperties("file.separator" to "/", "java.vm.name" to "unknown", "os.arch" to arch, "os.name" to os) {
            assertAll(
                { assertThrows<IllegalStateException>("current") { current() } },
                { assertNull(currentOrNull(), "currentOrNull") },
                { assertThrows<IllegalArgumentException>("fromString") { fromString(id) } },
                { assertNull(fromStringOrNull(id), "fromStringOrNull") },
                { assertThrows<IllegalArgumentException>("parse") { parse(id) } },
                { assertNull(parseOrNull(id), "parseOrNull") },
            )
        }
    }

    @Test
    @ResourceLock(SYSTEM_PROPERTIES)
    fun `input with invalid Os fails`() {
        val os = "unknown"
        val arch = "x86-64"
        val id = "$os-$arch"
        withSystemProperties("file.separator" to "/", "java.vm.name" to "unknown", "os.arch" to arch, "os.name" to os) {
            assertAll(
                { assertThrows<IllegalStateException>("current") { current() } },
                { assertNull(currentOrNull(), "currentOrNull") },
                { assertThrows<IllegalArgumentException>("fromString") { fromString(id) } },
                { assertNull(fromStringOrNull(id), "fromStringOrNull") },
                { assertThrows<IllegalArgumentException>("parse") { parse(id) } },
                { assertNull(parseOrNull(id), "parseOrNull") },
            )
        }
    }

    @Test fun compare() {
        val a = Platform(DARWIN, ARM_64)
        val b = Platform(LINUX, X86_32)
        val c = Platform(WINDOWS, X86_64)
        val sorted = TreeSet<Platform>().apply {
            add(c)
            add(a)
            add(b)
        }.toArray()

        assertAll(
            { Assertions.assertSame(a, sorted[0]) },
            { Assertions.assertSame(b, sorted[1]) },
            { Assertions.assertSame(c, sorted[2]) },
        )
    }

    @Test fun `equals and hashCode`() {
        EqualsVerifier.forClass(Platform::class.java)
            .withNonnullFields("os", "arch", "id")
            .withOnlyTheseFields("id")
            .verify()
    }
}
