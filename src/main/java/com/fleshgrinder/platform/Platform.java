package com.fleshgrinder.platform;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * https://github.com/golang/go/blob/master/src/go/build/syslist.go
 * https://github.com/java-native-access/jna/blob/master/src/com/sun/jna/Platform.java
 * https://github.com/trustin/os-maven-plugin/blob/master/src/main/java/kr/motd/maven/os/Detector.java
 * https://doc.rust-lang.org/nightly/rustc/platform-support.html
 * http://lopica.sourceforge.net/os.html
 * https://jvm-gaming.org/t/possible-values-for-system-property-os-arch-and-os-name/27670/4
 * https://git.eclipse.org/c/equinox/rt.equinox.framework.git/tree/bundles/org.eclipse.osgi/container/src/org/eclipse/osgi/internal/framework/osname.aliases
 * https://github.com/xerial/snappy-java/blob/develop/src/main/java/org/xerial/snappy/OSInfo.java
 * https://github.com/openjdk/jdk/blob/master/make/autoconf/platform.m4
 * https://github.com/apache/commons-crypto/blob/master/src/main/java/org/apache/commons/crypto/OsInfo.java#L28
 * https://github.com/apache/commons-lang/blob/master/src/main/java/org/apache/commons/lang3/ArchUtils.java
 *
 * UNIXProcess.Platform#get()
 * UNIXProcess.Platform#helperPath()
 */
public final class Platform implements Comparable<Platform>, Serializable {
    private static final long serialVersionUID = 1;

    /**
     * Get the operating system of this platform, if known.
     */
    public final @Nullable Os os;

    /**
     * Get the architecture of this platform, if known.
     */
    public final @Nullable Arch arch;

    /**
     * Gets the canonical identifier of this platform.
     *
     * <p>The canonical identifier is a machine readable and unique way to
     * identify a platform. The {@link Env} is not part of the platform because
     * it is only useful in certain situations and a target platform can have
     * support for more than one {@link Env} at the same time but it cannot be
     * detected if this is the case, or not. Hence, a platform is made up only
     * of the {@link Os} and {@link Arch} of which both are fixed.
     *
     * <h2>Anatomy (ABNF)</h2>
     * <pre>
     * ID = OS "-" ARCH "-" BITNESS ["-" ENDIANESS]
     *
     * OS = SEGMENT
     * ARCH = SEGMENT
     * BITNESS = 1*DIGIT
     * ENDIANESS = "be" / "le" ; Big Endian / Little Endian
     *
     * SEGMENT = LOWER *(LOWER / DIGIT) ; [a-z][a-z0-9]*
     * LOWER = %x61–7A ; a-z
     * </pre>
     *
     * <h2>Examples</h2>
     * <ul>
     *     <li>{@code unknown-unknown-unknown}
     *     <li>{@code unknown-x86-64}
     *     <li>{@code linux-unknown-unknown}
     *     <li>{@code linux-x86-64}
     *     <li>{@code linux-arm-32-be}
     *     <li>{@code darwin-ppc-64-le}
     * </ul>
     */
    public final @NotNull String id;

    /**
     * Constructs new {@link Platform}.
     */
    public Platform(final @Nullable Os os, final @Nullable Arch arch) {
        this.os = os;
        this.arch = arch;
        if (os == null && arch == null) {
            this.id = "unknown-unknown-unknown";
        } else if (os == null) {
            this.id = "unknown-" + id(arch.name());
        } else if (arch == null) {
            this.id = id(os.name()) + "-unknown-unknown";
        } else {
            this.id = id(os.name()) + "-" + id(arch.name());
        }
    }

    /**
     * Constructs new {@link Platform} without {@link Os} and {@link Arch}.
     *
     * <p>Both {@link #os} and {@link #arch} will be {@code null} and the
     * {@link #id} is set to {@code unknown-unknown-unknown}.
     */
    public Platform() {
        this(null, null);
    }

    /**
     * Constructs new {@link Platform} without {@link Arch}.
     *
     * <p>The {@link #arch} will be {@code null} and the {@link #id} will have
     * {@code unknown-unknown} in place of the architecture.
     */
    public Platform(final @Nullable Os os) {
        this(os, null);
    }

    /**
     * Constructs new {@link Platform} without {@link Os}.
     *
     * <p>The {@link #os} will be {@code null} and the {@link #id} will have
     * {@code unknown} in place of the OS.
     */
    public Platform(final @Nullable Arch arch) {
        this(null, arch);
    }

    /**
     * Parses the given value and constructs a new {@link Platform} instance.
     *
     * <p>Parsing of a {@link Platform} <strong>always</strong> succeeds because
     * {@code null} is used for the {@link #os} and {@link #arch} if they cannot
     * be parsed.
     *
     * @param value to parse
     * @return new {@link Platform}
     */
    @Contract(pure = true)
    public static @NotNull Platform parse(final @NotNull CharSequence value) {
        return new Platform(Os.parseOrNull(value), Arch.parseOrNull(value));
    }

    /**
     * Gets all platforms.
     */
    @Contract(pure = true)
    public static @NotNull Platform[] values() {
        final Os[] osValues = Os.values();
        final Arch[] archValues = Arch.values();
        final Platform[] values = new Platform[osValues.length * archValues.length];
        int i = -1;
        for (final Os os : osValues) {
            for (final Arch arch : archValues) {
                values[++i] = new Platform(os, arch);
            }
        }
        return values;
    }

    /**
     * Transforms {@code UPPER_SNAKE_CASE} to {@code lower-dash-case}.
     */
    @Contract(pure = true)
    private static @NotNull String id(final @NotNull String chars) {
        final int l = chars.length();
        final StringBuilder sb = new StringBuilder(l);
        int i = 0;
        do {
            final char c = chars.charAt(i);
            if (c == '_') {
                sb.append('-');
            } else if ('A' <= c && c <= 'Z') {
                sb.append((char) (c + 32));
            } else {
                sb.append(c);
            }
        } while (++i < l);
        return sb.toString();
    }

    /**
     * Normalizes the given chars by lowering all upper chars and replacing all
     * non-alphanumeric chars with a dash ({@code -}).
     *
     * @param chars to normalize
     * @return normalized string
     * @throws NullPointerException if {@code chars} is {@code null}
     * @see #normalize(CharSequence, boolean)
     */
    @Contract(pure = true)
    static @NotNull String normalize(final @NotNull CharSequence chars) {
        return normalize(chars, false);
    }

    /**
     * Normalizes the given chars by lowering all upper chars and either
     * stripping or replacing all non-alphanumeric chars.
     *
     * @param chars to normalize
     * @param strip non-alphanumeric chars ({@code true}) or replace them with a
     *     dash ({@code -}, {@code false})
     * @return normalized string
     * @throws NullPointerException if {@code chars} is {@code null}
     */
    @Contract(pure = true)
    static @NotNull String normalize(final @NotNull CharSequence chars, final boolean strip) {
        final int l = chars.length();
        if (l == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder(l);
        int i = 0;
        do {
            final char c = chars.charAt(i);
            if (('a' <= c && c <= 'z') || ('0' <= c && c <= '9')) {
                sb.append(c);
            } else if ('A' <= c && c <= 'Z') {
                sb.append((char) (c + 32));
            } else if (!strip) {
                sb.append('-');
            }
        } while (++i < l);
        return sb.toString();
    }

    @Contract(pure = true)
    @Override public int compareTo(final @NotNull Platform other) {
        return id.compareTo(other.id);
    }

    @Contract(pure = true)
    @Override public boolean equals(final @Nullable Object other) {
        return this == other || (other instanceof Platform && id.equals(((Platform) other).id));
    }

    @Contract(pure = true)
    @Override public int hashCode() {
        return id.hashCode();
    }

    @Contract(pure = true)
    @Override public @NotNull String toString() {
        return id;
    }
}
