package com.fleshgrinder.platform;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

/**
 * Platform is the combination of {@link Os} and {@link Arch}.
 */
public final class Platform implements Comparable<Platform>, Serializable {
    private static final long serialVersionUID = 1;

    private final @NotNull Os os;
    private final @NotNull Arch arch;
    private final @NotNull String id;

    /**
     * Constructs a new platform.
     *
     * @param os of the platform.
     * @param arch of the platform.
     * @throws NullPointerException if any argument is {@code null}.
     */
    public Platform(final @NotNull Os os, final @NotNull Arch arch) {
        this.os = os;
        this.arch = arch;
        this.id = os + "-" + arch;
    }

    /**
     * Gets the platform of the current JVM process.
     *
     * @return the current platform.
     * @throws IllegalStateException if it cannot be determined.
     * @see #currentOrNull()
     * @see Os#current()
     * @see Arch#current()
     */
    @Contract(pure = true)
    public static @NotNull Platform current() throws IllegalStateException {
        try {
            return new Platform(Os.current(), Arch.current());
        } catch (final IllegalStateException cause) {
            throw new IllegalStateException("Unknown platform", cause);
        }
    }

    /**
     * Gets the platform of the current JVM process.
     *
     * @return the current platform or {@code null} if it cannot be determined.
     * @see #current()
     * @see Os#currentOrNull()
     * @see Arch#currentOrNull()
     */
    @Contract(pure = true)
    public static @Nullable Platform currentOrNull() {
        final Os os = Os.currentOrNull();
        final Arch arch = Arch.currentOrNull();
        return os != null && arch != null ? new Platform(os, arch) : null;
    }

    /**
     * Gets the platform whose string matches the given value.
     *
     * <p>This method is strict and only accepts values that perfectly match the
     * strings as described in {@link #toString}. Use {@link #parse} for a
     * lenient approach that accept arbitrary input.
     *
     * @param value to get the platform for.
     * @return the matching platform.
     * @throws IllegalArgumentException if no match is found.
     * @throws NullPointerException if the given value is {@code null}.
     * @see #fromStringOrNull(String)
     * @see #parse(CharSequence)
     * @see #parseOrNull(CharSequence)
     * @see #toString()
     * @see Arch#fromString(String)
     * @see Os#fromString(String)
     */
    @Contract(pure = true)
    public static @NotNull Platform fromString(final @NotNull String value) throws IllegalArgumentException {
        final Platform platform = fromStringOrNull(value);
        if (platform == null) throw new IllegalArgumentException("Unknown platform: " + value);
        return platform;
    }

    /**
     * Gets the platform whose string matches the given value.
     *
     * <p>This method is strict and only accepts values that perfectly match the
     * strings as described in {@link #toString}. Use {@link #parse} or
     * {@link #parseOrNull} for a lenient approach that accepts arbitrary
     * input.
     *
     * @param value to get the platform for.
     * @return the matching platform or {@code null} if no match is found.
     * @throws NullPointerException if the given value is {@code null}.
     * @see #parse(CharSequence)
     * @see #parseOrNull(CharSequence)
     * @see #fromString(String)
     * @see #toString()
     * @see Arch#fromStringOrNull(String)
     * @see Os#fromStringOrNull(String)
     */
    @Contract(pure = true)
    public static @Nullable Platform fromStringOrNull(final @NotNull String value) {
        final int len = value.length();
        if (len > 0) {
            final int i = value.indexOf('-');
            final int j = i + 1;
            if (i > 0 && j < (len - 1)) {
                final Os os = Os.fromStringOrNull(value.substring(0, i));
                final Arch arch = Arch.fromStringOrNull(value.substring(j, len));
                if (os != null && arch != null) return new Platform(os, arch);
            }
        }
        return null;
    }

    /**
     * Parses the given value and constructs a new platform instance.
     *
     * @param value to parse.
     * @return parsed platform.
     * @throws IllegalArgumentException if parsing fails.
     * @throws NullPointerException if the given value is {@code null}.
     * @see #fromString(String)
     * @see #fromStringOrNull(String)
     * @see #parseOrNull(CharSequence)
     * @see Os#parse(CharSequence)
     * @see Arch#parse(CharSequence)
     */
    @Contract(pure = true)
    public static @NotNull Platform parse(final @NotNull CharSequence value) throws IllegalArgumentException {
        try {
            return new Platform(Os.parse(value), Arch.parse(value));
        } catch (final IllegalArgumentException cause) {
            throw new IllegalArgumentException("Unknown platform: " + value, cause);
        }
    }

    /**
     * Parses the given value and constructs a new platform instance.
     *
     * @param value to parse.
     * @return parsed platform or {@code null} if fails.
     * @throws NullPointerException if the given value is {@code null}.
     * @see #fromString(String)
     * @see #fromStringOrNull(String)
     * @see #parse(CharSequence)
     * @see Os#parseOrNull(CharSequence)
     * @see Arch#parseOrNull(CharSequence)
     */
    @Contract(pure = true)
    public static @Nullable Platform parseOrNull(final @NotNull CharSequence value) {
        final Os os = Os.parseOrNull(value);
        final Arch arch = Arch.parseOrNull(value);
        return os != null && arch != null ? new Platform(os, arch) : null;
    }

    /**
     * Gets whether the current platform has musl, or not.
     *
     * <p>Just because the default {@code ldd} in {@code PATH} points to musl
     * does not necessarily mean that no another C standard library is
     * available, however, it is a strong indicator that this platform requires
     * native executables that are either static or compiled against musl.
     *
     * @return {@code true} if the current platform has musl.
     * @see <a href="https://en.wikipedia.org/wiki/Musl">Wikipedia</a>
     */
    @Contract(pure = true)
    public static boolean hasMusl() {
        return hasMusl("ldd");
    }

    /** @see #hasMusl() */
    @Contract(pure = true)
    @VisibleForTesting
    static boolean hasMusl(final @NotNull String lddPath) {
        final Process proc;
        try {
            proc = new ProcessBuilder(lddPath, "--version").start();
        } catch (final IOException ignored) {
            return false;
        }

        final StringBuilder sb = new StringBuilder(40);
        try {
            if (proc.waitFor(1, TimeUnit.SECONDS)) {
                try (final InputStream s = proc.getInputStream()) {
                    int c;
                    while ((c = s.read()) != -1 && c != '\n') {
                        if ('A' <= c && c <= 'Z') sb.append((char) (c + 32));
                        else sb.append((char) c);
                    }
                }
            }
        } catch (final InterruptedException | IOException ignored) {
            return false;
        } finally {
            proc.destroyForcibly(); // closes all streams
        }

        return sb.toString().matches("(?s).*\\bmusl\\b.*");
    }

    @Contract(pure = true)
    public @NotNull Os getOs() {
        return os;
    }

    @Contract(pure = true)
    public @NotNull Arch getArch() {
        return arch;
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

    /**
     * Gets the canonical machine-readable identifier of this platform.
     *
     * <p>The anatomy of the returned string <strong>always</strong> matches the
     * following regular expression:
     *
     * <pre>{@code ^(?<os>[a-z][a-z0-9]*)-(?<arch>[a-z][a-z0-9]*-[1-9][0-9]*(-(be|le))?)$}</pre>
     *
     * <p>Examples:
     * <ul>
     *     <li>{@code linux-x86-64}
     *     <li>{@code linux-arm-32-be}
     *     <li>{@code darwin-ppc-64-le}
     *     <li>{@code windows-itanium-64}
     * </ul>
     *
     * @return {@link Os#toString} and {@link Arch#toString} joined with a dash
     *     ({@code -}).
     * @see #fromString(String)
     * @see #fromStringOrNull(String)
     * @see Arch#toString()
     * @see Os#toString()
     */
    @Contract(pure = true)
    @Override public @NotNull String toString() {
        return id;
    }
}
