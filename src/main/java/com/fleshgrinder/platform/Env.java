package com.fleshgrinder.platform;

import org.jetbrains.annotations.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import static com.fleshgrinder.platform.Platform.normalize;

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
     * @see <a href="https://en.wikipedia.org/wiki/UClibc">Wikipedia</a>, usually
     * @see <a href="https://uclibc-ng.org/">uClibc-ng</a>.
     */
    UCLIBC;

    /**
     * Gets the platform's env.
     *
     * <p>Determining the env is expensive compared to {@link Arch#current()} or
     * {@link Os#current()} because a process must be spawned to retrieve
     * information regarding this platform if it is not {@link Os#WINDOWS}. On
     * {@link Os#WINDOWS} the result always is {@link #MSVC}.
     *
     * @return current env
     * @throws UnsupportedPlatformException if unknown
     */
    @Contract(value = "-> new", pure = true)
    public static @NotNull Env current() throws UnsupportedPlatformException {
        return current(Os.current(), null);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @TestOnly
    @VisibleForTesting
    static @NotNull Env current(final @NotNull Os os, final @Nullable String lddPath) throws UnsupportedPlatformException {
        final Env env = toEnv(os);
        if (env == null) return parse(ldd(lddPath));
        return env;
    }

    /**
     * Gets the platform's env.
     *
     * @return current env or {@code null} if unknown
     */
    @Contract(pure = true)
    public static @Nullable Env currentOrNull() {
        return currentOrNull(Os.currentOrNull(), null);
    }

    @Contract(pure = true)
    @TestOnly
    @VisibleForTesting
    static @Nullable Env currentOrNull(final @Nullable Os os, final @Nullable String lddPath) {
        if (os == null) return null;
        final Env env = toEnv(os);
        if (env == null) return parseOrNull(ldd(lddPath));
        return env;
    }

    /**
     * @param value to parse
     * @return matching env
     * @throws NullPointerException if value is {@code null}
     * @throws UnsupportedPlatformException if env is unknown
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Env parse(final @NotNull CharSequence value) throws UnsupportedPlatformException {
        final Env env = parseOrNull(value);
        if (env == null) {
            throw UnsupportedPlatformException.fromValue(Env.class, value);
        }
        return env;
    }

    /**
     * @param value to parse
     * @return matching env or {@code null} if unknown
     * @throws NullPointerException if value is {@code null}
     */
    @Contract(pure = true)
    public static @Nullable Env parseOrNull(final @NotNull CharSequence value) {
        if (value.length() > 0) {
            final String it = normalize(value);
            for (final Env env : Env.values()) {
                if (it.contains(normalize(env.name()))) {
                    return env;
                }
            }
            // Matching `gnu` is dangerous because of the GNU operating systems
            // that sometimes identify themselves only with GNU as well, but we
            // need `gnu` here because of gcc. Future has to show if this is a
            // real world problem, or not.
            //
            // https://en.wikipedia.org/wiki/GNU_variants
            if (it.matches(".*g(cc|nu).*")) {
                return GLIBC;
            }
            if (it.matches(".*(apple|bsd|darwin|mac|osx).*")) {
                return BSDLIBC;
            }
            if (it.matches(".*(crtdll|ucrt|vcruntime|vs).*")) {
                return MSVC;
            }
        }
        return null;
    }

    /**
     * @param os to convert
     * @return env for the given OS or {@code null} if OS has not fixed env
     */
    @Contract(pure = true)
    private static @Nullable Env toEnv(final @NotNull Os os) {
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
        return null;
    }

    /**
     * @return first lowercased line from {@code ldd --version}, or an empty
     *     string if execution fails (e.g., {@code ldd} is not in {@code PATH})
     */
    @Contract(value = "_ -> new", pure = true)
    @VisibleForTesting
    static @NotNull String ldd(final @Nullable String lddPath) {
        final @NotNull Process p;
        try {
            p = new ProcessBuilder(lddPath == null ? "ldd" : lddPath, "--version").start();
        } catch (final IOException ignored) {
            return "";
        }

        final @NotNull StringBuilder sb = new StringBuilder(40);
        try {
            if (p.waitFor(1, TimeUnit.SECONDS)) {
                final @NotNull InputStream s = p.getInputStream();
                int c;
                while ((c = s.read()) != -1 && c != '\n') {
                    if ('A' <= c && c <= 'Z') {
                        sb.append((char) (c + 32));
                    } else {
                        sb.append((char) c);
                    }
                }
            }
        } catch (final InterruptedException | IOException ignored) {
            // fall through
        }

        p.destroyForcibly(); // closes all streams
        return sb.toString();
    }
}
