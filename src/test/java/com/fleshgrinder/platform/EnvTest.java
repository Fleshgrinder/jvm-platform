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

import static org.junit.jupiter.api.Assertions.*;

final class EnvTest {
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
        final File ldd = new File(tempDir, "ldd-musl");
        //noinspection ResultOfMethodCallIgnored
        ldd.createNewFile();
        //noinspection ResultOfMethodCallIgnored
        ldd.setExecutable(true);
        try (final PrintWriter w = new PrintWriter(ldd, "UTF-8")) {
            w.write(
                "#!/usr/bin/env sh\n" +
                    "echo 'musl libc (x86_64)'\n" +
                    "echo 'Version 1.2.2'\n" +
                    "echo 'Dynamic Program Loader'\n" +
                    "echo 'Usage: /lib/ld-musl-x86_64.so.1 [options] [--] pathname'\n"
            );
        }

        assertAll(
            () -> assertEquals(Env.MUSL, Env.current(Os.LINUX, ldd.getAbsolutePath())),
            () -> assertEquals(Env.MUSL, Env.currentOrNull(Os.LINUX, ldd.getAbsolutePath()))
        );
    }

    /** {@code docker run --rm alpine:3.10.0 ldd --version} */
    @Test void muslBug(@TempDir final @NotNull File tempDir) throws IOException {
        final File ldd = new File(tempDir, "ldd-musl-bug");
        //noinspection ResultOfMethodCallIgnored
        ldd.createNewFile();
        //noinspection ResultOfMethodCallIgnored
        ldd.setExecutable(true);
        try (final PrintWriter w = new PrintWriter(ldd, "UTF-8")) {
            w.write("#!/usr/bin/env sh\necho '/lib/ld-musl-x86_64.so.1: cannot load --version: No such file or directory'\n");
        }

        assertAll(
            () -> assertEquals(Env.MUSL, Env.current(Os.LINUX, ldd.getAbsolutePath())),
            () -> assertEquals(Env.MUSL, Env.currentOrNull(Os.LINUX, ldd.getAbsolutePath()))
        );
    }

    /** {@code docker run --rm ubuntu ldd --version} */
    @Test void glibc(@TempDir final @NotNull File tempDir) throws IOException {
        final File ldd = new File(tempDir, "ldd-glibc");
        //noinspection ResultOfMethodCallIgnored
        ldd.createNewFile();
        //noinspection ResultOfMethodCallIgnored
        ldd.setExecutable(true);
        try (final PrintWriter w = new PrintWriter(ldd, "UTF-8")) {
            w.write(
                "#!/usr/bin/env sh\n" +
                    "echo 'ldd (Ubuntu GLIBC 2.31-0ubuntu9.2) 2.31'\n" +
                    "echo 'Copyright (C) 2020 Free Software Foundation, Inc.'\n" +
                    "echo 'This is free software; see the source for copying conditions.  There is NO'\n" +
                    "echo 'warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.'\n" +
                    "echo 'Written by Roland McGrath and Ulrich Drepper.'\n"
            );
        }

        assertAll(
            () -> assertEquals(Env.GLIBC, Env.current(Os.LINUX, ldd.getAbsolutePath())),
            () -> assertEquals(Env.GLIBC, Env.currentOrNull(Os.LINUX, ldd.getAbsolutePath()))
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
