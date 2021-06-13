package com.fleshgrinder.platform;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

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
     * Linux
     *
     * @see <a href="https://en.wikipedia.org/wiki/Linux">Wikipedia</a>
     */
    LINUX,

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

    /**
     * Determines if the current OS is Windows.
     *
     * @return {@code true} if the current OS is Windows.
     */
    @Contract(pure = true)
    static boolean isWindows() {
        return "\\".equals(System.getProperty("file.separator"));
    }

    /**
     * Gets this platform's operating system.
     *
     * @return current operating system
     * @throws UnsupportedPlatformException if unknown
     */
    @Contract(pure = true)
    public static @NotNull Os current() throws UnsupportedPlatformException {
        final Os os = currentOrNull();
        if (os == null) throw UnsupportedPlatformException.fromSystemProperty(Os.class, "os.name");
        return os;
    }

    /**
     * Gets this platform's operating system.
     *
     * <p>The value to determine the operating system of the current platform is
     * retrieved from the {@code os.name} system property. This means that the
     * returned operating system is the one reported by the JVM and not
     * necessarily the real operating system of the platform.
     *
     * <p>Note that the value of the {@code os.arch} system property can be
     * changed by a user, like any other system property.
     *
     * @return current operating system or {@code null} if unknown
     */
    @Contract(pure = true)
    public static @Nullable Os currentOrNull() {
        if (isWindows()) return WINDOWS;
        final @NotNull String it = System.getProperty("os.name", "");
        switch (normalize(it, true)) {
            case "aix":
                return AIX;
            case "android":
                return ANDROID;
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
     * Parses the given value and tries to extract
     *
     * @param value to parse
     * @return matching operating system
     * @throws NullPointerException if value is {@code null}
     * @throws UnsupportedPlatformException if unknown
     */
    @Contract(pure = true)
    public static @NotNull Os parse(final @NotNull CharSequence value) throws UnsupportedPlatformException {
        final Os os = parseOrNull(value);
        if (os == null) throw UnsupportedPlatformException.fromValue(Os.class, value);
        return os;
    }

    /**
     * @param value to parse
     * @return matching operating system or {@code null} if unknown
     * @throws NullPointerException if value is {@code null}
     */
    @Contract(pure = true)
    public static @Nullable Os parseOrNull(final @NotNull CharSequence value) {
        if (value.length() > 0) {
            final @NotNull String it = normalize(value);
            // Android MUST come before Linux because they often come together
            if (it.contains("android")) return ANDROID;
            if (it.matches(".*n[iu]x.*")) return LINUX;
            // (32|64)imac as in RV(32|64)IMAC ISA may be embedded in a platform
            // identifier but is not necessarily targeting Darwin.
            // `osx` is considered Darwin even standalone but `os-x` is not,
            // because it would also match `os-x86` which leads to too many
            // false positives.
            if (it.matches(".*(apple|darwin|(?<!(32|64)i)mac|osx|ios).*")) return DARWIN;
            // MUST come after darWIN!
            // w32 and w64 are from gcc (actually mingw-w64)
            if (it.matches(".*w(in|32|64).*")) return WINDOWS;
            if (it.contains("aix")) return AIX;
            if (it.contains("dragonfly")) return DRAGONFLYBSD;
            if (it.contains("freebsd")) return FREEBSD;
            if (it.contains("fuchsia")) return FUCHSIA;
            if (it.contains("haiku")) return HAIKU;
            if (it.contains("netbsd")) return NETBSD;
            if (it.contains("openbsd")) return OPENBSD;
            if (it.contains("plan9")) return PLAN9;
            if (it.contains("redox")) return REDOX;
            if (it.contains("vxworks")) return VXWORKS;
            if (it.matches(".*hp-?ux.*")) return HPUX;
            if (it.matches(".*(ibm-?i|os400[^0-9]).*")) return IBMI;
            if (it.matches(".*illum-?os.*")) return ILLUMOS;
            if (it.matches(".*(qnx|procnto).*")) return QNX;
            if (it.matches(".*(s(olaris|un-?os)).*")) return SOLARIS;
            if (it.matches(".*z-?os.*")) return ZOS;
        }
        return null;
    }

    /**
     * @return {@link #name()} in {@code lower-dash-case}
     */
    @Contract(pure = true)
    public @NotNull String getId() {
        return id(name());
    }

    /**
     * @return extension for executables with leading period or an empty string
     *     if not applicable on this platform
     */
    @Contract(pure = true)
    public @NotNull String getExecutableExtension() {
        if (this == WINDOWS) return ".exe";
        return "";
    }

    /**
     * @param path to append the extension to
     * @return path with {@link #getExecutableExtension}
     */
    @Contract(pure = true)
    public @NotNull String withExecutableExtension(final @NotNull String path) {
        return path + getExecutableExtension();
    }

    /**
     * @param file to append the extension to
     * @return file with {@link #getExecutableExtension}
     */
    @Contract(pure = true)
    public @NotNull File withExecutableExtension(final @NotNull File file) {
        return new File(file.getParentFile(), withExecutableExtension(file.getName()));
    }

    /**
     * @return extension for link libraries with leading period on this platform
     */
    @Contract(pure = true)
    public @NotNull String getLinkLibraryExtension() {
        if (this == WINDOWS) return ".lib";
        return ".so";
    }

    /**
     * @param path to append the extension to
     * @return path with {@link #getLinkLibraryExtension}
     */
    @Contract(pure = true)
    public @NotNull String withLinkLibraryExtension(final @NotNull String path) {
        return path + getLinkLibraryExtension();
    }

    /**
     * @param file to append the extension to
     * @return file with {@link #getLinkLibraryExtension}
     */
    @Contract(pure = true)
    public @NotNull File withLinkLibraryExtension(final @NotNull File file) {
        return new File(file.getParentFile(), withLinkLibraryExtension(file.getName()));
    }

    /**
     * @return extension for shared libraries with leading period on this
     *     platform
     */
    @Contract(pure = true)
    public @NotNull String getSharedLibraryExtension() {
        switch (this) {
            case WINDOWS:
                return ".dll";
            case DARWIN:
                return ".dylib";
        }
        return ".so";
    }

    /**
     * @param path to append the extension to
     * @return path with {@link #getSharedLibraryExtension}
     */
    @Contract(pure = true)
    public @NotNull String withSharedLibraryExtension(final @NotNull String path) {
        return path + getSharedLibraryExtension();
    }

    /**
     * @param file to append the extension to
     * @return file with {@link #getSharedLibraryExtension}
     */
    @Contract(pure = true)
    public @NotNull File withSharedLibraryExtension(final @NotNull File file) {
        return new File(file.getParentFile(), withSharedLibraryExtension(file.getName()));
    }

    /**
     * @return extension for static libraries with leading period on this
     *     platform
     */
    @Contract(pure = true)
    public @NotNull String getStaticLibraryExtension() {
        if (this == WINDOWS) return ".lib";
        return ".a";
    }

    /**
     * @param path to append the extension to
     * @return path with {@link #getStaticLibraryExtension}
     */
    @Contract(pure = true)
    public @NotNull String withStaticLibraryExtension(final @NotNull String path) {
        return path + getStaticLibraryExtension();
    }

    /**
     * @param file to append the extension to
     * @return file with {@link #getStaticLibraryExtension}
     */
    @Contract(pure = true)
    public @NotNull File withStaticLibraryExtension(final @NotNull File file) {
        return new File(file.getParentFile(), withStaticLibraryExtension(file.getName()));
    }
}
