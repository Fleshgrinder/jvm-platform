package com.fleshgrinder.platform;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * A platform is the combination of an {@link Os} and its {@link Arch}.
 *
 * <p>The platform contains both the OS and architecture because both are
 * required to make decisions for a native platform. For instance, Windows can
 * execute 32-bit software on a 64-bit architecture and Solaris can even execute
 * 64-bit software on a 32-bit architecture, however, BSD does not allow this.
 * Hence, looking at the OS and architecture in isolation is often not good
 * enough.
 *
 * <p>The {@link Env} on the other hand is not included in the platform because
 * it is only useful in certain special cases. It is also more involving to
 * determine compared to the OS and architecture and should thus only be used if
 * really required.
 */
public final class Platform implements Comparable<Platform>, Serializable {
    private static final long serialVersionUID = 1;

    private final @Nullable Os os;
    private final @Nullable Arch arch;
    private final @NotNull String id;

    /**
     * @param os of the platform, if any
     * @param arch of the platform, if any
     */
    public Platform(final @Nullable Os os, final @Nullable Arch arch) {
        this.os = os;
        this.arch = arch;
        this.id = (os == null ? "unknown" : os.getId()) + "-" + (arch == null ? "unknown-unknown" : arch.getId());
    }

    /**
     * Constructs new platform without {@link Os} and {@link Arch}.
     */
    public Platform() {
        this(null, null);
    }

    /**
     * Constructs new platform without {@link Arch}.
     *
     * @param os of the platform
     */
    public Platform(final @Nullable Os os) {
        this(os, null);
    }

    /**
     * Constructs new platform without {@link Os}.
     *
     * @param arch of the platform
     */
    public Platform(final @Nullable Arch arch) {
        this(null, arch);
    }

    /**
     * Gets the platform of the current JVM process.
     *
     * <p>Getting the current platform <strong>always</strong> succeeds
     * because {@code null} is used for the {@link #os} and {@link #arch} if
     * they cannot be determined.
     *
     * @return the current platform
     */
    @Contract(pure = true)
    public static @NotNull Platform current() {
        return new Platform(Os.currentOrNull(), Arch.currentOrNull());
    }

    /**
     * Parses the given value and constructs a new platform instance.
     *
     * <p>Parsing of a platform <strong>always</strong> succeeds because
     * {@code null} is used for the {@link #os} and {@link #arch} if they cannot
     * be parsed.
     *
     * @param value to parse
     * @return new platform
     */
    @Contract(pure = true)
    public static @NotNull Platform parse(final @NotNull CharSequence value) {
        return new Platform(Os.parseOrNull(value), Arch.parseOrNull(value));
    }

    /**
     * Gets all platforms.
     *
     * @return all possible platforms.
     */
    @Contract(pure = true)
    public static @NotNull Platform[] values() {
        final Os[] osValues = Os.values();
        final Arch[] archValues = Arch.values();
        final Platform[] values = new Platform[osValues.length * archValues.length];
        int i = -1;
        for (final Os os : osValues) for (final Arch arch : archValues) values[++i] = new Platform(os, arch);
        return values;
    }

    /**
     * @return operating system of this platform, if known
     */
    @Contract(pure = true)
    public @Nullable Os getOs() {
        return os;
    }

    /**
     * @return architecture of this platform, if known
     */
    @Contract(pure = true)
    public @Nullable Arch getArch() {
        return arch;
    }

    /**
     * Gets the canonical identifier of this platform.
     *
     * <p>The canonical identifier is a machine-readable and unique way to
     * identify a platform. The {@link Env} is not part of the platform because
     * it is only useful in certain situations, and a target platform can have
     * support for more than one {@link Env} at the same time, but it cannot be
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
     *
     * @return machine-readable identifier of this platform
     */
    @Contract(pure = true)
    public @NotNull String getId() {
        return id;
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
