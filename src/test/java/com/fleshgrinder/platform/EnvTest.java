package com.fleshgrinder.platform;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junitpioneer.jupiter.ClearSystemProperty;
import org.junitpioneer.jupiter.SetSystemProperty;

import static com.fleshgrinder.platform.SystemProperties.FS_SEP;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("ResultOfMethodCallIgnored")
final class EnvTest {
    private static @NotNull String ldd(
        final @NotNull File tempDir,
        final @NotNull String name,
        final @NotNull String @NotNull ... lines
    ) throws IOException {
        final File ldd = new File(tempDir, "ldd-" + name + ".bat");
        ldd.createNewFile();
        ldd.setExecutable(true);
        try (final PrintWriter w = new PrintWriter(ldd, "UTF-8")) {
            if (Os.isWindows()) {
                w.write("@ECHO OFF\r\n");
                for (int i = 0; i < lines.length; i++) {
                    if (i != 0) w.write("ECHO.\r\n");
                    w.write("ECHO | SET /P line=\"");
                    w.write(lines[i]);
                    w.write("\"\r\n");
                }
            } else {
                w.write("#!/usr/bin/env sh\nprintf '");
                w.write(String.join("\n", lines));
                w.write("'");
            }
        }
        return ldd.getAbsolutePath();
    }

    @Test void bionic() {
        assertEquals(Env.BIONIC, Env.current(Os.ANDROID, "/non/existing/path"));
    }

    @ParameterizedTest
    @EnumSource(value = Os.class, names = {"DARWIN", "DRAGONFLYBSD", "FREEBSD", "NETBSD", "OPENBSD"})
    void bsdlibc(final @NotNull Os os) {
        assertEquals(Env.BSDLIBC, Env.current(os, "/non/existing/path"));
    }

    @SetSystemProperty(key = FS_SEP, value = "\\")
    @Test void msvc() {
        assertEquals(Env.MSVC, Env.current());
    }

    /** {@code docker run --rm alpine ldd --version} */
    @Test void musl(@TempDir final @NotNull File tempDir) throws IOException {
        final String ldd = ldd(
            tempDir,
            "musl",
            "musl libc (x86_64)",
            "Version 1.2.2",
            "Dynamic Program Loader",
            "Usage: /lib/ld-musl-x86_64.so.1 [options] [--] pathname"
        );

        assertEquals(Env.MUSL, Env.current(Os.LINUX, ldd));
    }

    /** {@code docker run --rm alpine:3.10.0 ldd --version} */
    @Test void muslBug(@TempDir final @NotNull File tempDir) throws IOException {
        final String ldd = ldd(
            tempDir,
            "musl-bug",
            "/lib/ld-musl-x86_64.so.1: cannot load --version: No such file or directory"
        );

        assertEquals(Env.MUSL, Env.current(Os.LINUX, ldd));
    }

    /** {@code docker run --rm ubuntu ldd --version} */
    @Test void glibc(@TempDir final @NotNull File tempDir) throws IOException {
        final String ldd = ldd(
            tempDir,
            "glibc",
            "ldd (Ubuntu GLIBC 2.31-0ubuntu9.2) 2.31",
            "Copyright (C) 2020 Free Software Foundation, Inc.",
            "This is free software; see the source for copying conditions.  There is NO",
            "warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.",
            "Written by Roland McGrath and Ulrich Drepper."
        );

        assertEquals(Env.GLIBC, Env.current(Os.LINUX, ldd));
    }

    @ParameterizedTest
    @CsvSource({
        "UNKNOWN, ''",
        "UNKNOWN, '    '",
        "UNKNOWN, '\t\n\t\n'",
        // os?x is too dangerous and produces too many false positives
        "UNKNOWN, garbage os x garbage",
        "BSDLIBC, BSDLIBC",
        "BSDLIBC, garbage apple garbage",
        "BSDLIBC, garbage bsd garbage",
        "BSDLIBC, garbage bsdlibc garbage",
        "BSDLIBC, garbage darwin garbage",
        "BSDLIBC, garbage mac garbage",
        "BSDLIBC, garbage macos garbage",
        "BSDLIBC, garbage mac os garbage",
        "BSDLIBC, garbage macosx garbage",
        "BSDLIBC, garbage macos x garbage",
        "BSDLIBC, garbage mac os x garbage",
        "BSDLIBC, garbage mac osx garbage",
        "BSDLIBC, garbage osx garbage",
        "DIETLIBC, DIETLIBC",
        "DIETLIBC, garbage dietlibc garbage",
        "GLIBC, garbage gcc garbage",
        "GLIBC, garbage glibc garbage",
        "GLIBC, garbage gnu garbage",
        "GLIBC, GLIBC",
        "KLIBC, garbage klibc garbage",
        "KLIBC, KLIBC",
        "MUSL, garbage musl garbage",
        "MUSL, MUSL",
        "NEWLIB, garbage newlib garbage",
        "NEWLIB, NEWLIB",
        "UCLIBC, garbage uclibc garbage",
        "UCLIBC, UCLIBC",
    })
    void parse(final @NotNull Env expected, final @NotNull String value) {
        assertEquals(expected, Env.parse(value));
    }

    /**
     * We test here that {@code ldd} is used by default and does not throw
     * regardless of the current platform, we cannot assert much here because
     * we do not know if {@code ldd} is actually available in the current
     * system, or not.
     */
    @Test void ldd() {
        assertDoesNotThrow(() -> Env.ldd(null));
    }

    @Test void lddHandlesInvocationExceptionsGracefully() {
        Env.ldd("/\0-is-not-allowed-in-a-path");
    }

    @Test void lddHandlesInterruptionGracefully(@TempDir final @NotNull File tempDir) throws IOException {
        final String ldd = ldd(tempDir, "interrupted");
        try {
            Thread.currentThread().interrupt();
            assertEquals("", Env.ldd(ldd));
        } finally {
            Thread.interrupted();
        }
    }

    @Test void lddProcessIsKilledIfItHangs(@TempDir final @NotNull File tempDir) throws IOException {
        final File ldd = new File(tempDir, "ldd-hang.bat");
        ldd.createNewFile();
        ldd.setExecutable(true);
        try (final PrintWriter w = new PrintWriter(ldd, "UTF-8")) {
            if (Os.isWindows()) {
                w.write("@ECHO OFF\r\n:loop\r\ngoto loop");
            } else {
                w.write("#!/usr/bin/env sh\ntail -f /dev/null");
            }
        }

        assertEquals("", Env.ldd(ldd.getAbsolutePath()));
    }
}
