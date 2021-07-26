package com.fleshgrinder.platform

import com.fleshgrinder.platform.Os.WINDOWS
import java.io.File
import java.io.PrintWriter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

private class PlatformMuslTest {
    val isWindows = Os.currentOrNull() == WINDOWS

    inline fun File.ldd(name: String, content: Appendable.() -> Unit): String {
        val lddFile = resolve("ldd-$name.bat")
        lddFile.createNewFile()
        lddFile.setExecutable(true)
        PrintWriter(lddFile, "UTF-8").use(content)
        return lddFile.absolutePath
    }

    fun File.ldd(name: String, vararg lines: String): String =
        ldd(name) {
            if (isWindows) {
                appendLine("@ECHO OFF")
                lines.forEachIndexed { i, line ->
                    if (i != 0) appendLine("ECHO.")
                    append("ECHO | SET /P line=\"")
                    append(line)
                    appendLine('"')
                }
            } else {
                append("#!/usr/bin/env sh\nprintf '")
                lines.forEach { append(it).append("\\n") }
                appendLine("'")
            }
        }

    /**
     * We don't know if the current platform is a musl platform, but we do know
     * that this must never throw on any system.
     */
    @Test fun `hasMusl never throws`() {
        Assertions.assertDoesNotThrow { Platform.hasMusl() }
    }

    /** `docker run --rm alpine ldd --version` */
    @Test fun `standard musl ldd output can be parsed`(@TempDir tempDir: File) {
        val lddPath = tempDir.ldd(
            name = "musl",
            "musl libc (x86_64)",
            "Version 1.2.2",
            "Dynamic Program Loader",
            "Usage: /lib/ld-musl-x86_64.so.1 [options] [--] pathname",
        )
        Assertions.assertTrue(Platform.hasMusl(lddPath))
    }

    /** `docker run --rm alpine:3.10.0 ldd --version` */
    @Test fun `broken musl ldd output can be parsed`(@TempDir tempDir: File) {
        val lddPath = tempDir.ldd(
            name = "musl-bug",
            "/lib/ld-musl-x86_64.so.1: cannot load --version: No such file or directory",
        )
        Assertions.assertTrue(Platform.hasMusl(lddPath))
    }

    /** `docker run --rm ubuntu ldd --version` */
    @Test fun `glibc ldd output can be parsed`(@TempDir tempDir: File) {
        val lddPath = tempDir.ldd(
            name = "glibc",
            "ldd (Ubuntu GLIBC 2.31-0ubuntu9.2) 2.31",
            "Copyright (C) 2020 Free Software Foundation, Inc.",
            "This is free software; see the source for copying conditions.  There is NO",
            "warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.",
            "Written by Roland McGrath and Ulrich Drepper.",
        )
        Assertions.assertFalse(Platform.hasMusl(lddPath))
    }

    @Test fun `hasMusl returns false if ldd is missing`() {
        Assertions.assertFalse(Platform.hasMusl("/non/existing/path"))
    }

    @Test fun `hasMusl handles invocation exceptions gracefully`() {
        Assertions.assertFalse(Platform.hasMusl("/\u0000-NUL-is-never-allowed-in-any-path"))
    }

    @Test fun `hasMusl handles interruption gracefully`(@TempDir tempDir: File) {
        val lddPath = tempDir.ldd("interrupted")
        try {
            Thread.currentThread().interrupt()
            Assertions.assertFalse(Platform.hasMusl(lddPath))
        } finally {
            Thread.interrupted()
        }
    }

    @Test fun `hasMusl kills hanging ldd process`(@TempDir tempDir: File) {
        val lddPath = tempDir.ldd("hang") { appendLine(if (isWindows) "@ECHO OFF\n:loop\ngoto loop" else "#!/usr/bin/env sh\ntail -f /dev/null") }
        Assertions.assertFalse(Platform.hasMusl(lddPath))
    }
}
