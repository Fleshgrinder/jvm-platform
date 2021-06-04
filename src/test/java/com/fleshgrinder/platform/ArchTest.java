package com.fleshgrinder.platform;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

final class ArchTest {
    private static void assertArch(
        final @Nullable String arch,
        final @NotNull Executable exe
    ) throws Throwable {
        final @NotNull String original = System.getProperty("os.arch");
        System.setProperty("os.arch", arch == null ? "unknown" : arch);
        try {
            exe.execute();
        } finally {
            System.setProperty("os.arch", original);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
        "    ",
        "\t\n\t\n",
        "nvptx64",
        "script.sh",
        "bash",
        "ksh",
        "shell",
        "zsh",
    })
    void failure(final @NotNull String arch) throws Throwable {
        assertArch(arch, () -> assertThrows(UnsupportedPlatformException.class, Arch::current));
    }

    @ParameterizedTest
    @CsvSource({
        // region jvm
        "ALPHA_64,      alpha",
        "ARM_32,        arm",
        "ARM_64,        aarch64",
        "ARM_64,        arm64",
        "IA_64,         ia64",
        "IBMZ_32,       s390",
        "IBMZ_64,       s390x",
        "M68K_32,       m68k",
        "MIPS_32,       mips",
        "MIPS_32_LE,    mipsel",
        "MIPS_64,       mips64",
        "MIPS_64_LE,    mips64el",
        "PPC_32,        ppc",
        "PPC_32_LE,     ppcle",
        "PPC_64,        ppc64",
        "PPC_64_LE,     ppc64le",
        "SPARC_32,      sparc",
        "SPARC_64,      sparcv9",
        "SUPERH_32,     sh",
        "SUPERH_32_BE,  shbe",
        "X86_32,        i386",
        "X86_32,        pentium",
        "X86_32,        x86",
        "X86_64,        amd64",
        "X86_64,        x86_64",
        // endregion jvm
        // region parse
        // region X86_32
        "X86_32, X86_32",
        "X86_32, garbage X86_32  garbage",
        "X86_32, garbage ia32    garbage",
        "X86_32, garbage x32     garbage",
        "X86_32, garbage x86     garbage",
        "X86_32, garbage i186    garbage",
        "X86_32, garbage 186     garbage",
        "X86_32, garbage i286    garbage",
        "X86_32, garbage 286     garbage",
        "X86_32, garbage i386    garbage",
        "X86_32, garbage 386     garbage",
        "X86_32, garbage i486    garbage",
        "X86_32, garbage 486     garbage",
        "X86_32, garbage i586    garbage",
        "X86_32, garbage 586     garbage",
        "X86_32, garbage i686    garbage",
        "X86_32, garbage 686     garbage",
        "X86_32, garbage i786    garbage",
        "X86_32, garbage 786     garbage",
        "X86_32, garbage pentium garbage",
        // endregion X86_32
        // region X86_64
        "X86_64, X86_64",
        "X86_64, garbage X86_64 garbage",
        "X86_64, garbage x8664  garbage",
        "X86_64, garbage x86.64 garbage",
        "X86_64, garbage x86-64 garbage",
        "X86_64, garbage x86_64 garbage",
        "X86_64, garbage em64t  garbage",
        "X86_64, garbage ia32e  garbage",
        "X86_64, garbage x64    garbage",
        "X86_64, garbage i886   garbage",
        "X86_64, garbage 886    garbage",
        "X86_64, garbage i986   garbage",
        "X86_64, garbage 986    garbage",
        // endregion X86_64
        // region ARM_32_BE
        "ARM_32_BE, ARM_32_BE",
        "ARM_32_BE, garbage ARM_32_BE garbage",
        "ARM_32_BE, garbage armbe garbage",
        "ARM_32_BE, garbage armeb garbage",
        "ARM_32_BE, garbage arm be garbage",
        "ARM_32_BE, garbage arm eb garbage",
        "ARM_32_BE, garbage arm 32 be garbage",
        "ARM_32_BE, garbage arm 32 eb garbage",
        // endregion ARM_32_BE
        // region ARM_64_BE
        "ARM_64_BE, ARM_64_BE",
        "ARM_64_BE, garbage ARM_64_BE   garbage",
        "ARM_64_BE, garbage aarchbe     garbage",
        "ARM_64_BE, garbage aarcheb     garbage",
        "ARM_64_BE, garbage aarch64be   garbage",
        "ARM_64_BE, garbage aarch64eb   garbage",
        "ARM_64_BE, garbage aarch be    garbage",
        "ARM_64_BE, garbage aarch eb    garbage",
        "ARM_64_BE, garbage aarch be 64 garbage",
        "ARM_64_BE, garbage aarch eb 64 garbage",
        "ARM_64_BE, garbage aarch 64 be garbage",
        "ARM_64_BE, garbage aarch 64 eb garbage",
        // endregion ARM_64_BE
        // region ARM_32
        "ARM_32, ARM_32",
        "ARM_32, garbage ARM_32 garbage",
        "ARM_32, garbage arm garbage",
        // endregion ARM_32
        // region ARM_64
        "ARM_64, ARM_64",
        "ARM_64, garbage ARM_64 garbage",
        "ARM_64, garbage aarch garbage",
        "ARM_64, garbage arm64 garbage",
        "ARM_64, garbage arm-64 garbage",
        "ARM_64, garbage armv8 garbage",
        "ARM_64, garbage armv8.2 garbage",
        "ARM_64, garbage armv8.2-a garbage",
        "ARM_64, garbage arm v8 garbage",
        "ARM_64, garbage arm v8.2 garbage",
        "ARM_64, garbage arm v8.2-a garbage",
        "ARM_64, garbage arm v10 garbage",
        // endregion ARM_64
        // region ALPHA_64
        "ALPHA_64, ALPHA_64",
        "ALPHA_64, garbage ALPHA_64 garbage",
        "ALPHA_64, garbage alpha garbage",
        // endregion ALPHA_64
        // region IA_32
        "IA_32, IA_32",
        "IA_32, garbage IA_32 garbage",
        "IA_32, garbage ia64n garbage",
        "IA_32, garbage ia6432 garbage",
        "IA_32, garbage ia64 32 garbage",
        "IA_32, garbage itanium32 garbage",
        "IA_32, garbage itanium 32 garbage",
        // endregion IA_32
        // region IA_64
        "IA_64, IA_64",
        "IA_64, garbage IA_64 garbage",
        "IA_64, garbage ia64 garbage",
        "IA_64, garbage itanium garbage",
        // endregion IA_64
        // region M68K_32
        "M68K_32, M68K_32",
        "M68K_32, garbage M68K_32 garbage",
        "M68K_32, garbage m68k garbage",
        "M68K_32, garbage m68000 garbage",
        // endregion M68K_32
        // region IBMZ_32
        "IBMZ_32, IBMZ_32",
        "IBMZ_32, garbage IBMZ_32 garbage",
        "IBMZ_32, garbage s390 garbage",
        "IBMZ_32, garbage ibmz garbage",
        "IBMZ_32, garbage ibm z garbage",
        // endregion IBMZ_32
        // region IBMZ_64
        "IBMZ_64, IBMZ_64",
        "IBMZ_64, garbage IBMZ_64 garbage",
        "IBMZ_64, garbage s390x garbage",
        "IBMZ_64, garbage s390 x garbage",
        "IBMZ_64, garbage s39064 garbage",
        "IBMZ_64, garbage s390 64 garbage",
        "IBMZ_64, garbage ibmz64 garbage",
        "IBMZ_64, garbage ibmz 64 garbage",
        "IBMZ_64, garbage ibm z 64 garbage",
        // endregion IBMZ_64
        // region PPC_32_LE
        "PPC_32_LE, PPC_32_LE",
        "PPC_32_LE, garbage PPC_32_LE garbage",

        "PPC_32_LE, garbage ppc32le garbage",
        "PPC_32_LE, garbage ppc32 le garbage",
        "PPC_32_LE, garbage ppc 32le garbage",
        "PPC_32_LE, garbage ppc 32 le garbage",

        "PPC_32_LE, garbage ppc32el garbage",
        "PPC_32_LE, garbage ppc32 el garbage",
        "PPC_32_LE, garbage ppc 32el garbage",
        "PPC_32_LE, garbage ppc 32 el garbage",

        "PPC_32_LE, garbage ppcle garbage",
        "PPC_32_LE, garbage ppc le garbage",

        "PPC_32_LE, garbage ppcel garbage",
        "PPC_32_LE, garbage ppc el garbage",

        "PPC_32_LE, garbage power32le garbage",
        "PPC_32_LE, garbage power32 le garbage",
        "PPC_32_LE, garbage power 32le garbage",
        "PPC_32_LE, garbage power 32 le garbage",

        "PPC_32_LE, garbage power32el garbage",
        "PPC_32_LE, garbage power32 el garbage",
        "PPC_32_LE, garbage power 32el garbage",
        "PPC_32_LE, garbage power 32 el garbage",

        "PPC_32_LE, garbage powerle garbage",
        "PPC_32_LE, garbage power le garbage",

        "PPC_32_LE, garbage powerel garbage",
        "PPC_32_LE, garbage power el garbage",

        "PPC_32_LE, garbage powerpc32le garbage",
        "PPC_32_LE, garbage powerpc32 le garbage",
        "PPC_32_LE, garbage powerpc 32le garbage",
        "PPC_32_LE, garbage powerpc 32 le garbage",

        "PPC_32_LE, garbage powerpc32el garbage",
        "PPC_32_LE, garbage powerpc32 el garbage",
        "PPC_32_LE, garbage powerpc 32el garbage",
        "PPC_32_LE, garbage powerpc 32 el garbage",

        "PPC_32_LE, garbage powerpcle garbage",
        "PPC_32_LE, garbage powerpc le garbage",

        "PPC_32_LE, garbage powerpcel garbage",
        "PPC_32_LE, garbage powerpc el garbage",

        "PPC_32_LE, garbage power pc32le garbage",
        "PPC_32_LE, garbage power pc32 le garbage",
        "PPC_32_LE, garbage power pc 32le garbage",
        "PPC_32_LE, garbage power pc 32 le garbage",

        "PPC_32_LE, garbage power pc32el garbage",
        "PPC_32_LE, garbage power pc32 el garbage",
        "PPC_32_LE, garbage power pc 32el garbage",
        "PPC_32_LE, garbage power pc 32 el garbage",

        "PPC_32_LE, garbage power pcle garbage",
        "PPC_32_LE, garbage power pc le garbage",

        "PPC_32_LE, garbage power pcel garbage",
        "PPC_32_LE, garbage power pc el garbage",

        "PPC_32_LE, garbage powerrs32le garbage",
        "PPC_32_LE, garbage powerrs32 le garbage",
        "PPC_32_LE, garbage powerrs 32le garbage",
        "PPC_32_LE, garbage powerrs 32 le garbage",

        "PPC_32_LE, garbage powerrs32el garbage",
        "PPC_32_LE, garbage powerrs32 el garbage",
        "PPC_32_LE, garbage powerrs 32el garbage",
        "PPC_32_LE, garbage powerrs 32 el garbage",

        "PPC_32_LE, garbage powerrsle garbage",
        "PPC_32_LE, garbage powerrs le garbage",

        "PPC_32_LE, garbage powerrsel garbage",
        "PPC_32_LE, garbage powerrs el garbage",

        "PPC_32_LE, garbage power rs32le garbage",
        "PPC_32_LE, garbage power rs32 le garbage",
        "PPC_32_LE, garbage power rs 32le garbage",
        "PPC_32_LE, garbage power rs 32 le garbage",

        "PPC_32_LE, garbage power rs32el garbage",
        "PPC_32_LE, garbage power rs32 el garbage",
        "PPC_32_LE, garbage power rs 32el garbage",
        "PPC_32_LE, garbage power rs 32 el garbage",

        "PPC_32_LE, garbage power rsle garbage",
        "PPC_32_LE, garbage power rs le garbage",

        "PPC_32_LE, garbage power rsel garbage",
        "PPC_32_LE, garbage power rs el garbage",
        // endregion PPC_32_LE
        // region PPC_64_LE
        "PPC_64_LE, PPC_64_LE",
        "PPC_64_LE, garbage PPC_64_LE garbage",

        "PPC_64_LE, garbage ppc64le garbage",
        "PPC_64_LE, garbage ppc64 le garbage",
        "PPC_64_LE, garbage ppc 64le garbage",
        "PPC_64_LE, garbage ppc 64 le garbage",

        "PPC_64_LE, garbage ppc64el garbage",
        "PPC_64_LE, garbage ppc64 el garbage",
        "PPC_64_LE, garbage ppc 64el garbage",
        "PPC_64_LE, garbage ppc 64 el garbage",

        "PPC_64_LE, garbage power64le garbage",
        "PPC_64_LE, garbage power64 le garbage",
        "PPC_64_LE, garbage power 64le garbage",
        "PPC_64_LE, garbage power 64 le garbage",

        "PPC_64_LE, garbage power64el garbage",
        "PPC_64_LE, garbage power64 el garbage",
        "PPC_64_LE, garbage power 64el garbage",
        "PPC_64_LE, garbage power 64 el garbage",

        "PPC_64_LE, garbage powerpc64le garbage",
        "PPC_64_LE, garbage powerpc64 le garbage",
        "PPC_64_LE, garbage powerpc 64le garbage",
        "PPC_64_LE, garbage powerpc 64 le garbage",

        "PPC_64_LE, garbage powerpc64el garbage",
        "PPC_64_LE, garbage powerpc64 el garbage",
        "PPC_64_LE, garbage powerpc 64el garbage",
        "PPC_64_LE, garbage powerpc 64 el garbage",

        "PPC_64_LE, garbage power pc64le garbage",
        "PPC_64_LE, garbage power pc64 le garbage",
        "PPC_64_LE, garbage power pc 64le garbage",
        "PPC_64_LE, garbage power pc 64 le garbage",

        "PPC_64_LE, garbage power pc64el garbage",
        "PPC_64_LE, garbage power pc64 el garbage",
        "PPC_64_LE, garbage power pc 64el garbage",
        "PPC_64_LE, garbage power pc 64 el garbage",

        "PPC_64_LE, garbage powerrs64le garbage",
        "PPC_64_LE, garbage powerrs64 le garbage",
        "PPC_64_LE, garbage powerrs 64le garbage",
        "PPC_64_LE, garbage powerrs 64 le garbage",

        "PPC_64_LE, garbage powerrs64el garbage",
        "PPC_64_LE, garbage powerrs64 el garbage",
        "PPC_64_LE, garbage powerrs 64el garbage",
        "PPC_64_LE, garbage powerrs 64 el garbage",

        "PPC_64_LE, garbage power rs64le garbage",
        "PPC_64_LE, garbage power rs64 le garbage",
        "PPC_64_LE, garbage power rs 64le garbage",
        "PPC_64_LE, garbage power rs 64 le garbage",

        "PPC_64_LE, garbage power rs64el garbage",
        "PPC_64_LE, garbage power rs64 el garbage",
        "PPC_64_LE, garbage power rs 64el garbage",
        "PPC_64_LE, garbage power rs 64 el garbage",
        // endregion PPC_64_LE
        // region PPC_32
        "PPC_32, PPC_32",
        "PPC_32, garbage PPC_32 garbage",
        "PPC_32, garbage ppc garbage",
        "PPC_32, garbage power garbage",
        "PPC_32, garbage powerpc garbage",
        "PPC_32, garbage powerrs garbage",
        "PPC_32, garbage power pc garbage",
        "PPC_32, garbage power rs garbage",
        // endregion PPC_32
        // region PPC_64
        "PPC_64, PPC_64",
        "PPC_64, garbage PPC_64 PPC_64",
        "PPC_64, garbage ppc64 PPC_64",
        "PPC_64, garbage ppc 64 PPC_64",
        "PPC_64, garbage power64 PPC_64",
        "PPC_64, garbage power 64 PPC_64",
        "PPC_64, garbage powerpc64 PPC_64",
        "PPC_64, garbage powerpc 64 PPC_64",
        "PPC_64, garbage power pc 64 PPC_64",
        "PPC_64, garbage power pc64 PPC_64",
        "PPC_64, garbage powerrs64 PPC_64",
        "PPC_64, garbage powerrs 64 PPC_64",
        "PPC_64, garbage power rs 64 PPC_64",
        "PPC_64, garbage power rs64 PPC_64",
        // endregion PPC_64
        // region MIPS_32_LE
        "MIPS_32_LE, MIPS_32_LE",
        "MIPS_32_LE, garbage MIPS_32_LE garbage",
        "MIPS_32_LE, garbage mips32le garbage",
        "MIPS_32_LE, garbage mips32el garbage",
        "MIPS_32_LE, garbage mips32 le garbage",
        "MIPS_32_LE, garbage mips32 el garbage",
        "MIPS_32_LE, garbage mips 32le garbage",
        "MIPS_32_LE, garbage mips 32el garbage",
        "MIPS_32_LE, garbage mips 32 le garbage",
        "MIPS_32_LE, garbage mips 32 el garbage",
        "MIPS_32_LE, garbage mipsle garbage",
        "MIPS_32_LE, garbage mips le garbage",
        // endregion MIPS_32_LE
        // region MIPS_64_LE
        "MIPS_64_LE, MIPS_64_LE",
        "MIPS_64_LE, garbage MIPS_64_LE garbage",
        "MIPS_64_LE, garbage mips64le garbage",
        "MIPS_64_LE, garbage mips64el garbage",
        "MIPS_64_LE, garbage mips64 le garbage",
        "MIPS_64_LE, garbage mips64 el garbage",
        "MIPS_64_LE, garbage mips 64le garbage",
        "MIPS_64_LE, garbage mips 64el garbage",
        "MIPS_64_LE, garbage mips 64 le garbage",
        "MIPS_64_LE, garbage mips 64 el garbage",
        "MIPS_64_LE, garbage mipsle64 garbage",
        "MIPS_64_LE, garbage mipsel64 garbage",
        "MIPS_64_LE, garbage mipsle 64 garbage",
        "MIPS_64_LE, garbage mipsel 64 garbage",
        "MIPS_64_LE, garbage mips le64 garbage",
        "MIPS_64_LE, garbage mips el64 garbage",
        "MIPS_64_LE, garbage mips le 64 garbage",
        "MIPS_64_LE, garbage mips el 64 garbage",
        // endregion MIPS_64_LE
        // region MIPS_32
        "MIPS_32, MIPS_32",
        "MIPS_32, garbage MIPS_32 garbage",
        "MIPS_32, garbage mips garbage",
        "MIPS_32, garbage mips32 garbage",
        "MIPS_32, garbage mips 32 garbage",
        // endregion MIPS_32
        // region MIPS_64
        "MIPS_64, MIPS_64",
        "MIPS_64, garbage MIPS_64 garbage",
        "MIPS_64, garbage mips64 garbage",
        "MIPS_64, garbage mips 64 garbage",
        // endregion MIPS_64
        // region RISCV_32
        "RISCV_32, RISCV_32",
        "RISCV_32, garbage RISCV_32 garbage",
        "RISCV_32, garbage riscv garbage",
        "RISCV_32, garbage risc v garbage",
        // endregion RISCV_32
        // region RISCV_64
        "RISCV_64, RISCV_64",
        "RISCV_64, garbage RISCV_64 garbage",
        "RISCV_64, garbage riscv64 garbage",
        "RISCV_64, garbage riscv 64 garbage",
        "RISCV_64, garbage risc v64 garbage",
        "RISCV_64, garbage risc v 64 garbage",
        // endregion RISCV_64
        // region SPARC_32
        "SPARC_32, SPARC_32",
        "SPARC_32, garbage SPARC_32 garbage",
        "SPARC_32, garbage sparc garbage",
        // endregion SPARC_32
        // region SPARC_64
        "SPARC_64, SPARC_64",
        "SPARC_64, garbage SPARC_64 garbage",
        "SPARC_64, garbage ultrasparc garbage",
        "SPARC_64, garbage ultra sparc garbage",
        "SPARC_64, garbage sparcv9 garbage",
        "SPARC_64, garbage sparc v9 garbage",
        "SPARC_64, garbage sparc64 garbage",
        "SPARC_64, garbage sparc 64 garbage",
        // endregion SPARC_64
        // region SUPERH_32_BE
        "SUPERH_32_BE, SUPERH_32_BE",
        "SUPERH_32_BE, garbage SUPERH_32_BE garbage",

        "SUPERH_32_BE, garbage superh32be garbage",
        "SUPERH_32_BE, garbage superhbe32 garbage",
        "SUPERH_32_BE, garbage superh32 be garbage",
        "SUPERH_32_BE, garbage superhbe 32 garbage",
        "SUPERH_32_BE, garbage superh 32 be garbage",
        "SUPERH_32_BE, garbage superh be 32 garbage",
        "SUPERH_32_BE, garbage superhbe garbage",
        "SUPERH_32_BE, garbage superh be garbage",

        "SUPERH_32_BE, garbage sh32be garbage",
        "SUPERH_32_BE, garbage shbe32 garbage",
        "SUPERH_32_BE, garbage sh32 be garbage",
        "SUPERH_32_BE, garbage shbe 32 garbage",
        "SUPERH_32_BE, garbage sh 32 be garbage",
        "SUPERH_32_BE, garbage sh be 32 garbage",
        "SUPERH_32_BE, garbage shbe garbage",
        "SUPERH_32_BE, garbage sh be garbage",
        // endregion SUPERH_32_BE
        // region SUPERH_32
        "SUPERH_32, SUPERH_32",
        "SUPERH_32, garbage SUPERH_32 garbage",
        "SUPERH_32, garbage superh garbage",
        "SUPERH_32, garbage sh garbage",
        "SUPERH_32, garbage sh",
        "SUPERH_32, sh garbage",
        // endregion SUPERH_32
        // endregion parse
    })
    void success(final @NotNull Arch expected, final @NotNull String arch) throws Throwable {
        assertArch(arch, () -> assertEquals(expected, Arch.current()));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test void parseFailure() {
        assertThrows(UnsupportedPlatformException.class, () -> Arch.parse("xxx"));
    }

    @Test void parseSuccess() {
        assertEquals(Arch.X86_64, Arch.parse("amd64"));
    }

    @Test void bitness() {
        assertAll(
            () -> assertEquals(32, Arch.X86_32.getBitness()),
            () -> assertTrue(Arch.X86_32.is32bit()),
            () -> assertFalse(Arch.X86_32.is64bit()),
            () -> assertEquals(64, Arch.X86_64.getBitness()),
            () -> assertFalse(Arch.X86_64.is32bit()),
            () -> assertTrue(Arch.X86_64.is64bit())
        );
    }
}
