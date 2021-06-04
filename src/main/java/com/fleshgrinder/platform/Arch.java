package com.fleshgrinder.platform;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

import static com.fleshgrinder.platform.Platform.normalize;

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
    IA_32,

    /**
     * Itanium 64-bit
     *
     * @see <a href="https://en.wikipedia.org/wiki/Itanium">Wikipedia</a>
     */
    IA_64,

    /**
     * IBM Z 32-bit (aka. s390)
     *
     * @see <a href="https://en.wikipedia.org/wiki/IBM_Z">Wikipedia</a>
     */
    IBMZ_32,

    /**
     * IBM Z 64-bit (aka. s390)
     *
     * @see <a href="https://en.wikipedia.org/wiki/IBM_Z">Wikipedia</a>
     */
    IBMZ_64,

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

    /**
     * @return current architecture
     * @throws UnsupportedPlatformException if unknown
     */
    @Contract(value = "-> new", pure = true)
    public static @NotNull Arch current() throws UnsupportedPlatformException {
        final Arch arch = currentOrNull();
        if (arch == null) {
            throw UnsupportedPlatformException.fromSystemProperty(Arch.class, "os.arch");
        }
        return arch;
    }

    /**
     * <p>The value to determine the architecture of the current platform is
     * retrieved from the {@code os.arch} system property. This means that the
     * returned architecture is the one reported by the JVM and not necessarily
     * the native architecture of the underlying system.
     *
     * <p>This means that the returned architecture does not necessarily reflect
     * the native architecture of the platform. In case of manipulation it could
     * be anything and thus also incompatible and subsequent failures have to be
     * expected but it could also be that the JVM is simply 32-bit and the
     * platform is 64-bit, or even vice versa.
     *
     * <p>Many platform support the execution of 32-bit software on 64-bit
     * platforms, and some (e.g., Solaris/Sparc) even 64-bit on 32-bit. This
     * is important because if the current JVM has a certain bitness then any
     * native library it is supposed to interact with also must have the same
     * matching bitness. The JVM, unlike some platforms, cannot mix the bitness.
     *
     * <p>It can be summarizes that the JVM is compiled for a certain target
     * platform that also defines the target architecture. Let the target be
     * {@code x86-32} and we execute the JVM with this target architecture on
     * a platform that is {@code x86-64}, then this function is going to return
     * {@code x86-32} and not {@code x86-64}. However, this also means that any
     * other native component that is {@code x86-32} is executable on this
     * platform and in case of JNI it must indeed be {@code x86-32} to be
     * compatible with the currently running JVM.
     *
     * <p>Note that the value of the {@code os.arch} system property can be
     * changed by a user, like any other system property.
     *
     * @return current architecture or {@code null} if unknown
     */
    @Contract(pure = true)
    @SuppressWarnings("ConstantConditions")
    public static @Nullable Arch currentOrNull() {
        final @NotNull String it = System.getProperty("os.arch", "");
        switch (normalize(it, true)) {
            case "alpha":
                return ALPHA_64;
            case "arm":
                return ARM_32;
            case "arm64":
            case "aarch64":
                return ARM_64;
            case "ia64":
                return IA_64;
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
                return IBMZ_32;
            case "s390x":
                return IBMZ_64;
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
     * @param value to parse
     * @return matching architecture
     * @throws NullPointerException if value is {@code null}
     * @throws UnsupportedPlatformException if unknown
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Arch parse(final @NotNull CharSequence value) throws UnsupportedPlatformException {
        final Arch arch = parseOrNull(value);
        if (arch == null) {
            throw UnsupportedPlatformException.fromValue(Arch.class, value);
        }
        return arch;
    }

    /**
     * Parses the given value and tries to map it to an architecture.
     *
     * @param value to parse
     * @return matching architecture or {@code null} if unknown
     * @throws NullPointerException if value is {@code null}
     */
    @Contract(pure = true)
    public static @Nullable Arch parseOrNull(final @NotNull CharSequence value) {
        if (value.length() > 0) {
            final String it = normalize(value);
            if (it.matches(".*(x86-?64|amd64|em64t|ia32e|(?<!nvpt)x64|[89]86).*")) {
                return X86_64;
            }
            if (it.matches(".*((ia|x)32|(x|[1-7])86|pentium).*")) {
                return X86_32;
            }
            if (it.matches(".*aarch-?(64)?-?(be|eb).*")) {
                return ARM_64_BE;
            }
            if (it.contains("aarch")) {
                return ARM_64;
            }
            if (it.matches(".*arm-?((64|v[8-9]|v[1-9]\\d+)-?(be|eb)|(be|eb)-?(64|v[8-9]|v[1-9]\\d+)).*")) {
                return ARM_64_BE;
            }
            if (it.matches(".*arm-?((32)?-?(be|eb)|(be|eb)).*")) {
                return ARM_32_BE;
            }
            if (it.matches(".*arm-?(64|v[8-9]|v[1-9]\\d+).*")) {
                return ARM_64;
            }
            if (it.contains("arm")) {
                return ARM_32;
            }
            if (it.contains("alpha")) {
                return ALPHA_64;
            }
            if (it.matches(".*i(a(-?32|64(n|-?32))|tanium-?32).*")) {
                return IA_32;
            }
            if (it.matches(".*i(a-?64|tanium).*")) {
                return IA_64;
            }
            if (it.contains("m68k") || it.contains("m68000")) {
                return M68K_32;
            }
            if (it.matches(".*(s390-?(x|64)|ibm-?z-?64).*")) {
                return IBMZ_64;
            }
            if (it.matches(".*(s390|ibm-?z).*")) {
                return IBMZ_32;
            }
            if (it.matches(".*(power-?(pc|rs)?|ppc)-?(64-?(le|el)|(le|el)-?64).*")) {
                return PPC_64_LE;
            }
            if (it.matches(".*(power-?(pc|rs)?|ppc)-?((32)?-?(le|el)|(le|el)).*")) {
                return PPC_32_LE;
            }
            if (it.matches(".*(power-?(pc|rs)?|ppc)-?64.*")) {
                return PPC_64;
            }
            if (it.matches(".*(power-?(pc|rs)?|ppc).*")) {
                return PPC_32;
            }
            if (it.matches(".*mips-?(64-?(le|el)|(le|el)-?64).*")) {
                return MIPS_64_LE;
            }
            if (it.matches(".*mips-?((32)?-?(le|el)|(le|el)).*")) {
                return MIPS_32_LE;
            }
            if (it.matches(".*mips-?64.*")) {
                return MIPS_64;
            }
            if (it.contains("mips")) {
                return MIPS_32;
            }
            if (it.matches(".*risc-?v-?64.*")) {
                return RISCV_64;
            }
            if (it.matches(".*risc-?v.*")) {
                return RISCV_32;
            }
            if (it.matches(".*(ultra-?sparc|sparc-?(64|v9)).*")) {
                return SPARC_64;
            }
            if (it.contains("sparc")) {
                return SPARC_32;
            }
            if (it.matches(".*s(uper)?h-?(32-?)?be.*")) {
                return SUPERH_32_BE;
            }
            if (it.contains("superh") || Pattern.matches("(?i)(^|.*[^\\w.])sh([^\\w].*|$)", value)) {
                return SUPERH_32;
            }
        }
        return null;
    }

    /**
     * @return bitness of this architecture, guaranteed to be positive
     */
    @Contract(pure = true)
    public int getBitness() {
        return is32bit() ? 32 : 64;
    }

    /**
     * @return {@code true} if this is a 32-bit architecture
     */
    @Contract(pure = true)
    public boolean is32bit() {
        return name().contains("32");
    }

    /**
     * @return {@code true} if this is a 64-bit architecture
     */
    @Contract(pure = true)
    public boolean is64bit() {
        return name().contains("64");
    }
}
