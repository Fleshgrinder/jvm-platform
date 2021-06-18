package com.fleshgrinder.platform;

import java.io.File;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junitpioneer.jupiter.ClearSystemProperty;
import org.junitpioneer.jupiter.SetSystemProperty;

import static com.fleshgrinder.platform.SystemProperties.FS_SEP;
import static com.fleshgrinder.platform.SystemProperties.OS_NAME;
import static com.fleshgrinder.platform.SystemProperties.VM_NAME;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

final class OsTest {
    @ParameterizedTest
    @ClearSystemProperty(key = OS_NAME)
    @ClearSystemProperty(key = FS_SEP)
    @ClearSystemProperty(key = VM_NAME)
    @CsvSource({
        "UNKNOWN, ''",
        "UNKNOWN, '    '",
        "UNKNOWN, '\t\n\t\n'",
        "UNKNOWN, rv32imac",
        "UNKNOWN, rv64imac",
        "UNKNOWN, os4000",
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
    void current(final @NotNull Os expected, final @NotNull String osName) {
        SystemProperties.set(OS_NAME, osName);
        assertEquals(expected, Os.current());
    }

    @SetSystemProperty(key = OS_NAME, value = "linux")
    @SetSystemProperty(key = VM_NAME, value = "Dalvik")
    @ClearSystemProperty(key = FS_SEP)
    @Test void currentAndroid() {
        assertEquals(Os.ANDROID, Os.current());
    }

    @SetSystemProperty(key = FS_SEP, value = "\\")
    @ClearSystemProperty(key = VM_NAME)
    @ClearSystemProperty(key = OS_NAME)
    @Test void currentWindows() {
        assertEquals(Os.WINDOWS, Os.current());
    }

    private static void assertExtension(
        final @NotNull String ext,
        final @NotNull Supplier<String> getExt,
        final @NotNull Function<String, String> withExtString,
        final @NotNull Function<File, File> withExtFile
    ) {
        assertAll(
            () -> assertEquals(ext, getExt.get()),
            () -> assertEquals("some/path" + ext, withExtString.apply("some/path")),
            () -> assertEquals(new File("some/path" + ext), withExtFile.apply(new File("some/path")))
        );
    }

    @Test void executableExtensionWindows() {
        assertExtension(
            ".exe",
            Os.WINDOWS::getExecutableExtension,
            Os.WINDOWS::withExecutableExtension,
            Os.WINDOWS::withExecutableExtension
        );
    }

    @ParameterizedTest
    @EnumSource(value = Os.class, names = {"WINDOWS"}, mode = EXCLUDE)
    void executableExtension(final @NotNull Os os) {
        assertExtension(
            "",
            os::getExecutableExtension,
            os::withExecutableExtension,
            os::withExecutableExtension
        );
    }

    @Test void linkLibraryExtensionWindows() {
        assertExtension(
            ".lib",
            Os.WINDOWS::getLinkLibraryExtension,
            Os.WINDOWS::withLinkLibraryExtension,
            Os.WINDOWS::withLinkLibraryExtension
        );
    }

    @ParameterizedTest
    @EnumSource(value = Os.class, names = {"WINDOWS"}, mode = EXCLUDE)
    void linkLibraryExtension(final @NotNull Os os) {
        assertExtension(
            ".so",
            os::getLinkLibraryExtension,
            os::withLinkLibraryExtension,
            os::withLinkLibraryExtension
        );
    }

    @Test void sharedLibraryExtensionWindows() {
        assertExtension(
            ".dll",
            Os.WINDOWS::getSharedLibraryExtension,
            Os.WINDOWS::withSharedLibraryExtension,
            Os.WINDOWS::withSharedLibraryExtension
        );
    }

    @Test void sharedLibraryExtensionDarwin() {
        assertExtension(
            ".dylib",
            Os.DARWIN::getSharedLibraryExtension,
            Os.DARWIN::withSharedLibraryExtension,
            Os.DARWIN::withSharedLibraryExtension
        );
    }

    @ParameterizedTest
    @EnumSource(value = Os.class, names = {"DARWIN", "WINDOWS"}, mode = EXCLUDE)
    void sharedLibraryExtension(final @NotNull Os os) {
        assertExtension(
            ".so",
            os::getSharedLibraryExtension,
            os::withSharedLibraryExtension,
            os::withSharedLibraryExtension
        );
    }

    @Test void staticLibraryExtensionWindows() {
        assertExtension(
            ".lib",
            Os.WINDOWS::getStaticLibraryExtension,
            Os.WINDOWS::withStaticLibraryExtension,
            Os.WINDOWS::withStaticLibraryExtension
        );
    }

    @ParameterizedTest
    @EnumSource(value = Os.class, names = {"WINDOWS"}, mode = EXCLUDE)
    void staticLibraryExtension(final @NotNull Os os) {
        assertExtension(
            ".a",
            os::getStaticLibraryExtension,
            os::withStaticLibraryExtension,
            os::withStaticLibraryExtension
        );
    }
}
