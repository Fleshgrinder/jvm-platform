package com.fleshgrinder.platform

import org.intellij.lang.annotations.Language as L
import com.fleshgrinder.junit.clearSystemProperty
import com.fleshgrinder.junit.enumTestsOf
import com.fleshgrinder.junit.generexWithGarbage
import com.fleshgrinder.junit.testsOf
import com.fleshgrinder.junit.toTests
import com.fleshgrinder.junit.withSystemProperty
import com.fleshgrinder.platform.Arch.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.parallel.ResourceLock
import org.junit.jupiter.api.parallel.Resources.SYSTEM_PROPERTIES

private class ArchTest {
    fun assertAlias(expected: Arch, alias: String) =
        withSystemProperty("os.arch", alias) {
            assertAll(
                { assertEquals(expected, current(), "current") },
                { if (expected.toString() == alias) assertEquals(expected, fromString(alias)) else assertThrows<IllegalArgumentException>("fromString") { fromString(alias) } },
                { assertEquals(expected, parse(alias), "parse") },
            )
        }

    fun aliasTestsOf(expected: Arch, @L("RegExp") p1: String) = generexWithGarbage(p1).toTests { assertAlias(expected, it) }
    fun aliasTestsOf(expected: Arch, @L("RegExp") p1: String, @L("RegExp") p2: String) = generexWithGarbage(p1, p2).toTests { assertAlias(expected, it) }
    fun aliasTestsOf(expected: Arch, @L("RegExp") p1: String, @L("RegExp") p2: String, @L("RegExp") p3: String, @L("RegExp") p4: String) = generexWithGarbage(p1, p2, p3, p4).toTests { assertAlias(expected, it) }

    inline fun isserTestsOf(vararg expected: Arch, crossinline test: Arch.() -> Boolean) =
        enumTestsOf<Arch> { assertEquals(it in expected, test(it)) }

    @Test fun `current Arch must be known`() {
        assertNotNull(currentOrNull())
    }

    @Test @ResourceLock(SYSTEM_PROPERTIES) fun `current throws if arch system property was forcefully removed by the user`() {
        assertThrows<IllegalStateException> { clearSystemProperty("os.arch") { current() } }
    }

    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun unknown() =
        testsOf("", "bash", "ksh", "m680000", "nvptx64", "script.sh", "sh64", "sh 64", "shell", "zsh") {
            withSystemProperty("os.arch", it) {
                assertAll(
                    { assertThrows<IllegalStateException>("current") { current() } },
                    { assertThrows<IllegalArgumentException>("fromString") { fromString(it) } },
                    { assertThrows<IllegalArgumentException>("parse") { parse(it) } },
                )
            }
        }

    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun names() =
        enumTestsOf<Arch> {
            val name = it.name
            withSystemProperty("os.arch", name) {
                assertAll(
                    { assertEquals(it, current(), "current") },
                    { assertThrows<IllegalArgumentException>("fromString") { fromString(name) } },
                    { assertEquals(it, parse(name), "parse") },
                )
            }
        }

    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun ids() =
        enumTestsOf<Arch> {
            val id = it.toString()
            withSystemProperty("os.arch", id) {
                assertAll(
                    { assertEquals(it, current(), "current") },
                    { assertEquals(it, fromString(id), "fromString") },
                    { assertEquals(it, parse(id), "parse") },
                )
            }
        }

    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `ALPHA_64 aliases`() = aliasTestsOf(ALPHA_64, "(DEC)?Alpha(64)?")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `ARM_32 aliases`() = aliasTestsOf(ARM_32, "ARM( ?32| ?v([1-3]|4T?|5(TE)?|6(-M)?|7(-A|E(-M)?|-R)?))?")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `ARM_32_BE aliases`() = aliasTestsOf(ARM_32_BE, "ARM( ?32)? ?(BE|EB)", "ARM ?(BE|EB) ?32")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `ARM_64 aliases`() = aliasTestsOf(ARM_64, "A(ARCH|RM) ?(64|v(8(\\.2(-A))?|9|[1-9][01]))")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `ARM_64_BE aliases`() = aliasTestsOf(ARM_64_BE, "AARCH( ?64)? ?(BE|EB)", "AARCH ?(BE|EB) ?64", "ARM ?64 ?(BE|EB)", "ARM ?(BE|EB) ?64")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `ITANIUM_32 aliases`() = aliasTestsOf(ITANIUM_32, "I(A-?64(n| ?32)|tanium ?32)")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `ITANIUM_64 aliases`() = aliasTestsOf(ITANIUM_64, "I(A ?64|tanium( ?64)?)")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `M68K_32 aliases`() = aliasTestsOf(M68K_32, "M68(k|000)")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `MIPS_32 aliases`() = aliasTestsOf(MIPS_32, "MIPS( ?32)?")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `MIPS_32_LE aliases`() = aliasTestsOf(MIPS_32_LE, "MIPS( ?32)? ?(LE|EL)", "MIPS ?(LE|EL)32")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `MIPS_64 aliases`() = aliasTestsOf(MIPS_64, "MIPS ?64")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `MIPS_64_LE aliases`() = aliasTestsOf(MIPS_64_LE, "MIPS ?64 ?(LE|EL)", "MIPS ?(EL|LE) ?64")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `PPC_32 aliases`() = aliasTestsOf(PPC_32, "P(ower( ?(PC|RS))?|PC)(32)?")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `PPC_32_LE aliases`() = aliasTestsOf(PPC_32_LE, "P(ower( ?(PC|RS))?|PC)( ?32) ?(LE|EL)", "P(ower( ?(PC|RS))?|PC) ?(LE|EL)(32)?")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `PPC_64 aliases`() = aliasTestsOf(PPC_64, "P(ower( ?(PC|RS))?|PC) ?64")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `PPC_64_LE aliases`() = aliasTestsOf(PPC_64_LE, "P(ower( ?(PC|RS))?|PC) ?64 ?(LE|EL)", "P(ower( ?(PC|RS))?|PC) ?(LE|EL) ?64")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `RISCV_32 aliases`() = aliasTestsOf(RISCV_32, "RISC ?V( ?32)?")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `RISCV_64 aliases`() = aliasTestsOf(RISCV_64, "RISC ?V ?64")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `S390_32 aliases`() = aliasTestsOf(S390_32, "s390( ?32)?", "IBM ?Z( ?32)?")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `S390_64 aliases`() = aliasTestsOf(S390_64, "s390(x(64)?| ?64)", "IBM ?Z ?64")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `SPARC_32 aliases`() = aliasTestsOf(SPARC_32, "(hyper|micro|Super|Turbo)?SPARC( ?32)?")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `SPARC_64 aliases`() = aliasTestsOf(SPARC_64, "(SPARC ?(64|v(9|[1-9][01]))|Ultra-?SPARC)")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `SUPERH_32 aliases`() = aliasTestsOf(SUPERH_32, "SuperH( ?32)?", "SH ?32")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `SUPERH_32_BE aliases`() = aliasTestsOf(SUPERH_32_BE, "SuperH( ?32)? ?(BE|EB)", "SuperH ?(BE|EB)32", "SH ?32 ?(BE|EB)", "SH ?(BE|EB)32")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `X86_32 aliases`() = aliasTestsOf(X86_32, "(ia|x)32", "(i[1-7]|x)86", "pentium", "win ?32")
    @TestFactory @ResourceLock(SYSTEM_PROPERTIES) fun `X86_64 aliases`() = aliasTestsOf(X86_64, "(amd ?|win ?|x(86 ?)?)64", "em64t", "i[89]86", "ia32e")

    @Test @ResourceLock(SYSTEM_PROPERTIES) fun `SUPERH_32 JVM special value`() = withSystemProperty("os.arch", "sh") { assertEquals(SUPERH_32, current(), "current") }
    @Test @ResourceLock(SYSTEM_PROPERTIES) fun `SUPERH_32_BE JVM special value`() = withSystemProperty("os.arch", "shbe") { assertEquals(SUPERH_32_BE, current(), "current") }

    @TestFactory fun is32bit() =
        testsOf(ARM_32, ARM_32_BE, ITANIUM_32, S390_32, M68K_32, MIPS_32, MIPS_32_LE, PPC_32, PPC_32_LE, RISCV_32, SPARC_32, SUPERH_32, SUPERH_32_BE, X86_32) {
            assertAll(
                { assertEquals(32, it.bitness) },
                { assertTrue(it.is32bit, "is32bit") },
                { assertFalse(it.is64bit, "is64bit") },
            )
        }

    @TestFactory fun is64bit() =
        testsOf(ALPHA_64, ARM_64, ARM_64_BE, ITANIUM_64, S390_64, MIPS_64, MIPS_64_LE, PPC_64, PPC_64_LE, RISCV_64, SPARC_64, X86_64) {
            assertAll(
                { assertEquals(64, it.bitness) },
                { assertFalse(it.is32bit, "is32bit") },
                { assertTrue(it.is64bit, "is64bit") },
            )
        }

    @TestFactory fun isArm() = isserTestsOf(ARM_32, ARM_32_BE, ARM_64, ARM_64_BE) { isArm }
    @TestFactory fun isArmBe() = isserTestsOf(ARM_32_BE, ARM_64_BE) { isArmBe }
    @TestFactory fun isArmLe() = isserTestsOf(ARM_32, ARM_64) { isArmLe }
    @TestFactory fun isItanium() = isserTestsOf(ITANIUM_32, ITANIUM_64) { isItanium }
    @TestFactory fun isMips() = isserTestsOf(MIPS_32, MIPS_32_LE, MIPS_64, MIPS_64_LE) { isMips }
    @TestFactory fun isMipsBe() = isserTestsOf(MIPS_32, MIPS_64) { isMipsBe }
    @TestFactory fun isMipsLe() = isserTestsOf(MIPS_32_LE, MIPS_64_LE) { isMipsLe }
    @TestFactory fun isPpc() = isserTestsOf(PPC_32, PPC_32_LE, PPC_64, PPC_64_LE) { isPpc }
    @TestFactory fun isPpcBe() = isserTestsOf(PPC_32, PPC_64) { isPpcBe }
    @TestFactory fun isPpcLe() = isserTestsOf(PPC_32_LE, PPC_64_LE) { isPpcLe }
    @TestFactory fun isRiscv() = isserTestsOf(RISCV_32, RISCV_64) { isRiscv }
    @TestFactory fun isS390() = isserTestsOf(S390_32, S390_64) { isS390 }
    @TestFactory fun isSparc() = isserTestsOf(SPARC_32, SPARC_64) { isSparc }
    @TestFactory fun isX86() = isserTestsOf(X86_32, X86_64) { isX86 }
}
