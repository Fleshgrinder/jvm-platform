package com.fleshgrinder.platform;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junitpioneer.jupiter.SetSystemProperty;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class EnvTest {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static @NotNull String ldd(final @NotNull File tempDir, final @NotNull String name, final @NotNull String @NotNull ... lines) throws IOException {
        final File ldd = new File(tempDir, "ldd-" + name + ".bat");
        ldd.createNewFile();
        ldd.setExecutable(true);
        try (final PrintWriter w = new PrintWriter(ldd, "UTF-8")) {
            if (Os.isWindows()) {
                w.write("@ECHO OFF\r\n");
                for (final String line : lines) {
                    w.write("ECHO \"");
                    w.write(line);
                    w.write("\"\r\n");
                }
            } else {
                w.write("#!/usr/bin/env sh\n");
                for (final String line : lines) {
                    w.write("echo '");
                    w.write(line);
                    w.write("'\n");
                }
            }
        }
        return ldd.getAbsolutePath();
    }

    @Test void bionic() {
        assertAll(
            () -> assertEquals(Env.BIONIC, Env.current(Os.ANDROID, "/non/existing/path")),
            () -> assertEquals(Env.BIONIC, Env.currentOrNull(Os.ANDROID, "/non/existing/path"))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"DARWIN", "DRAGONFLYBSD", "FREEBSD", "NETBSD", "OPENBSD"})
    void bsdlibc(final @NotNull Os os) {
        assertAll(
            () -> assertEquals(Env.BSDLIBC, Env.current(os, "/non/existing/path")),
            () -> assertEquals(Env.BSDLIBC, Env.currentOrNull(os, "/non/existing/path"))
        );
    }

    @SetSystemProperty(key = "file.separator", value = "\\")
    @Test void msvc() {
        assertAll(
            () -> assertEquals(Env.MSVC, Env.current()),
            () -> assertEquals(Env.MSVC, Env.currentOrNull())
        );
    }

    /** {@code docker run --rm alpine ldd --version} */
    @Test void musl(@TempDir final @NotNull File tempDir) throws IOException {
        final String ldd = ldd(tempDir, "musl",
            "musl libc (x86_64)" +
                "Version 1.2.2" +
                "Dynamic Program Loader" +
                "Usage: /lib/ld-musl-x86_64.so.1 [options] [--] pathname"
        );

        assertAll(
            () -> assertEquals(Env.MUSL, Env.current(Os.LINUX, ldd)),
            () -> assertEquals(Env.MUSL, Env.currentOrNull(Os.LINUX, ldd))
        );
    }

    /** {@code docker run --rm alpine:3.10.0 ldd --version} */
    @Test void muslBug(@TempDir final @NotNull File tempDir) throws IOException {
        final String ldd = ldd(tempDir, "musl-bug", "/lib/ld-musl-x86_64.so.1: cannot load --version: No such file or directory");

        assertAll(
            () -> assertEquals(Env.MUSL, Env.current(Os.LINUX, ldd)),
            () -> assertEquals(Env.MUSL, Env.currentOrNull(Os.LINUX, ldd))
        );
    }

    /** {@code docker run --rm ubuntu ldd --version} */
    @Test void glibc(@TempDir final @NotNull File tempDir) throws IOException {
        final String ldd = ldd(tempDir, "glibc",
            "ldd (Ubuntu GLIBC 2.31-0ubuntu9.2) 2.31",
            "Copyright (C) 2020 Free Software Foundation, Inc.",
            "This is free software; see the source for copying conditions.  There is NO",
            "warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.",
            "Written by Roland McGrath and Ulrich Drepper."
        );

        assertAll(
            () -> assertEquals(Env.GLIBC, Env.current(Os.LINUX, ldd)),
            () -> assertEquals(Env.GLIBC, Env.currentOrNull(Os.LINUX, ldd))
        );
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {
        "    ",
        "\t\n\t\n",
        // os?x is too dangerous and produces too many false positives
        "garbage os x garbage",
    })
    void parseFailure(final @NotNull String value) {
        assertThrows(UnsupportedPlatformException.class, () -> Env.parse(value));
    }

    @ParameterizedTest
    @CsvSource({
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
    void parseSuccess(final @NotNull Env expected, final @NotNull String value) {
        assertEquals(expected, Env.parse(value));
    }

    @Test void lddProcessIsKilled(@TempDir final @NotNull File tempDir) throws IOException {
        final File ldd = new File(tempDir, "ldd-hang");
        //noinspection ResultOfMethodCallIgnored
        ldd.createNewFile();
        //noinspection ResultOfMethodCallIgnored
        ldd.setExecutable(true);
        try (final PrintWriter w = new PrintWriter(ldd, "UTF-8")) {
            w.write("#!/usr/bin/env sh\ntail -f /dev/null\n");
        }

        assertEquals("", Env.ldd(ldd.getAbsolutePath()));
    }
}
