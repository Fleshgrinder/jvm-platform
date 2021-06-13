package com.fleshgrinder.platform;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junitpioneer.jupiter.SetSystemProperty;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SuppressWarnings("ResultOfMethodCallIgnored")
final class OsTest {
    private static void assertOs(
        final @NotNull String fs,
        final @Nullable String os,
        final @NotNull Executable exe
    ) throws Throwable {
        final @NotNull String fsOrig = System.getProperty("file.separator");
        final @NotNull String osOrig = System.getProperty("os.name");
        System.setProperty("file.separator", fs);
        System.setProperty("os.name", os == null ? "unknown" : os);
        try {
            exe.execute();
        } finally {
            System.setProperty("file.separator", fsOrig);
            System.setProperty("os.name", osOrig);
        }
    }

    private static void assertOs(
        final @Nullable String os,
        final @NotNull Executable exe
    ) throws Throwable {assertOs("/", os, exe);}

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
        "    ",
        "\t\n\t\n",
        "rv32imac",
        "rv64imac",
        "os4000",
    })
    void failure(final @NotNull String os) throws Throwable {
        assertOs(os, () -> assertThrows(UnsupportedPlatformException.class, Os::current));
    }

    @ParameterizedTest
    @CsvSource({
        "AIX, AIX",
        "ANDROID, Android",
        "DARWIN, Darwin",
        "DARWIN, Mac OS X",
        "DARWIN, Mac OS",
        "DRAGONFLYBSD, dragonflybsd",
        "FREEBSD, freebsd",
        "FUCHSIA, fuchsia",
        "HAIKU, haiku",
        "HPUX, hpux",
        "IBMI, ibmi",
        "IBMI, os400",
        "ILLUMOS, illumos",
        "LINUX, linux",
        "NETBSD, netbsd",
        "OPENBSD, openbsd",
        "PLAN9, plan9",
        "QNX, procnto",
        "QNX, qnx",
        "REDOX, redox",
        "SOLARIS, solaris",
        "SOLARIS, sunos",
        "VXWORKS, vxworks",
        "ZOS, zos",

        "AIX, AIX",
        "AIX, garbage aix garbage",
        "ANDROID, ANDROID",
        "ANDROID, garbage android garbage",
        "ANDROID, linux android linux",
        "DARWIN, DARWIN",
        "DARWIN, garbage apple garbage",
        "DARWIN, garbage darwin garbage",
        "DARWIN, garbage mac garbage",
        "DARWIN, garbage mac os garbage",
        "DARWIN, garbage mac os x garbage",
        "DARWIN, garbage mac osx garbage",
        "DARWIN, garbage macos garbage",
        "DARWIN, garbage macos x garbage",
        "DARWIN, garbage macosx garbage",
        "DRAGONFLYBSD, DRAGONFLYBSD",
        "DRAGONFLYBSD, garbage dragonfly garbage",
        "FREEBSD, FREEBSD",
        "FREEBSD, garbage freebsd garbage",
        "FUCHSIA, FUCHSIA",
        "FUCHSIA, garbage fuchsia garbage",
        "HAIKU, garbage haiku garbage",
        "HAIKU, HAIKU",
        "HPUX, garbage hp-ux garbage",
        "HPUX, garbage hpux garbage",
        "HPUX, HPUX",
        "IBMI, garbage ibm i garbage",
        "IBMI, garbage ibmi garbage",
        "IBMI, garbage os400 garbage",
        "IBMI, IBMI",
        "ILLUMOS, garbage illum os garbage",
        "ILLUMOS, garbage illumos garbage",
        "ILLUMOS, ILLUMOS",
        "LINUX, garbage nix garbage",
        "LINUX, garbage nux garbage",
        "LINUX, LINUX",
        "NETBSD, garbage netbsd garbage",
        "NETBSD, NETBSD",
        "OPENBSD, garbage openbsd garbage",
        "OPENBSD, OPENBSD",
        "PLAN9, garbage plan9 garbage",
        "PLAN9, PLAN9",
        "QNX, garbage procnto garbage",
        "QNX, garbage qnx garbage",
        "QNX, QNX",
        "REDOX, garbage redox garbage",
        "REDOX, REDOX",
        "SOLARIS, garbage solaris garbage",
        "SOLARIS, garbage sun os garbage",
        "SOLARIS, garbage sunos garbage",
        "SOLARIS, SOLARIS",
        "VXWORKS, garbage VxWorks garbage",
        "VXWORKS, VXWORKS",
        "WINDOWS, garbage w32 garbage",
        "WINDOWS, garbage w64 garbage",
        "WINDOWS, garbage win32 garbage",
        "WINDOWS, garbage windows garbage",
        "WINDOWS, Windows 10",
        "WINDOWS, Windows 7",
        "WINDOWS, Windows XP",
        "WINDOWS, WINDOWS",
        "ZOS, garbage z/OS garbage",
        "ZOS, garbage zos garbage",
        "ZOS, z/OS",
        "ZOS, ZOS",
    })
    void success(final @NotNull Os expected, final @NotNull String os) throws Throwable {
        assertOs(os, () -> assertEquals(expected, Os.current()));
    }

    @SetSystemProperty(key = "java.vm.name", value = "Dalvik")
    @Test void successAndroidLinux() throws Throwable {
        assertOs("linux", () -> assertEquals(Os.ANDROID, Os.currentOrNull()));
    }

    @Test void parseFailure() {
        assertThrows(UnsupportedPlatformException.class, () -> Os.parse("xxx"));
    }

    @Test void parseSuccess() {
        assertEquals(Os.LINUX, Os.parse("linux"));
    }

    @Test void currentOsIsWindowsIfFileSeparatorSystemPropertyIsSetToBackslash() throws Throwable {
        assertOs("\\", "unknown", () -> assertEquals(Os.WINDOWS, Os.current()));
    }

    @TestFactory Stream<DynamicTest> executableExtension() {
        return Arrays.stream(Os.values()).map(os -> dynamicTest(os.name(), () -> {
            final String it = os == Os.WINDOWS ? ".exe" : "";
            assertAll(
                () -> assertEquals(it, os.getExecutableExtension()),
                () -> assertEquals("some/path" + it, os.withExecutableExtension("some/path")),
                () -> assertEquals(new File("some/path" + it), os.withExecutableExtension(new File("some/path")))
            );
        }));
    }

    @TestFactory Stream<DynamicTest> linkLibraryExtension() {
        return Arrays.stream(Os.values()).map(os -> dynamicTest(os.name(), () -> {
            final String it = os == Os.WINDOWS ? ".lib" : ".so";
            assertAll(
                () -> assertEquals(it, os.getLinkLibraryExtension()),
                () -> assertEquals("some/path" + it, os.withLinkLibraryExtension("some/path")),
                () -> assertEquals(new File("some/path" + it), os.withLinkLibraryExtension(new File("some/path")))
            );
        }));
    }

    @TestFactory Stream<DynamicTest> sharedLibraryExtension() {
        return Arrays.stream(Os.values()).map(os -> dynamicTest(os.name(), () -> {
            final String it = os == Os.WINDOWS ? ".dll" : os == Os.DARWIN ? ".dylib" : ".so";
            assertAll(
                () -> assertEquals(it, os.getSharedLibraryExtension()),
                () -> assertEquals("some/path" + it, os.withSharedLibraryExtension("some/path")),
                () -> assertEquals(new File("some/path" + it), os.withSharedLibraryExtension(new File("some/path")))
            );
        }));
    }

    @TestFactory Stream<DynamicTest> staticLibraryExtension() {
        return Arrays.stream(Os.values()).map(os -> dynamicTest(os.name(), () -> {
            final String it = os == Os.WINDOWS ? ".lib" : ".a";
            assertAll(
                () -> assertEquals(it, os.getStaticLibraryExtension()),
                () -> assertEquals("some/path" + it, os.withStaticLibraryExtension("some/path")),
                () -> assertEquals(new File("some/path" + it), os.withStaticLibraryExtension(new File("some/path")))
            );
        }));
    }
}
