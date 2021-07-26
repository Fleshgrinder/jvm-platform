package com.fleshgrinder.platform;

import java.io.File;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.fleshgrinder.platform.Utils.id;
import static com.fleshgrinder.platform.Utils.normalize;

/**
 * Operating System
 *
 * @see <a href="https://en.wikipedia.org/wiki/Operating_system">Wikipedia</a>
 */
@SuppressWarnings("SpellCheckingInspection")
public enum Os {
    /**
     * IBM AIX
     *
     * @see <a href="https://en.wikipedia.org/wiki/IBM_AIX">Wikipedia</a>
     */
    AIX,

    /**
     * Google Android
     *
     * @see <a href="https://en.wikipedia.org/wiki/Android_(operating_system)">Wikipedia</a>
     */
    ANDROID,

    /**
     * Apple Darwin (Mac OS X)
     *
     * @see <a href="https://en.wikipedia.org/wiki/Darwin">Wikipedia</a>
     */
    DARWIN,

    /**
     * DragonFly BSD
     *
     * @see <a href="https://en.wikipedia.org/wiki/DragonFly_BSD">Wikipedia</a>
     */
    DRAGONFLYBSD,

    /**
     * FreeBSD
     *
     * @see <a href="https://en.wikipedia.org/wiki/FreeBSD">Wikipedia</a>
     */
    FREEBSD,

    /**
     * Google Fuchsia
     *
     * @see <a href="https://en.wikipedia.org/wiki/Google_Fuchsia">Wikipedia</a>
     */
    FUCHSIA,

    /**
     * Haiku
     *
     * @see <a href="https://en.wikipedia.org/wiki/Haiku_(operating_system)">Wikipedia</a>
     */
    HAIKU,

    /**
     * HP-UX
     *
     * @see <a href="https://en.wikipedia.org/wiki/HP-UX">Wikipedia</a>
     */
    HPUX,

    /**
     * IBM i
     *
     * @see <a href="https://en.wikipedia.org/wiki/IBM_i">Wikipedia</a>
     */
    IBMI,

    /**
     * illumos
     *
     * @see <a href="https://en.wikipedia.org/wiki/Illumos">Wikipedia</a>
     */
    ILLUMOS,

    /**
     * Linux
     *
     * @see <a href="https://en.wikipedia.org/wiki/Linux">Wikipedia</a>
     */
    LINUX,

    /**
     * NetBSD
     *
     * @see <a href="https://en.wikipedia.org/wiki/NetBSD">Wikipedia</a>
     */
    NETBSD,

    /**
     * OpenBSD
     *
     * @see <a href="https://en.wikipedia.org/wiki/OpenBSD">Wikipedia</a>
     */
    OPENBSD,

    /**
     * Plan9 from Bell Labs
     *
     * @see <a href="https://en.wikipedia.org/wiki/Plan_9_from_Bell_Labs">Wikipedia</a>
     * @see <a href="https://p9f.org/">p9f.org</a>
     */
    PLAN9,

    /**
     * QNX
     *
     * @see <a href="https://en.wikipedia.org/wiki/QNX">Wikipedia</a>
     */
    QNX,

    /**
     * Redox
     *
     * @see <a href="https://en.wikipedia.org/wiki/Redox_(operating_system)">Wikipedia</a>
     */
    REDOX,

    /**
     * Oracle Solaris (SunOS)
     *
     * @see <a href="https://en.wikipedia.org/wiki/Oracle_Solaris">Wikipedia</a>
     */
    SOLARIS,

    /**
     * VxWorks
     *
     * @see <a href="https://en.wikipedia.org/wiki/VxWorks">Wikipedia</a>
     */
    VXWORKS,

    /**
     * Microsoft Windows
     *
     * @see <a href="https://en.wikipedia.org/wiki/Microsoft_Windows">Wikipedia</a>
     */
    WINDOWS,

    /**
     * z/OS
     *
     * @see <a href="https://en.wikipedia.org/wiki/Z/OS">Wikipedia</a>
     */
    ZOS;

    /** @see #toString() */
    private final @NotNull String id = id(name());

    /**
     * Gets the OS of the current platform.
     *
     * <p>The system properties {@code file.separator} and {@code os.name} are
     * used to determine the current OS. This means that the value
     * for them was set during the compilation of the JVM and may not represent
     * the actual OS that this JVM is currently running on.
     * However, if the JVM can run on this OS despite it not
     * matching the actual OS this means that any other software
     * should be able to do so as well.
     *
     * <p>Note that the values of the {@code file.separator} and {@code os.name}
     * system properties can be changed by a user, like any other system
     * property.
     *
     * @return the current OS.
     * @throws IllegalStateException if it cannot be determined.
     * @see #currentOrNull()
     */
    @Contract(pure = true)
    public static @NotNull Os current() throws IllegalStateException {
        final Os os = currentOrNull();
        if (os == null) throw new IllegalStateException("Unknown operating system: " + System.getProperty("os.name", "missing 'os.name' system property"));
        return os;
    }

    /**
     * Gets the OS of the current platform.
     *
     * <p>The system properties {@code file.separator} and {@code os.name} are
     * used to determine the current OS. This means that the value
     * for them was set during the compilation of the JVM and may not represent
     * the actual OS that this JVM is currently running on.
     * However, if the JVM can run on this OS despite it not
     * matching the actual OS this means that any other software
     * should be able to do so as well.
     *
     * <p>Note that the values of the {@code file.separator} and {@code os.name}
     * system properties can be changed by a user, like any other system
     * property.
     *
     * @return the current OS or {@code null} if it cannot be determined.
     * @see #current()
     */
    @Contract(pure = true)
    public static @Nullable Os currentOrNull() {
        if ("\\".equals(System.getProperty("file.separator"))) return WINDOWS;
        final @Nullable String it = System.getProperty("os.name");
        if (it == null) return null;
        switch (normalize(it, true)) {
            case "aix":
                return AIX;
            case "linux":
                return "dalvik".equalsIgnoreCase(System.getProperty("java.vm.name", "")) ? ANDROID : LINUX;
            case "darwin":
            case "macosx":
            case "macos":
                return DARWIN;
            case "dragonflybsd":
                return DRAGONFLYBSD;
            case "freebsd":
                return FREEBSD;
            case "fuchsia":
                return FUCHSIA;
            case "haiku":
                return HAIKU;
            case "hpux":
                return HPUX;
            case "os400":
                return IBMI;
            case "illumos":
                return ILLUMOS;
            case "netbsd":
                return NETBSD;
            case "openbsd":
                return OPENBSD;
            case "plan9":
                return PLAN9;
            case "qnx":
            case "procnto":
                return QNX;
            case "redox":
                return REDOX;
            case "solaris":
            case "sunos":
                return SOLARIS;
            case "vxworks":
                return VXWORKS;
            case "zos":
                return ZOS;
        }
        return parseOrNull(it);
    }

    /**
     * Gets the OS whose string matches the given value.
     *
     * <p>This method is strict and only accepts values that perfectly match the
     * strings as described in {@link #toString}. Use {@link #parse} or
     * {@link #parseOrNull} for a lenient approach that accept arbitrary input.
     *
     * @param value to get the OS for.
     * @return the matching OS.
     * @throws IllegalArgumentException if no match is found.
     * @throws NullPointerException if the given value is {@code null}.
     * @see #fromStringOrNull(String)
     * @see #parse(CharSequence)
     * @see #parseOrNull(CharSequence)
     * @see #toString()
     * @see #valueOf(String)
     */
    @Contract(pure = true)
    public static @NotNull Os fromString(final @NotNull String value) throws IllegalArgumentException {
        final Os os = fromStringOrNull(value);
        if (os == null) throw new IllegalArgumentException("Unknown operating system: " + value);
        return os;
    }

    /**
     * Gets the OS whose string matches the given value.
     *
     * <p>This method is strict and only accepts values that perfectly match the
     * strings as described in {@link #toString}. Use {@link #parse} or
     * {@link #parseOrNull} for a lenient approach that accept arbitrary input.
     *
     * @param value to get the OS for.
     * @return the matching OS or {@code null} if no match is found.
     * @throws NullPointerException if the given value is {@code null}.
     * @see #fromString(String)
     * @see #parse(CharSequence)
     * @see #parseOrNull(CharSequence)
     * @see #toString()
     * @see #valueOf(String)
     */
    @Contract(pure = true)
    public static @Nullable Os fromStringOrNull(final @NotNull String value) {
        for (final Os os : Os.values()) if (os.id.equals(value)) return os;
        return null;
    }

    /**
     * Parses the given value and tries to match it with an OS.
     *
     * @param value to parse and match.
     * @return the matching OS.
     * @throws IllegalArgumentException if no matching OS is found.
     * @throws NullPointerException if the given value is {@code null}.
     * @see #fromString(String)
     * @see #fromStringOrNull(String)
     * @see #parseOrNull(CharSequence)
     * @see #valueOf(String)
     */
    @Contract(pure = true)
    public static @NotNull Os parse(final @NotNull CharSequence value) throws IllegalArgumentException {
        final Os os = parseOrNull(value);
        if (os == null) throw new IllegalArgumentException("Unknown operating system: " + value);
        return os;
    }

    /**
     * Parses the given value and tries to match it with an OS.
     *
     * @param value to parse and match.
     * @return the matching OS or {@code null} if no match is found.
     * @throws NullPointerException if the given value is {@code null}.
     * @see #fromString(String)
     * @see #fromStringOrNull(String)
     * @see #parse(CharSequence)
     * @see #valueOf(String)
     */
    @Contract(pure = true)
    public static @Nullable Os parseOrNull(final @NotNull CharSequence value) {
        if (value.length() > 0) {
            final String it = normalize(value);
            for (final Os os : Os.values()) if (os.id.equals(it)) return os;
            // region common
            // Android MUST come before Linux because they often come together
            if (it.matches("(?s).*\\bandroid\\b.*?")) return ANDROID;
            if (it.matches("(?s).*\\b(linux|u?nix)\\b.*?")) return LINUX;
            if (it.matches("(?s).*\\b(apple|darwin|ios|mac(osx?)?|os-?x)\\b.*?")) return DARWIN;
            if (it.matches("(?s).*\\b(w(7|8|1[01]|32|64|xp)|win(dows)?(\\d|\\d\\d|xp)?)\\b.*?")) return WINDOWS;
            // endregion common
            // region uncommon
            if (it.matches("(?s).*\\baix\\b.*?")) return AIX;
            if (it.matches("(?s).*\\bdragon-?fly(bsd)?\\b.*?")) return DRAGONFLYBSD;
            if (it.matches("(?s).*\\bfree-?bsd\\b.*?")) return FREEBSD;
            if (it.matches("(?s).*\\bfuchsia\\b.*?")) return FUCHSIA;
            if (it.matches("(?s).*\\bhaiku\\b.*?")) return HAIKU;
            if (it.matches("(?s).*\\bhp-?ux\\b.*?")) return HPUX;
            if (it.matches("(?s).*\\b(ibm-?i|os-?400)\\b.*?")) return IBMI;
            if (it.matches("(?s).*\\billum(os)?\\b.*?")) return ILLUMOS;
            if (it.matches("(?s).*\\bnet-?bsd\\b.*?")) return NETBSD;
            if (it.matches("(?s).*\\bopen-?bsd\\b.*?")) return OPENBSD;
            if (it.matches("(?s).*\\bplan-?9\\b.*?")) return PLAN9;
            if (it.matches("(?s).*\\b(qnx|procnto)\\b.*?")) return QNX;
            if (it.matches("(?s).*\\bredox\\b.*?")) return REDOX;
            if (it.matches("(?s).*\\b(s(olaris|un-?os))\\b.*?")) return SOLARIS;
            if (it.matches("(?s).*\\bvx-?works\\b.*?")) return VXWORKS;
            if (it.matches("(?s).*\\bz-?os\\b.*?")) return ZOS;
            // endregion uncommon
        }
        return null;
    }

    /**
     * Gets the OS specific executable extension.
     *
     * <ul>
     *     <li>{@code .exe} for {@link #WINDOWS}
     *     <li>empty string for all others
     * </ul>
     *
     * @return the OS specific executable extension.
     * @see #withExecutableExtension(File)
     * @see #withExecutableExtension(String)
     */
    @Contract(pure = true)
    public @NotNull String getExecutableExtension() {
        return this == WINDOWS ? ".exe" : "";
    }

    /**
     * Appends the OS specific executable extension to the given path.
     *
     * @param path to append the extension to.
     * @return path with OS specific executable extension appended.
     * @throws NullPointerException if the given path is {@code null}.
     * @see #getExecutableExtension()
     * @see #withExecutableExtension(File)
     */
    @Contract(pure = true)
    public @NotNull String withExecutableExtension(final @NotNull String path) {
        return this == WINDOWS ? path + getExecutableExtension() : path;
    }

    /**
     * Appends the OS specific executable extension to the given file.
     *
     * @param file to append the extension to.
     * @return file with OS specific executable extension appended.
     * @throws NullPointerException if the given file is {@code null}.
     * @see #getExecutableExtension()
     * @see #withExecutableExtension(File)
     */
    @Contract(pure = true)
    public @NotNull File withExecutableExtension(final @NotNull File file) {
        return this == WINDOWS ? new File(file.getParentFile(), withExecutableExtension(file.getName())) : file;
    }

    /**
     * Gets the OS specific link library extension.
     *
     * <ul>
     *     <li>{@code .lib} for {@link #WINDOWS}
     *     <li>{@code .so} for all others
     * </ul>
     *
     * @return the OS specific link library extension.
     * @see #withLinkLibraryExtension(File)
     * @see #withLinkLibraryExtension(String)
     */
    @Contract(pure = true)
    public @NotNull String getLinkLibraryExtension() {
        return this == WINDOWS ? ".lib" : ".so";
    }

    /**
     * Appends the OS specific link library extension to the given path.
     *
     * @param path to append the extension to.
     * @return path with OS specific link library extension appended.
     * @throws NullPointerException if the given path is {@code null}.
     * @see #getLinkLibraryExtension()
     * @see #withLinkLibraryExtension(File)
     */
    @Contract(pure = true)
    public @NotNull String withLinkLibraryExtension(final @NotNull String path) {
        return path + getLinkLibraryExtension();
    }

    /**
     * Appends the OS specific link library extension to the given file.
     *
     * @param file to append the extension to.
     * @return file with OS specific link library extension appended.
     * @throws NullPointerException if the given file is {@code null}.
     * @see #getLinkLibraryExtension()
     * @see #withLinkLibraryExtension(File)
     */
    @Contract(pure = true)
    public @NotNull File withLinkLibraryExtension(final @NotNull File file) {
        return new File(file.getParentFile(), withLinkLibraryExtension(file.getName()));
    }

    /**
     * Gets the OS specific shared library extension.
     *
     * <ul>
     *     <li>{@code .dll} for {@link #WINDOWS}
     *     <li>{@code .dylib} for {@link #DARWIN}*
     *     <li>{@code .so} for all others
     * </ul>
     *
     * <p>* The extension for shared libraries has changed from {@code .jnilib}
     * to {@code .dylib} on {@link #DARWIN} a very long time ago. Before Java
     * 1.7 the {@code .jnilib} extension was the default and this is why there
     * still are various libraries out there using this extension. However, it
     * should not be used anymore and this library has no support for it in any
     * way.
     *
     * @return the OS specific shared library extension.
     * @see #withSharedLibraryExtension(String)
     * @see #withSharedLibraryExtension(File)
     */
    @Contract(pure = true)
    public @NotNull String getSharedLibraryExtension() {
        return this == WINDOWS ? ".dll" : this == DARWIN ? ".dylib" : ".so";
    }

    /**
     * Appends the OS specific shared library extension to the given path.
     *
     * @param path to append the extension to.
     * @return path with OS specific shared library extension appended.
     * @throws NullPointerException if the given path is {@code null}.
     * @see #getSharedLibraryExtension()
     * @see #withSharedLibraryExtension(File)
     */
    @Contract(pure = true)
    public @NotNull String withSharedLibraryExtension(final @NotNull String path) {
        return path + getSharedLibraryExtension();
    }

    /**
     * Appends the OS specific shared library extension to the given file.
     *
     * @param file to append the extension to.
     * @return file with OS specific shared library extension appended.
     * @throws NullPointerException if the given file is {@code null}.
     * @see #getSharedLibraryExtension()
     * @see #withSharedLibraryExtension(File)
     */
    @Contract(pure = true)
    public @NotNull File withSharedLibraryExtension(final @NotNull File file) {
        return new File(file.getParentFile(), withSharedLibraryExtension(file.getName()));
    }

    /**
     * Gets the OS specific static library extension.
     *
     * <ul>
     *     <li>{@code .lib} for {@link #WINDOWS}
     *     <li>{@code .a} for all others
     * </ul>
     *
     * @return the OS specific static library extension.
     * @see #withStaticLibraryExtension(File)
     * @see #withStaticLibraryExtension(String)
     */
    @Contract(pure = true)
    public @NotNull String getStaticLibraryExtension() {
        return this == WINDOWS ? ".lib" : ".a";
    }

    /**
     * Appends the OS specific static library extension to the given path.
     *
     * @param path to append the extension to.
     * @return path with OS specific static library extension appended.
     * @throws NullPointerException if the given path is {@code null}.
     * @see #getStaticLibraryExtension()
     * @see #withStaticLibraryExtension(File)
     */
    @Contract(pure = true)
    public @NotNull String withStaticLibraryExtension(final @NotNull String path) {
        return path + getStaticLibraryExtension();
    }

    /**
     * Appends the OS specific static library extension to the given file.
     *
     * @param file to append the extension to.
     * @return file with OS specific static library extension appended.
     * @throws NullPointerException if the given file is {@code null}.
     * @see #getStaticLibraryExtension()
     * @see #withStaticLibraryExtension(File)
     */
    @Contract(pure = true)
    public @NotNull File withStaticLibraryExtension(final @NotNull File file) {
        return new File(file.getParentFile(), withStaticLibraryExtension(file.getName()));
    }

    /**
     * Gets the canonical machine-readable identifier of this OS.
     *
     * <p>The anatomy of the returned string <strong>always</strong> matches the
     * following regular expression:
     *
     * <pre>{@code ^[a-z][a-z0-9]*$}</pre>
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
