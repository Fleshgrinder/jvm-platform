package com.fleshgrinder.platform;

import com.fleshgrinder.platform.fixtures.PlatformSpec;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.stream.Stream;

import static com.fleshgrinder.platform.Arch.ARM_32;
import static com.fleshgrinder.platform.Arch.ARM_64;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

final class PlatformTest {
    @Test void equalsAndHashCode() {
        EqualsVerifier.forClass(Platform.class)
            .withIgnoredFields("os", "arch")
            .withNonnullFields("id")
            .verify();
    }

    @Test void id() {
        assertAll(
            () -> assertEquals("unknown-unknown-unknown", new Platform().toString()),
            () -> assertEquals("linux-unknown-unknown", new Platform(Os.LINUX).toString()),
            () -> assertEquals("unknown-arm-32", new Platform(Arch.ARM_32).toString()),
            () -> assertEquals("unknown-arm-32-be", new Platform(Arch.ARM_32_BE).toString()),
            () -> assertEquals("linux-arm-32-be", new Platform(Os.LINUX, Arch.ARM_32_BE).toString())
        );
    }

    @Test void compare() {
        final Platform a = new Platform(Os.ANDROID);
        final Platform b = new Platform(Os.LINUX);
        final Platform c = new Platform(Os.WINDOWS);
        final Object[] ps = new TreeSet<Platform>() {{
            add(c);
            add(a);
            add(b);
        }}.toArray();

        assertAll(
            () -> assertSame(a, ps[0]),
            () -> assertSame(b, ps[1]),
            () -> assertSame(c, ps[2])
        );
    }

    @TestFactory Stream<DynamicTest> values() {
        return Arrays.stream(Platform.values()).map(platform -> dynamicTest(platform.id, () -> assertAll(
            () -> assertNotNull(platform.os),
            () -> assertNotNull(platform.arch)
        )));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @ParameterizedTest
    @CsvSource({
        "unknown-unknown-unknown, ''",
        "unknown-unknown-unknown, '    '",
        "unknown-unknown-unknown, '\t\n\t\n'",
        "unknown-unknown-unknown, unknown",
        "linux-unknown-unknown, linux",
        "linux-unknown-unknown, linux-unknown",
        "linux-unknown-unknown, linux-unknown-unknown",
        "linux-unknown-unknown, linux-unknown-unknown-unknown",
        "unknown-arm-32, arm-32",
        "unknown-arm-32, unknown-arm-32",
        "unknown-arm-32, unknown-arm-32-unknown",
    })
    void parseFailure(final @NotNull String expected, final @NotNull String value) {
        assertAll(
            () -> assertEquals(expected, Platform.parse(value).id),
            () -> assertThrows(UnsupportedPlatformException.class, () -> Platform.parseOrThrow(value))
        );
    }

    @ParameterizedTest
    @MethodSource({"jvmPlatforms", "jvmPlatformsWithEnvs"})
    @CsvSource({
        // region gcc
        // gcc -dumpmachine
        //
        // This list is not exhaustive because there is no way to get a full
        // list of supported gcc targets. The values here are either produced
        // by myself via the systems that are available to me (including Docker)
        // or collected from the Internet.
        "        ,          , GLIBC   , generic-gnu",
        "        , ARM_32   ,         , arm-none-eabi",
        "        , ARM_32   ,         , armv5te-none-rvct",
        "        , ARM_32   ,         , armv6-none-rvct",
        "        , ARM_32   ,         , armv7-none-rvct",
        "        , ARM_64   ,         , aarch64-none-elf",
        "        , RISCV_64 ,         , riscv64-none-elf",
        "        , X86_32   , GLIBC   , i686-elf-gcc",
        "        , X86_32   , GLIBC   , x86-os2-gcc",
        "ANDROID , ARM_32   , GLIBC   , armv5te-android-gcc",
        "ANDROID , ARM_32   , GLIBC   , armv7-android-gcc",
        "ANDROID , X86_32   , GLIBC   , x86-android-gcc",
        "DARWIN  ,          , GLIBC   , universal-darwin10-gcc",
        "DARWIN  ,          , GLIBC   , universal-darwin11-gcc",
        "DARWIN  ,          , GLIBC   , universal-darwin12-gcc",
        "DARWIN  ,          , GLIBC   , universal-darwin8-gcc",
        "DARWIN  ,          , GLIBC   , universal-darwin9-gcc",
        "DARWIN  , ARM_32   , GLIBC   , armv6-darwin-gcc",
        "DARWIN  , ARM_32   , GLIBC   , armv7-darwin-gcc",
        "DARWIN  , PPC_32   , GLIBC   , ppc32-darwin8-gcc",
        "DARWIN  , PPC_32   , GLIBC   , ppc32-darwin9-gcc",
        "DARWIN  , PPC_64   , GLIBC   , ppc64-darwin8-gcc",
        "DARWIN  , PPC_64   , GLIBC   , ppc64-darwin9-gcc",
        "DARWIN  , X86_32   ,         , x86-darwin8-icc",
        "DARWIN  , X86_32   ,         , x86-darwin9-icc",
        "DARWIN  , X86_32   , GLIBC   , x86-darwin10-gcc",
        "DARWIN  , X86_32   , GLIBC   , x86-darwin11-gcc",
        "DARWIN  , X86_32   , GLIBC   , x86-darwin12-gcc",
        "DARWIN  , X86_32   , GLIBC   , x86-darwin8-gcc",
        "DARWIN  , X86_32   , GLIBC   , x86-darwin9-gcc",
        "DARWIN  , X86_64   , BSDLIBC , x86_64-apple-darwin19.6.0",
        "DARWIN  , X86_64   , BSDLIBC , x86_64-apple-macosx10.15.0",
        "DARWIN  , X86_64   , GLIBC   , x86_64-darwin10-gcc",
        "DARWIN  , X86_64   , GLIBC   , x86_64-darwin11-gcc",
        "DARWIN  , X86_64   , GLIBC   , x86_64-darwin12-gcc",
        "DARWIN  , X86_64   , GLIBC   , x86_64-darwin9-gcc",
        "FREEBSD , X86_64   , BSDLIBC , x86_64-unknown-freebsd",
        "LINUX   , ARM_32   ,         , armv5te-linux-rvct",
        "LINUX   , ARM_32   ,         , armv6-linux-rvct",
        "LINUX   , ARM_32   ,         , armv7-linux-rvct",
        "LINUX   , ARM_32   , GLIBC   , arm-unknown-linux-gnueabihf",
        "LINUX   , ARM_32   , GLIBC   , armv5te-linux-gcc",
        "LINUX   , ARM_32   , GLIBC   , armv6-linux-gcc",
        "LINUX   , ARM_32   , GLIBC   , armv7-linux-gcc",
        "LINUX   , MIPS_32  , GLIBC   , mips32-linux-gcc",
        "LINUX   , PPC_32   , GLIBC   , ppc32-linux-gcc",
        "LINUX   , PPC_64   , GLIBC   , ppc64-linux-gcc",
        "LINUX   , X86_32   ,         , x86-linux-icc",
        "LINUX   , X86_32   , GLIBC   , i486-pc-linux-gnu",
        "LINUX   , X86_32   , GLIBC   , i586-pc-linux-gnu",
        "LINUX   , X86_32   , GLIBC   , i686-pc-linux-gnu",
        "LINUX   , X86_32   , GLIBC   , x86-linux-gcc",
        "LINUX   , X86_64   ,         , x86_64-linux-icc",
        "LINUX   , X86_64   , GLIBC   , x86_64-linux-gcc",
        "LINUX   , X86_64   , GLIBC   , x86_64-linux-gnu",
        "LINUX   , X86_64   , GLIBC   , x86_64-pc-linux-gnu",
        "LINUX   , X86_64   , GLIBC   , x86_64-unknown-linux-gnu",
        "SOLARIS , SPARC_32 , GLIBC   , sparc-solaris-gcc",
        "SOLARIS , SPARC_64 ,         , ultrasparc2-sun-solaris2.10",
        "SOLARIS , X86_32   , GLIBC   , x86-solaris-gcc",
        "SOLARIS , X86_64   , GLIBC   , x86_64-solaris-gcc",
        "WINDOWS , X86_32   , GLIBC   , i686-w64-mingw32-gcc",
        "WINDOWS , X86_32   , GLIBC   , x86-win32-gcc",
        "WINDOWS , X86_32   , MSVC    , x86-win32-vs7",
        "WINDOWS , X86_32   , MSVC    , x86-win32-vs8",
        "WINDOWS , X86_32   , MSVC    , x86-win32-vs9",
        "WINDOWS , X86_64   ,         , x86_64-w64-mingw32",
        "WINDOWS , X86_64   , GLIBC   , x86_64-win64-gcc",
        "WINDOWS , X86_64   , MSVC    , x86_64-win64-vs8",
        "WINDOWS , X86_64   , MSVC    , x86_64-win64-vs9",
        // endregion gcc
        // region go
        // endregion go
        // region rust
        // endregion rust
        // region zig
        // endregion zig

        // region buf
        // endregion buf
        // region protoc
        // endregion protoc
        // region shellcheck
        // endregion shellcheck
    })
    void parseSuccess(
        final @Nullable Os os,
        final @Nullable Arch arch,
        final @Nullable Env env,
        final @NotNull String value
    ) {
        final Platform parse = Platform.parse(value);
//        final Platform parseOrThrow = Platform.parseOrThrow(value);

        assertAll(
            () -> assertEquals(os, parse.os, "parse.os"),
            () -> assertEquals(arch, parse.arch, "parse.arch"),
//            () -> assertEquals(os, parseOrThrow.os, "parseOrThrow.os"),
//            () -> assertEquals(arch, parseOrThrow.arch, "parseOrThrow.arch"),
            () -> {
                if (env != null) assertEquals(env, Env.parse(value), "env");
            }
        );
    }

    static Stream<Arguments> jvmPlatforms() {
        return Arrays.stream(Platform.values()).map(it -> Arguments.of(it.os, it.arch, null, it.id));
    }

    static Iterator<Arguments> jvmPlatformsWithEnvs() {
        final Platform[] platforms = Platform.values();
        final Env[] envs = Env.values();
        return new Iterator<Arguments>() {
            private int i = -1;
            private int j = -1;

            @Override public boolean hasNext() {
                if (++i == platforms.length) i = -1;
                return ++j != envs.length;
            }

            @Override public Arguments next() {
                final Platform platform = platforms[i];
                final Env env = envs[j];
                return Arguments.of(platform.os, platform.arch, env, platform.id + "-" + env.name());
            }
        };
    }
}
