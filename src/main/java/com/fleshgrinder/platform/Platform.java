package com.fleshgrinder.platform;

import java.io.Serializable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    /**
     * Gets the operating system of this platform.
     */
    public final @NotNull Os os;

    /**
     * Gets the architecture (and bitness) of this platform.
     */
    public final @NotNull Arch arch;


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
     * <h4>Anatomy (ABNF)</h4>
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
     * <h4>Examples</h4>
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
     * @param os of the platform
     * @param arch of the platform
     */
    public Platform(final @NotNull Os os, final @NotNull Arch arch) {
        this.os = os;
        this.arch = arch;
        this.id = os.id + "-" + arch.id;
    }

    /**
     * Constructs new platform with {@link Os#UNKNOWN} and
     * {@link Arch#UNKNOWN_UNKNOWN}.
     */
    public Platform() {
        this(Os.UNKNOWN, Arch.UNKNOWN_UNKNOWN);
    }

    /**
     * Constructs new platform with {@link Arch#UNKNOWN_UNKNOWN}.
     *
     * @param os of the platform
     */
    public Platform(final @NotNull Os os) {
        this(os, Arch.UNKNOWN_UNKNOWN);
    }

    /**
     * Constructs new platform with {@link Os#UNKNOWN}.
     *
     * @param arch of the platform
     */
    public Platform(final @NotNull Arch arch) {
        this(Os.UNKNOWN, arch);
    }

    /**
     * Gets the platform of the current JVM process.
     *
     * @return the current platform
     */
    @Contract(pure = true)
    public static @NotNull Platform current() {
        return new Platform(Os.current(), Arch.current());
    }

    /**
     * Parses the given value and constructs a new platform instance.
     *
     * @param value to parse
     * @return new platform
     */
    @Contract(pure = true)
    public static @NotNull Platform parse(final @NotNull CharSequence value) {
        return new Platform(Os.parse(value), Arch.parse(value));
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
