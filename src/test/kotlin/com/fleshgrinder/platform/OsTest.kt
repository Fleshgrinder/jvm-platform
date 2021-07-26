package com.fleshgrinder.platform

import com.fleshgrinder.junit.clearSystemProperties
import com.fleshgrinder.junit.enumTestsOf
import com.fleshgrinder.junit.generexWithGarbage
import com.fleshgrinder.junit.testsOf
import com.fleshgrinder.junit.toTests
import com.fleshgrinder.junit.withSystemProperties
import com.fleshgrinder.junit.withSystemProperty
import com.fleshgrinder.platform.Os.*
import java.io.File
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.parallel.ResourceLock
import org.junit.jupiter.api.parallel.Resources.SYSTEM_PROPERTIES

private class OsTest {
    fun assertAlias(expected: Os, alias: String) =
        clearSystemProperties("file.separator", "java.vm.name") {
            withSystemProperty("os.name", alias) {
                assertAll(
                    { assertEquals(expected, current(), "current") },
                    { if (expected.toString() == alias) assertEquals(expected, fromString(alias), "fromId") else assertThrows<IllegalArgumentException>("fromId") { fromString(alias) } },
                    { assertEquals(expected, parse(alias), "parse") },
                )
            }
        }

    fun aliasTestsOf(expected: Os, @Language("RegExp") p1: String) = generexWithGarbage(p1).toTests { assertAlias(expected, it) }
    fun aliasTestsOf(expected: Os, @Language("RegExp") p1: String, @Language("RegExp") p2: String) = generexWithGarbage(p1, p2).toTests { assertAlias(expected, it) }

    fun assertExtension(ext: String, getExt: () -> String, withExtString: (String) -> String, withExtFile: (File) -> File) =
        assertAll(
            { assertEquals(ext, getExt(), "getExt") },
            { assertEquals("some/path$ext", withExtString("some/path"), "withExtString") },
            { assertEquals(File("some/path$ext"), withExtFile(File("some/path")), "withExtFile") },
        )

    @Test fun `current Os must be known`() {
        assertNotNull(currentOrNull())
    }

    @Test @ResourceLock(SYSTEM_PROPERTIES) fun `current throws if os system property was forcefully removed by the user`() {
        assertThrows<IllegalStateException> { clearSystemProperties("file.separator", "java.vm.name", "os.name") { current() } }
    }

    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun unknown() =
        testsOf("", "os4000", "Roaix", "rv32imac", "rv64imac") {
            clearSystemProperties("file.separator", "java.vm.name") {
                withSystemProperty("os.name", it) {
                    assertAll(
                        { assertThrows<IllegalStateException>("current") { current() } },
                        { assertThrows<IllegalArgumentException>("fromId") { fromString(it) } },
                        { assertThrows<IllegalArgumentException>("parse") { parse(it) } },
                    )
                }
            }
        }

    @Test @ResourceLock(SYSTEM_PROPERTIES) fun `current does not return WINDOWS if file separator is not a backslash`() =
        withSystemProperties("file.separator" to "/", "os.name" to "linux") {
            assertNotEquals(WINDOWS, current())
        }

    @Test @ResourceLock(SYSTEM_PROPERTIES) fun `current returns WINDOWS if file separator is a backslash`() =
        withSystemProperty("file.separator", "\\") {
            assertEquals(WINDOWS, current())
        }

    @Test @ResourceLock(SYSTEM_PROPERTIES) fun `current does not return Android if OS name is not Linux and VM name is not Dalvik`() =
        withSystemProperties("os.name" to "Windows", "java.vm.name" to "HotSpot") {
            assertNotEquals(ANDROID, current())
        }

    @Test @ResourceLock(SYSTEM_PROPERTIES) fun `current return Android if OS name is Linux and VM name is Dalvik`() =
        clearSystemProperties("file.separator") {
            withSystemProperties("java.vm.name" to "Dalvik", "os.name" to "Linux") {
                assertEquals(ANDROID, current())
            }
        }

    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun names() =
        enumTestsOf<Os> {
            val name = it.name
            clearSystemProperties("file.separator", "java.vm.name") {
                withSystemProperty("os.name", name) {
                    assertAll(
                        { assertEquals(it, current(), "current") },
                        { assertThrows<IllegalArgumentException>("fromId") { fromString(name) } },
                        { assertEquals(it, parse(name), "parse") },
                    )
                }
            }
        }

    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun ids() =
        enumTestsOf<Os> {
            val id = it.toString()
            clearSystemProperties("file.separator", "java.vm.name") {
                withSystemProperty("os.name", id) {
                    assertAll(
                        { assertEquals(it, current(), "current") },
                        { assertEquals(it, fromString(id), "fromId") },
                        { assertEquals(it, parse(id), "parse") },
                    )
                }
            }
        }

    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `AIX aliases`() = aliasTestsOf(AIX, "AIX")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `ANDROID aliases`() = aliasTestsOf(ANDROID, "Android")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `DARWIN aliases`() = aliasTestsOf(DARWIN, "(Apple|Darwin|iOS)", "Mac( ?OS( ?X)?)?")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `DRAGONFLYBSD aliases`() = aliasTestsOf(DRAGONFLYBSD, "DragonFly( ?BSD)?")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `FREEBSD aliases`() = aliasTestsOf(FREEBSD, "Free ?BSD")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `FUCHSIA aliases`() = aliasTestsOf(FUCHSIA, "Fuchsia")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `HAIKU aliases`() = aliasTestsOf(HAIKU, "Haiku")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `HPUX aliases`() = aliasTestsOf(HPUX, "HP ?UX")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `IBMI aliases`() = aliasTestsOf(IBMI, "IBM ?i", "OS ?400")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `ILLUMOS aliases`() = aliasTestsOf(ILLUMOS, "Illum( ?OS)?")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `LINUX aliases`() = aliasTestsOf(LINUX, "linux", "u?nix")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `NETBSD aliases`() = aliasTestsOf(NETBSD, "Net ?BSD")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `OPENBSD aliases`() = aliasTestsOf(OPENBSD, "Open ?BSD")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `PLAN9 aliases`() = aliasTestsOf(PLAN9, "Plan ?9")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `QNX aliases`() = aliasTestsOf(QNX, "QNX", "procnto")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `REDOX aliases`() = aliasTestsOf(REDOX, "Redox")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `SOLARIS aliases`() = aliasTestsOf(SOLARIS, "Solaris", "Sun ?OS")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `VXWORKS aliases`() = aliasTestsOf(VXWORKS, "VxWorks")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `WINDOWS aliases`() = aliasTestsOf(WINDOWS, "W(in(dows)?)?(7|8|10|32|64|XP)")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `ZOS aliases`() = aliasTestsOf(ZOS, "z/?OS")

    @TestFactory fun exeExt() = enumTestsOf<Os>(WINDOWS) { assertExtension("", it::getExecutableExtension, it::withExecutableExtension, it::withExecutableExtension) }
    @Test fun exeExtWindows() = assertExtension(".exe", WINDOWS::getExecutableExtension, WINDOWS::withExecutableExtension, WINDOWS::withExecutableExtension)

    @TestFactory fun linkLibExt() = enumTestsOf<Os>(WINDOWS) { assertExtension(".so", it::getLinkLibraryExtension, it::withLinkLibraryExtension, it::withLinkLibraryExtension) }
    @Test fun linkLibExtWindows() = assertExtension(".lib", WINDOWS::getLinkLibraryExtension, WINDOWS::withLinkLibraryExtension, WINDOWS::withLinkLibraryExtension)

    @TestFactory fun sharedLibExt() = enumTestsOf<Os>(DARWIN, WINDOWS) { assertExtension(".so", it::getSharedLibraryExtension, it::withSharedLibraryExtension, it::withSharedLibraryExtension) }
    @Test fun sharedLibExtWindows() = assertExtension(".dll", WINDOWS::getSharedLibraryExtension, WINDOWS::withSharedLibraryExtension, WINDOWS::withSharedLibraryExtension)
    @Test fun sharedLibExtDarwin() = assertExtension(".dylib", DARWIN::getSharedLibraryExtension, DARWIN::withSharedLibraryExtension, DARWIN::withSharedLibraryExtension)

    @TestFactory fun staticLibExt() = enumTestsOf<Os>(WINDOWS) { assertExtension(".a", it::getStaticLibraryExtension, it::withStaticLibraryExtension, it::withStaticLibraryExtension) }
    @Test fun staticLibExtWindows() = assertExtension(".lib", WINDOWS::getStaticLibraryExtension, WINDOWS::withStaticLibraryExtension, WINDOWS::withStaticLibraryExtension)
}
