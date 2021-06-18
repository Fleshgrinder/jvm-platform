package com.fleshgrinder.platform;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import static com.fleshgrinder.platform.Utils.id;
import static com.fleshgrinder.platform.Utils.normalize;

/**
 * Environment
 *
 * <p>A platform's environment defines the ABI/libc requirements of shared
 * libraries or dynamic executables.
 *
 * @see <a href="https://en.wikipedia.org/wiki/C_standard_library">Wikipedia</a>
 */
@SuppressWarnings("SpellCheckingInspection")
public enum Env {
    UNKNOWN,

    /**
     * Bionic
     *
     * @see <a href="https://en.wikipedia.org/wiki/Bionic_(software)">Wikipedia</a>
     */
    BIONIC,

    /**
     * BSD libc
     *
     * @see <a href="https://en.wikipedia.org/wiki/BSD_libc">Wikipedia</a>
     */
    BSDLIBC,

    /**
     * dietlibc
     *
     * @see <a href="https://en.wikipedia.org/wiki/Dietlibc">Wikipedia</a>
     */
    DIETLIBC,

    /**
     * GNU C Library
     *
     * @see <a href="https://en.wikipedia.org/wiki/GNU_C_Library">Wikipedia</a>
     */
    GLIBC,

    /**
     * klibc
     *
     * @see <a href="https://en.wikipedia.org/wiki/Klibc">Wikipedia</a>
     */
    KLIBC,

    /**
     * Microsoft Visual C++
     *
     * @see <a href="https://en.wikipedia.org/wiki/Microsoft_Visual_C++">Wikipedia</a>
     */
    MSVC,

    /**
     * musl
     *
     * @see <a href="https://en.wikipedia.org/wiki/Musl">Wikipedia</a>
     */
    MUSL,

    /**
     * Newlib
     *
     * @see <a href="https://en.wikipedia.org/wiki/Newlib">Wikipedia</a>
     */
    NEWLIB,

    /**
     * µClibc
     *
     * @see <a href="https://en.wikipedia.org/wiki/UClibc">Wikipedia</a>
     * @see <a href="https://uclibc-ng.org/">uClibc-ng</a>
     */
    UCLIBC;

    /**
     * {@link #name()} in {@code lower-dash-case}
     */
    public final @NotNull String id = id(name());

    /**
     * Gets the platform's env.
     *
     * <p>Determining the env is expensive compared to {@link Arch#current()} or
     * {@link Os#current()} because a process must be spawned to retrieve
     * information regarding this platform if it is not {@link Os#WINDOWS}. On
     * {@link Os#WINDOWS} the result always is {@link #MSVC}.
     *
     * @return current env or {@link #UNKNOWN}
     */
    @Contract(pure = true)
    public static @NotNull Env current() {
        return current(Os.current(), null);
    }

    /**
     * Parses the given value and tries to match it to one of the known envs.
     *
     * @param value to parse
     * @return matching env or {@link #UNKNOWN}
     * @throws NullPointerException if value is {@code null}
     */
    @Contract(pure = true)
    public static @NotNull Env parse(final @NotNull CharSequence value) {
        if (value.length() > 0) {
            final String it = normalize(value);
            for (final Env env : Env.values()) if (env != Env.UNKNOWN && it.contains(env.id)) return env;
            // Matching `gnu` is dangerous because of the GNU operating systems
            // that sometimes identify themselves only with GNU as well, but we
            // need `gnu` here because of gcc. Future has to show if this is a
            // real world problem, or not.
            //
            // https://en.wikipedia.org/wiki/GNU_variants
            if (it.matches(".*g(cc|nu).*")) return GLIBC;
            if (it.matches(".*(apple|bsd|darwin|mac|osx|ios|dragonfly).*")) return BSDLIBC;
            if (it.matches(".*(crtdll|ucrt|vcruntime|vs|win).*")) return MSVC;
            if (it.contains("android")) return BIONIC;
        }
        return UNKNOWN;
    }

    @Contract(pure = true)
    @VisibleForTesting
    static @NotNull Env current(final @NotNull Os os, final @Nullable String lddPath) {
        switch (os) {
            case ANDROID:
                return BIONIC;
            case DARWIN:
            case DRAGONFLYBSD:
            case FREEBSD:
            case NETBSD:
            case OPENBSD:
                return BSDLIBC;
            case WINDOWS:
                return MSVC;
        }
        return parse(ldd(lddPath));
    }

    /**
     * @return first lowercased line from {@code ldd --version}, or an empty
     *     string if execution fails (e.g., {@code ldd} is not in {@code PATH})
     */
    @Contract(pure = true)
    @VisibleForTesting
    static @NotNull String ldd(final @Nullable String lddPath) {
        final @NotNull Process p;
        try {
            p = new ProcessBuilder(lddPath == null ? "ldd" : lddPath, "--version").start();
        } catch (final Throwable ignored) {
            return "";
        }

        final @NotNull StringBuilder sb = new StringBuilder(40);
        try {
            if (p.waitFor(1, TimeUnit.SECONDS)) {
                try (final @NotNull InputStream s = p.getInputStream()) {
                    int c;
                    while ((c = s.read()) != -1 && c != '\n') {
                        if ('A' <= c && c <= 'Z') sb.append((char) (c + 32));
                        else sb.append((char) c);
                    }
                }
            }
        } catch (final Throwable ignored) {
            // fall through
        }

        p.destroyForcibly(); // closes all streams
        return sb.toString();
    }

    @Override public String toString() {
        return id;
    }
}
