package com.fleshgrinder.platform;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        System.out.println(current.os);
        System.out.println(current.arch);
        System.out.println(current.id);

        // parse() is able to extract platform information
        final Platform custom = Platform.parse("Command-Linux-Aarch64_BE.exe");
        assertEquals(Os.LINUX, custom.os);
        assertEquals(Arch.ARM_64_BE, custom.arch);
        // getId() is strictly defined and parser friendly, refer to the Javadoc
        // of it for detailed information
        assertEquals("linux-arm-64-be", custom.id);

        // it is also possible to construct any kind of platform we want
        System.out.println(new Platform(Os.LINUX, Arch.ALPHA_64));
        System.out.println(new Platform(Os.WINDOWS));
        System.out.println(new Platform(Arch.SPARC_64));
    }

    @Test void os() {
        System.out.println(Os.current());

        assertEquals(Os.UNKNOWN, Os.parse("PSP"));
        assertEquals(Os.DARWIN, Os.parse("Mac OS X"));

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
        System.out.println(Arch.current());

        assertEquals(Arch.UNKNOWN_UNKNOWN, Arch.parse("test"));
        assertEquals(Arch.UNKNOWN_32, Arch.parse("test32"));
        assertEquals(Arch.UNKNOWN_64, Arch.parse("test64"));
        assertEquals(Arch.ARM_64, Arch.parse("aarch64"));

        assertEquals(32, Arch.X86_32.getBitness());
        assertEquals(64, Arch.X86_64.getBitness());
        assertTrue(Arch.ARM_32.is32bit());
        assertTrue(Arch.ARM_64.is64bit());
        assertTrue(Arch.ARM_64_BE.isArm());
        assertFalse(Arch.ARM_64_BE.isArmLe());

        assertTrue(Arch.ARM_32.isArm());
        assertTrue(Arch.ARM_32.isArmLe());
        assertFalse(Arch.ARM_32.isArmBe());
    }

    @Test void env() {
        System.out.println(Env.current());

        assertEquals(Env.UNKNOWN, Env.parse("elf"));
        assertEquals(Env.GLIBC, Env.parse("gcc"));
        assertEquals(Env.MUSL, Env.parse("musl"));
        assertEquals(Env.MSVC, Env.parse("win"));
    }
}
