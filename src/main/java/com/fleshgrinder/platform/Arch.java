package com.fleshgrinder.platform;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.fleshgrinder.platform.Utils.id;
import static com.fleshgrinder.platform.Utils.normalize;

/**
 * Architecture
 *
 * @see <a href="https://en.wikipedia.org/wiki/Computer_architecture">Wikipedia</a>
 */
@SuppressWarnings("SpellCheckingInspection")
public enum Arch {
    /**
     * DEC Alpha 64-bit
     *
     * @see <a href="https://en.wikipedia.org/wiki/DEC_Alpha">Wikipedia</a>
     */
    ALPHA_64,

    /**
     * ARM v1+ 32-bit Little Endian
     *
     * @see <a href="https://en.wikipedia.org/wiki/ARM_architecture">Wikipedia</a>
     */
    ARM_32,

    /**
     * ARM v1+ 32-bit Big Endian
     *
     * @see <a href="https://en.wikipedia.org/wiki/ARM_architecture">Wikipedia</a>
     */
    ARM_32_BE,

    /**
     * ARM v8+ 64-bit Little Endian
     *
     * @see <a href="https://en.wikipedia.org/wiki/ARM_architecture">Wikipedia</a>
     */
    ARM_64,

    /**
     * ARM v8+ 64-bit Big Endian
     *
     * @see <a href="https://en.wikipedia.org/wiki/ARM_architecture">Wikipedia</a>
     */
    ARM_64_BE,

    /**
     * Itanium 32-bit
     *
     * @see <a href="https://en.wikipedia.org/wiki/Itanium">Wikipedia</a>
     */
    ITANIUM_32,

    /**
     * Itanium 64-bit
     *
     * @see <a href="https://en.wikipedia.org/wiki/Itanium">Wikipedia</a>
     */
    ITANIUM_64,

    /**
     * Motorola 68000 32-bit
     *
     * @see <a href="https://en.wikipedia.org/wiki/Motorola_68000">Wikipedia</a>
     */
    M68K_32,

    /**
     * MIPS 32-bit Big Endian
     *
     * @see <a href="https://en.wikipedia.org/wiki/MIPS_architecture">Wikipedia</a>
     */
    MIPS_32,

    /**
     * MIPS 32-bit Little Endian
     *
     * @see <a href="https://en.wikipedia.org/wiki/MIPS_architecture">Wikipedia</a>
     */
    MIPS_32_LE,

    /**
     * MIPS 64-bit Big Endian
     *
     * @see <a href="https://en.wikipedia.org/wiki/MIPS_architecture">Wikipedia</a>
     */
    MIPS_64,

    /**
     * MIPS 64-bit Little Endian
     *
     * @see <a href="https://en.wikipedia.org/wiki/MIPS_architecture">Wikipedia</a>
     */
    MIPS_64_LE,

    /**
     * PowerPC 32-bit Big Endian
     *
     * @see <a href="https://en.wikipedia.org/wiki/PowerPC">Wikipedia</a>
     */
    PPC_32,

    /**
     * PowerPC 32-bit Little Endian
     *
     * @see <a href="https://en.wikipedia.org/wiki/PowerPC">Wikipedia</a>
     */
    PPC_32_LE,

    /**
     * PowerPC 64-bit Big Endian
     *
     * @see <a href="https://en.wikipedia.org/wiki/PowerPC">Wikipedia</a>
     */
    PPC_64,

    /**
     * PowerPC 64-bit Little Endian
     *
     * @see <a href="https://en.wikipedia.org/wiki/PowerPC">Wikipedia</a>
     */
    PPC_64_LE,

    /**
     * RISC-V 32-bit
     *
     * @see <a href="https://en.wikipedia.org/wiki/RISC-V">Wikipedia</a>
     */
    RISCV_32,

    /**
     * RISC-V 64-bit
     *
     * @see <a href="https://en.wikipedia.org/wiki/RISC-V">Wikipedia</a>
     */
    RISCV_64,

    /**
     * s390 32-bit (IBM Z)
     *
     * @see <a href="https://en.wikipedia.org/wiki/IBM_Z">Wikipedia</a>
     */
    S390_32,

    /**
     * s390x 64-bit (IBM Z)
     *
     * @see <a href="https://en.wikipedia.org/wiki/IBM_Z">Wikipedia</a>
     */
    S390_64,

    /**
     * SPARC 32-bit
     *
     * @see <a href="https://en.wikipedia.org/wiki/SPARC">Wikipedia</a>
     */
    SPARC_32,

    /**
     * SPARC 64-bit
     *
     * @see <a href="https://en.wikipedia.org/wiki/SPARC">Wikipedia</a>
     */
    SPARC_64,

    /**
     * SuperH 32-bit Little Endian
     *
     * @see <a href="https://en.wikipedia.org/wiki/SuperH">Wikipedia</a>
     */
    SUPERH_32,

    /**
     * SuperH 32-bit Big Endian
     *
     * @see <a href="https://en.wikipedia.org/wiki/SuperH">Wikipedia</a>
     */
    SUPERH_32_BE,

    /**
     * x86 32-bit
     *
     * @see <a href="https://en.wikipedia.org/wiki/X86">Wikipedia</a>
     */
    X86_32,

    /**
     * x86 64-bit
     *
     * @see <a href="https://en.wikipedia.org/wiki/X86">Wikipedia</a>
     */
    X86_64;

    /** @see #toString() */
    private final @NotNull String id = id(name());

    /**
     * Gets the arch of the current platform.
     *
     * <p>The value to determine the arch of the current platform is retrieved
     * from the {@code os.arch} system property. This means that the returned
     * arch is the one reported by the JVM and not necessarily the native arch
     * of the underlying system.
     *
     * <p>This means that the returned arch does not necessarily reflect the
     * native arch of the platform. In case of manipulation it could be anything
     * and thus also incompatible and subsequent failures have to be expected,
     * but it could also be that the JVM is simply 32-bit and the platform is
     * 64-bit, or even vice versa.
     *
     * <p>Many platforms support the execution of 32-bit software on 64-bit
     * platforms, and some (e.g., Solaris/Sparc) even 64-bit on 32-bit. This
     * is important because if the current JVM has a certain bitness then any
     * native library it is supposed to interact with also must have the same
     * matching bitness. The JVM, unlike some platforms, cannot mix the bitness.
     *
     * <p>It can be summarized that the JVM is compiled for a certain target
     * platform that also defines the target arch. Let the target be
     * {@code x86-32} and we execute the JVM with this target arch on a platform
     * that is {@code x86-64}, then this function is going to return
     * {@code x86-32} and not {@code x86-64}. However, this also means that any
     * other native component that is {@code x86-32} is executable on this
     * platform and in case of JNI it must indeed be {@code x86-32} to be
     * compatible with the currently running JVM.
     *
     * <p>Note that the value of the {@code os.arch} system property can be
     * changed by a user, like any other system property.
     *
     * @return the current arch.
     * @throws IllegalStateException if it cannot be determined.
     * @see #currentOrNull()
     */
    @Contract(pure = true)
    public static @NotNull Arch current() throws IllegalStateException {
        final Arch arch = currentOrNull();
        if (arch == null) throw new IllegalStateException("Unknown architecture: " + System.getProperty("os.arch", "missing 'os.arch' system property"));
        return arch;
    }

    /**
     * Gets the arch of the current platform.
     *
     * <p>The value to determine the arch of the current platform is retrieved
     * from the {@code os.arch} system property. This means that the returned
     * arch is the one reported by the JVM and not necessarily the native arch
     * of the underlying system.
     *
     * <p>This means that the returned arch does not necessarily reflect the
     * native arch of the platform. In case of manipulation it could be anything
     * and thus also incompatible and subsequent failures have to be expected,
     * but it could also be that the JVM is simply 32-bit and the platform is
     * 64-bit, or even vice versa.
     *
     * <p>Many platforms support the execution of 32-bit software on 64-bit
     * platforms, and some (e.g., Solaris/SPARC) even 64-bit on 32-bit. This
     * is important because if the current JVM has a certain bitness then any
     * native library it is supposed to interact with also must have the same
     * matching bitness. The JVM, unlike some platforms, cannot mix the bitness.
     *
     * <p>It can be summarized that the JVM is compiled for a certain target
     * platform that also defines the target arch. Let the target be
     * {@code x86-32} and we execute the JVM with this target arch on a platform
     * that is {@code x86-64}, then this function is going to return
     * {@code x86-32} and not {@code x86-64}. However, this also means that any
     * other native component that is {@code x86-32} is executable on this
     * platform and in case of JNI it must indeed be {@code x86-32} to be
     * compatible with the currently running JVM.
     *
     * <p>Note that the value of the {@code os.arch} system property can be
     * changed by a user, like any other system property.
     *
     * @return the current arch or {@code null} if it cannot be determined.
     * @see #current()
     */
    @Contract(pure = true)
    public static @Nullable Arch currentOrNull() {
        final @Nullable String it = System.getProperty("os.arch");
        if (it == null) return null;
        switch (normalize(it, true)) {
            case "alpha":
                return ALPHA_64;
            case "arm":
                return ARM_32;
            case "arm64":
            case "aarch64":
                return ARM_64;
            case "ia64":
                return ITANIUM_64;
            case "m68k":
                return M68K_32;
            case "mips":
                return MIPS_32;
            case "mipsel":
                return MIPS_32_LE;
            case "mips64":
                return MIPS_64;
            case "mips64el":
                return MIPS_64_LE;
            case "ppc":
                return PPC_32;
            case "ppcle":
                return PPC_32_LE;
            case "ppc64":
                return PPC_64;
            case "ppc64le":
                return PPC_64_LE;
            case "s390":
                return S390_32;
            case "s390x":
                return S390_64;
            case "sparc":
                return SPARC_32;
            case "sparcv9":
                return SPARC_64;
            case "sh":
                return SUPERH_32;
            case "shbe":
                return SUPERH_32_BE;
            case "x8664":
            case "amd64":
                return X86_64;
            case "x86":
            case "i386":
            case "pentium":
                return X86_32;
        }
        return parseOrNull(it);
    }

    /**
     * Gets the arch whose string matches the given value.
     *
     * <p>This method is strict and only accepts values that perfectly match the
     * strings as described in {@link #toString}. Use {@link #parse} or
     * {@link #parseOrNull} for a lenient approach that accept arbitrary input.
     *
     * @param value to get the arch for.
     * @return the matching arch.
     * @throws IllegalArgumentException if no match is found.
     * @throws NullPointerException if the given value is {@code null}.
     * @see #fromStringOrNull(String)
     * @see #parse(CharSequence)
     * @see #parseOrNull(CharSequence)
     * @see #toString()
     * @see #valueOf(String)
     */
    @Contract(pure = true)
    public static @NotNull Arch fromString(final @NotNull String value) throws IllegalArgumentException {
        final Arch arch = fromStringOrNull(value);
        if (arch == null) throw new IllegalArgumentException("Unknown architecture: " + value);
        return arch;
    }

    /**
     * Gets the arch whose string matches the given value.
     *
     * <p>This method is strict and only accepts values that perfectly match the
     * strings as described in {@link #toString}. Use {@link #parse} or
     * {@link #parseOrNull} for a lenient approach that accept arbitrary input.
     *
     * @param value to get the arch for.
     * @return the matching arch or {@code null} if no match is found.
     * @throws NullPointerException if the given value is {@code null}.
     * @see #fromString(String)
     * @see #parse(CharSequence)
     * @see #parseOrNull(CharSequence)
     * @see #toString()
     * @see #valueOf(String)
     */
    @Contract(pure = true)
    public static @Nullable Arch fromStringOrNull(final @NotNull String value) {
        for (final Arch arch : Arch.values()) if (arch.id.equals(value)) return arch;
        return null;
    }

    /**
     * Parses the given value and tries to match it with an arch.
     *
     * @param value to parse and match.
     * @return the matching arch.
     * @throws IllegalArgumentException if no matching arch is found.
     * @throws NullPointerException if the given value is {@code null}.
     * @see #fromString(String)
     * @see #fromStringOrNull(String)
     * @see #parseOrNull(CharSequence)
     * @see #valueOf(String)
     */
    @Contract(pure = true)
    public static @NotNull Arch parse(final @NotNull CharSequence value) throws IllegalArgumentException {
        final Arch arch = parseOrNull(value);
        if (arch == null) throw new IllegalArgumentException("Unknown architecture: " + value);
        return arch;
    }

    /**
     * Parses the given value and tries to match it with an arch.
     *
     * @param value to parse and match.
     * @return the matching arch or {@code null} if no match is found.
     * @throws NullPointerException if the given value is {@code null}.
     * @see #fromString(String)
     * @see #fromStringOrNull(String)
     * @see #parse(CharSequence)
     * @see #valueOf(String)
     */
    @Contract(pure = true)
    public static @Nullable Arch parseOrNull(final @NotNull CharSequence value) {
        if (value.length() > 0) {
            final String it = normalize(value);
            for (final Arch arch : Arch.values()) if (arch.id.equals(it)) return arch;
            // region common
            if (it.matches("(?s).*\\b((amd|x(86)?)-?64|em64t|i[89]86|ia32e)\\b.*?")) return X86_64;
            if (it.matches("(?s).*\\b((ia|x)32|(i[1-7]|x)86|pentium)\\b.*?")) return X86_32;
            if (it.matches("(?s).*\\b(aarch(-?64)?-?(be|eb)(-?64)?|arm-?(64-?(eb|be)|(eb|be)-?64))\\b.*?")) return ARM_64_BE;
            if (it.matches("(?s).*\\barm(-?32)?-?(be|eb)(32)?\\b.*?")) return ARM_32_BE;
            if (it.matches("(?s).*\\ba(arch|rm)-?(64|v([8-9]|[1-9]\\d+))\\b.*?")) return ARM_64;
            if (it.matches("(?s).*\\barm(-?32|-?v([1-36]|4t?|5(te)?|7e?)?)?\\b.*?")) return ARM_32;
            // endregion common
            // region uncommon
            if (it.matches("(?s).*\\bi(a-?64-?(32|n)|tanium-?32)\\b.*?")) return ITANIUM_32;
            if (it.matches("(?s).*\\bi(a-?64|tanium(64)?)\\b.*?")) return ITANIUM_64;
            if (it.matches("(?s).*\\bm68(k|000)\\b.*?")) return M68K_32;
            if (it.matches("(?s).*\\bmips-?(64-?(le|el)|(le|el)-?64)\\b.*?")) return MIPS_64_LE;
            if (it.matches("(?s).*\\bmips(-?32)?-?(le|el)(32)?\\b.*?")) return MIPS_32_LE;
            if (it.matches("(?s).*\\bmips-?64\\b.*?")) return MIPS_64;
            if (it.matches("(?s).*\\bmips(32)?\\b.*?")) return MIPS_32;
            if (it.matches("(?s).*\\bp(ower(-?(pc|rs))?|pc)-?(64-?(le|el)|(le|el)-?64)\\b.*?")) return PPC_64_LE;
            if (it.matches("(?s).*\\bp(ower(-?(pc|rs))?|pc)(-?32)?-?(le|el)(32)?\\b.*?")) return PPC_32_LE;
            if (it.matches("(?s).*\\bp(ower(-?(pc|rs))?|pc)-?64\\b.*?")) return PPC_64;
            if (it.matches("(?s).*\\bp(ower(-?(pc|rs))?|pc)(32)?\\b.*?")) return PPC_32;
            if (it.matches("(?s).*\\brisc-?v-?64\\b.*?")) return RISCV_64;
            if (it.matches("(?s).*\\brisc-?v(32)?\\b.*?")) return RISCV_32;
            if (it.matches("(?s).*\\b(s390(x(64)?|-?64)|ibm-?z-?64)\\b.*?")) return S390_64;
            if (it.matches("(?s).*\\b(s390|ibm-?z)(32)?\\b.*?")) return S390_32;
            if (it.matches("(?s).*\\b(sparc-?(64|v(9|[1-9]\\d+))|ultra-?sparc)\\b.*?")) return SPARC_64;
            if (it.matches("(?s).*\\b(hyper|micro|super|turbo)?sparc(32)?\\b.*?")) return SPARC_32;
            if (it.matches("(?s).*\\bs(uper)?h(-?32)?-?(be|eb)(32)?\\b.*?")) return SUPERH_32_BE;
            if (it.matches("(?s).*\\b(superh(32)?|sh-?32)\\b.*?")) return SUPERH_32;
            if (it.matches("(?s).*\\b(dec)?alpha(64)?\\b.*?")) return ALPHA_64;
            // endregion uncommon
            // region special
            // These are not real arch identifiers but used by some vendors to
            // indicate the OS and arch at once, this MUST come last because
            // Windows has support for other archs, and we can only make this
            // assumption if absolutely nothing else matched.
            if (it.matches("(?s).*\\bw(in)?-?32\\b.*?")) return X86_32;
            if (it.matches("(?s).*\\bw(in)?-?64\\b.*?")) return X86_64;
            // endregion special
        }
        return null;
    }

    /**
     * Gets the bitness of this arch.
     *
     * @return the positive and non-zero bitness of this arch.
     * @see #is32bit()
     * @see #is64bit()
     */
    @Contract(pure = true)
    public int getBitness() {
        return is32bit() ? 32 : 64;
    }

    /**
     * @return {@code true} if this is a 32-bit arch.
     * @see #getBitness()
     * @see #is64bit()
     */
    @Contract(pure = true)
    public boolean is32bit() {
        return name().contains("32");
    }

    /**
     * @return {@code true} if this is a 64-bit arch.
     * @see #getBitness()
     * @see #is32bit()
     */
    @Contract(pure = true)
    public boolean is64bit() {
        return name().contains("64");
    }

    /**
     * @return {@code true} if this is any of the ARM archs.
     * @see #ARM_32
     * @see #ARM_64
     * @see #ARM_32_BE
     * @see #ARM_64_BE
     * @see #isArmBe()
     * @see #isArmLe()
     */
    @Contract(pure = true)
    public boolean isArm() {
        return isArmBe() || isArmLe();
    }

    /**
     * @return {@code true} if this is any of the ARM big endian archs.
     * @see #ARM_32_BE
     * @see #ARM_64_BE
     * @see #isArm()
     * @see #isArmLe()
     */
    @Contract(pure = true)
    public boolean isArmBe() {
        return this == ARM_32_BE || this == ARM_64_BE;
    }

    /**
     * @return {@code true} if this is any of the ARM little endian archs.
     * @see #ARM_32
     * @see #ARM_64
     * @see #isArm()
     * @see #isArmBe()
     */
    @Contract(pure = true)
    public boolean isArmLe() {
        return this == ARM_32 || this == ARM_64;
    }

    /**
     * @return {@code true} if this is any of the Itanium archs.
     * @see #ITANIUM_32
     * @see #ITANIUM_64
     */
    @Contract(pure = true)
    public boolean isItanium() {
        return this == ITANIUM_32 || this == ITANIUM_64;
    }

    /**
     * @return {@code true} if this is any of the MIPS archs.
     * @see #MIPS_32
     * @see #MIPS_64
     * @see #MIPS_32_LE
     * @see #MIPS_64_LE
     * @see #isMipsBe()
     * @see #isMipsLe()
     */
    @Contract(pure = true)
    public boolean isMips() {
        return isMipsBe() || isMipsLe();
    }

    /**
     * @return {@code true} if this is any of the MIPS big endian archs.
     * @see #MIPS_32
     * @see #MIPS_64
     * @see #isMips()
     * @see #isMipsLe()
     */
    @Contract(pure = true)
    public boolean isMipsBe() {
        return this == MIPS_32 || this == MIPS_64;
    }

    /**
     * @return {@code true} if this is any of the MIPS little endian archs.
     * @see #MIPS_32_LE
     * @see #MIPS_64_LE
     * @see #isMips()
     * @see #isMipsBe()
     */
    @Contract(pure = true)
    public boolean isMipsLe() {
        return this == MIPS_32_LE || this == MIPS_64_LE;
    }

    /**
     * @return {@code true} if this is any of the PowerPC archs.
     * @see #PPC_32
     * @see #PPC_64
     * @see #PPC_32_LE
     * @see #PPC_64_LE
     * @see #isPpcBe()
     * @see #isPpcLe()
     */
    @Contract(pure = true)
    public boolean isPpc() {
        return isPpcBe() || isPpcLe();
    }

    /**
     * @return {@code true} if this is any of the PowerPC big endian archs.
     * @see #PPC_32
     * @see #PPC_64
     * @see #isPpc()
     * @see #isPpcLe()
     */
    @Contract(pure = true)
    public boolean isPpcBe() {
        return this == PPC_32 || this == PPC_64;
    }

    /**
     * @return {@code true} if this is any of the PowerPC little endian archs.
     * @see #PPC_32_LE
     * @see #PPC_64_LE
     * @see #isPpc()
     * @see #isPpcBe()
     */
    @Contract(pure = true)
    public boolean isPpcLe() {
        return this == PPC_32_LE || this == PPC_64_LE;
    }

    /**
     * @return {@code true} if this is any of the RISC-V archs.
     * @see #RISCV_32
     * @see #RISCV_64
     */
    @Contract(pure = true)
    public boolean isRiscv() {
        return this == RISCV_32 || this == RISCV_64;
    }

    /**
     * @return {@code true} if this is any of the s390 (IBM Z) archs.
     * @see #S390_32
     * @see #S390_64
     */
    @Contract(pure = true)
    public boolean isS390() {
        return this == S390_32 || this == S390_64;
    }

    /**
     * @return {@code true} if this is any of the SPARC archs.
     * @see #SPARC_32
     * @see #SPARC_64
     */
    @Contract(pure = true)
    public boolean isSparc() {
        return this == SPARC_32 || this == SPARC_64;
    }

    /**
     * @return {@code true} if this is any of the x86 archs.
     * @see #X86_32
     * @see #X86_64
     */
    @Contract(pure = true)
    public boolean isX86() {
        return this == X86_32 || this == X86_64;
    }

    /**
     * Gets the canonical machine-readable identifier of this arch.
     *
     * <p>The anatomy of the returned string <strong>always</strong> matches the
     * following regular expression:
     *
     * <pre>{@code ^[a-z][a-z0-9]*-[1-9][0-9]*(-(be|le))?$}</pre>
     *
     * @return {@link #name()} in {@code lower-dash-case}.
     * @see #fromString(String)
     * @see #fromStringOrNull(String)
     */
    @Contract(pure = true)
    @Override public @NotNull String toString() {
        return id;
    }
}
