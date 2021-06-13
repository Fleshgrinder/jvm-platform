package com.fleshgrinder.platform;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Usage Examples
 *
 * @see #platform()
 * @see #os()
 * @see #arch()
 * @see #env()
 */
final class Usage {
    /**
     * The {@link Platform} is the main entry point and combined {@link Os} and
     * {@link Arch}. It does not contain the {@link Env} because it is required
     * only is special situations and expensive to determine compared to the
     * others.
     */
    @Test void platform() {
        // current() gets the Os and Arch of this JVM
        final Platform current = Platform.current();
        System.out.println(current.getOs());   // null if unsupported
        System.out.println(current.getArch()); // null if unsupported
        System.out.println(current.getId());   // unknown-unknown-unknown if unsupported

        // parse() is able to extract platform information
        final Platform custom = Platform.parse("Command-Linux-Aarch64_BE.exe");
        assertEquals(Os.LINUX, custom.getOs());
        assertEquals(Arch.ARM_64_BE, custom.getArch());
        // getId() is strictly defined and parser friendly, refer to the Javadoc
        // of it for detailed information
        assertEquals("linux-arm-64-be", custom.getId());

        // it is also possible to construct any kind of platform we want
        System.out.println(new Platform(Os.LINUX, Arch.ALPHA_64));
        System.out.println(new Platform(Os.WINDOWS));
        System.out.println(new Platform(Arch.SPARC_64));
    }

    @Test void os() {
        System.out.println(Os.current()); // throws if unsupported
        System.out.println(Os.currentOrNull()); // null if unsupported

        assertEquals(Os.DARWIN, Os.parse("Mac OS X")); // throws if unsupported
        assertNull(Os.parseOrNull("PSP")); // null if unsupported

        // Additional functionality
        assertEquals("", Os.LINUX.getExecutableExtension());
        assertEquals(".exe", Os.WINDOWS.getExecutableExtension());

        assertEquals("cmd", Os.LINUX.withExecutableExtension("cmd"));
        assertEquals("cmd.exe", Os.WINDOWS.withExecutableExtension("cmd"));

        assertEquals(".so", Os.LINUX.getLinkLibraryExtension());
        assertEquals(".lib", Os.WINDOWS.getLinkLibraryExtension());

        assertEquals("some.so", Os.LINUX.withLinkLibraryExtension("some"));
        assertEquals("some.lib", Os.WINDOWS.withLinkLibraryExtension("some"));

        assertEquals(".dylib", Os.DARWIN.getSharedLibraryExtension());
        assertEquals(".so", Os.LINUX.getSharedLibraryExtension());
        assertEquals(".dll", Os.WINDOWS.getSharedLibraryExtension());

        assertEquals("some.dylib", Os.DARWIN.withSharedLibraryExtension("some"));
        assertEquals("some.so", Os.LINUX.withSharedLibraryExtension("some"));
        assertEquals("some.dll", Os.WINDOWS.withSharedLibraryExtension("some"));

        assertEquals(".a", Os.LINUX.getStaticLibraryExtension());
        assertEquals(".lib", Os.WINDOWS.getStaticLibraryExtension());

        assertEquals("some.a", Os.LINUX.withStaticLibraryExtension("some"));
        assertEquals("some.lib", Os.WINDOWS.withStaticLibraryExtension("some"));
    }

    @Test void arch() {
        System.out.println(Arch.current()); // throws if unsupported
        System.out.println(Arch.currentOrNull()); // null if unsupported

        assertEquals(Arch.ARM_64, Arch.parse("aarch64")); // throws if unsupported
        assertNull(Arch.parseOrNull("mainframe")); // null if unsupported

        // Additional functionality
        assertEquals(32, Arch.X86_32.getBitness());
        assertEquals(64, Arch.X86_64.getBitness());
        assertTrue(Arch.ARM_32.is32bit());
        assertTrue(Arch.ARM_64.is64bit());
    }

    @Test void env() {
        System.out.println(Env.current()); // throws if unknown
        System.out.println(Env.currentOrNull()); // null if unknown

        assertEquals(Env.GLIBC, Env.parse("gcc"));
        assertEquals(Env.MUSL, Env.parse("musl"));
        assertEquals(Env.MSVC, Env.parse("win"));

        assertNull(Env.parseOrNull("elf"));
    }
}
