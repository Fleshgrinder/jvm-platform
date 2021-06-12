package com.fleshgrinder.platform;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
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

    @Test void serializable() throws ClassNotFoundException, IOException {
        final Platform p = new Platform(Os.LINUX, Arch.X86_64);
        final ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (final ObjectOutputStream o = new ObjectOutputStream(buf)) {
            o.writeObject(p);
        }
        Object o;
        try (final ObjectInputStream i = new ObjectInputStream(new ByteArrayInputStream(buf.toByteArray()))) {
            o = i.readObject();
        }
        assertEquals(p, o);
    }

    @TestFactory Stream<DynamicTest> values() {
        return Arrays.stream(Platform.values()).map(platform -> dynamicTest(platform.id, () -> assertAll(
            () -> assertNotNull(platform.os),
            () -> assertNotNull(platform.arch)
        )));
    }

    @ParameterizedTest
    @MethodSource({"jvmPlatforms", "jvmPlatformsWithEnvs"})
    @CsvSource({
        // region gcc
        // $ gcc -dumpmachine
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
        // $ go tool dist list
        // https://github.com/golang/go/blob/master/src/go/build/syslist.go
        "             ,            , , js/wasm",
        "AIX          , PPC_64     , , aix/ppc64",
        "ANDROID      , ARM_32     , , android/arm",
        "ANDROID      , ARM_64     , , android/arm64",
        "ANDROID      , X86_32     , , android/386",
        "ANDROID      , X86_64     , , android/amd64",
        "DARWIN       , ARM_64     , , darwin/arm64",
        "DARWIN       , ARM_64     , , ios/arm64",
        "DARWIN       , X86_64     , , darwin/amd64",
        "DARWIN       , X86_64     , , ios/amd64",
        "DRAGONFLYBSD , X86_64     , , dragonfly/amd64",
        "FREEBSD      , ARM_32     , , freebsd/arm",
        "FREEBSD      , ARM_64     , , freebsd/arm64",
        "FREEBSD      , X86_32     , , freebsd/386",
        "FREEBSD      , X86_64     , , freebsd/amd64",
        "ILLUMOS      , X86_64     , , illumos/amd64",
        "LINUX        , ARM_32     , , linux/arm",
        "LINUX        , ARM_64     , , linux/arm64",
        "LINUX        , IBMZ_64    , , linux/s390x",
        "LINUX        , MIPS_32    , , linux/mips",
        "LINUX        , MIPS_32_LE , , linux/mipsle",
        "LINUX        , MIPS_64    , , linux/mips64",
        "LINUX        , MIPS_64_LE , , linux/mips64le",
        "LINUX        , PPC_64     , , linux/ppc64",
        "LINUX        , PPC_64_LE  , , linux/ppc64le",
        "LINUX        , RISCV_64   , , linux/riscv64",
        "LINUX        , X86_32     , , linux/386",
        "LINUX        , X86_64     , , linux/amd64",
        "NETBSD       , ARM_32     , , netbsd/arm",
        "NETBSD       , ARM_64     , , netbsd/arm64",
        "NETBSD       , X86_32     , , netbsd/386",
        "NETBSD       , X86_64     , , netbsd/amd64",
        "OPENBSD      , ARM_32     , , openbsd/arm",
        "OPENBSD      , ARM_64     , , openbsd/arm64",
        "OPENBSD      , MIPS_64    , , openbsd/mips64",
        "OPENBSD      , X86_32     , , openbsd/386",
        "OPENBSD      , X86_64     , , openbsd/amd64",
        "PLAN9        , ARM_32     , , plan9/arm",
        "PLAN9        , X86_32     , , plan9/386",
        "PLAN9        , X86_64     , , plan9/amd64",
        "SOLARIS      , X86_64     , , solaris/amd64",
        "WINDOWS      , ARM_32     , , windows/arm",
        "WINDOWS      , X86_32     , , windows/386",
        "WINDOWS      , X86_64     , , windows/amd64",
        // endregion go
        // region rust
        // $ rustc --print target-list
        // https://doc.rust-lang.org/nightly/rustc/platform-support.html
        "             ,            ,        , asmjs-unknown-emscripten",
        "             ,            ,        , msp430-none-elf",
        "             ,            ,        , nvptx64-nvidia-cuda",
        "             ,            ,        , thumbv4t-none-eabi",
        "             ,            ,        , thumbv6m-none-eabi",
        "             ,            ,        , thumbv7em-none-eabi",
        "             ,            ,        , thumbv7em-none-eabihf",
        "             ,            ,        , thumbv7m-none-eabi",
        "             ,            ,        , thumbv8m.base-none-eabi",
        "             ,            ,        , thumbv8m.main-none-eabi",
        "             ,            ,        , thumbv8m.main-none-eabihf",
        "             ,            ,        , wasm32-unknown-emscripten",
        "             ,            ,        , wasm32-unknown-unknown",
        "             ,            ,        , wasm32-wasi",
        "             ,            , GLIBC  , avr-unknown-gnu-atmega328", // FIXME false positive on glibc but impossible to fix generically
        "             , ARM_32     ,        , armv7a-none-eabi",
        "             , ARM_32     ,        , armv7a-none-eabihf",
        "             , ARM_32     ,        , armv7r-none-eabi",
        "             , ARM_32     ,        , armv7r-none-eabihf",
        "             , ARM_32_BE  ,        , armebv7r-none-eabi",
        "             , ARM_32_BE  ,        , armebv7r-none-eabihf",
        "             , ARM_64     ,        , aarch64-unknown-hermit",
        "             , ARM_64     ,        , aarch64-unknown-none",
        "             , ARM_64     ,        , aarch64-unknown-none-softfloat",
        "             , MIPS_32_LE ,        , mipsel-sony-psp",
        "             , MIPS_32_LE ,        , mipsel-unknown-none",
        "             , RISCV_32   ,        , riscv32i-unknown-none-elf",
        "             , RISCV_32   ,        , riscv32imac-unknown-none-elf",
        "             , RISCV_32   ,        , riscv32imc-unknown-none-elf",
        "             , RISCV_64   ,        , riscv64gc-unknown-none-elf",
        "             , RISCV_64   ,        , riscv64imac-unknown-none-elf",
        "             , X86_32     ,        , i686-unknown-uefi",
        "             , X86_64     ,        , x86_64-unknown-hermit",
        "             , X86_64     ,        , x86_64-unknown-none-hermitkernel",
        "             , X86_64     ,        , x86_64-unknown-uefi",
        "             , X86_64     , UCLIBC , x86_64-unknown-l4re-uclibc",
        "ANDROID      ,            ,        , thumbv7neon-linux-androideabi",
        "ANDROID      , ARM_32     ,        , arm-linux-androideabi",
        "ANDROID      , ARM_32     ,        , armv7-linux-androideabi",
        "ANDROID      , ARM_64     ,        , aarch64-linux-android",
        "ANDROID      , X86_32     ,        , i686-linux-android",
        "ANDROID      , X86_64     ,        , x86_64-linux-android",
        "DARWIN       , ARM_32     ,        , armv7-apple-ios",
        "DARWIN       , ARM_32     ,        , armv7s-apple-ios",
        "DARWIN       , ARM_64     ,        , aarch64-apple-darwin",
        "DARWIN       , ARM_64     ,        , aarch64-apple-ios",
        "DARWIN       , ARM_64     ,        , aarch64-apple-ios-macabi",
        "DARWIN       , ARM_64     ,        , aarch64-apple-ios-sim",
        "DARWIN       , ARM_64     ,        , aarch64-apple-tvos",
        "DARWIN       , X86_32     ,        , i386-apple-ios",
        "DARWIN       , X86_32     ,        , i686-apple-darwin",
        "DARWIN       , X86_64     ,        , x86_64-apple-darwin",
        "DARWIN       , X86_64     ,        , x86_64-apple-ios",
        "DARWIN       , X86_64     ,        , x86_64-apple-ios-macabi",
        "DARWIN       , X86_64     ,        , x86_64-apple-tvos",
        "DRAGONFLYBSD , X86_64     ,        , x86_64-unknown-dragonfly",
        "FREEBSD      , ARM_32     ,        , armv6-unknown-freebsd",
        "FREEBSD      , ARM_32     ,        , armv7-unknown-freebsd",
        "FREEBSD      , ARM_64     ,        , aarch64-unknown-freebsd",
        "FREEBSD      , PPC_64     ,        , powerpc64-unknown-freebsd",
        "FREEBSD      , X86_32     ,        , i686-unknown-freebsd",
        "FREEBSD      , X86_64     ,        , x86_64-unknown-freebsd",
        "FUCHSIA      , ARM_64     ,        , aarch64-fuchsia",
        "FUCHSIA      , X86_64     ,        , x86_64-fuchsia",
        "HAIKU        , X86_32     ,        , i686-unknown-haiku",
        "HAIKU        , X86_64     ,        , x86_64-unknown-haiku",
        "ILLUMOS      , X86_64     ,        , x86_64-unknown-illumos",
        "LINUX        ,            , GLIBC  , thumbv7neon-unknown-linux-gnueabihf",
        "LINUX        ,            , MUSL   , hexagon-unknown-linux-musl",
        "LINUX        ,            , MUSL   , thumbv7neon-unknown-linux-musleabihf",
        "LINUX        , ARM_32     , GLIBC  , arm-unknown-linux-gnueabi",
        "LINUX        , ARM_32     , GLIBC  , arm-unknown-linux-gnueabihf",
        "LINUX        , ARM_32     , GLIBC  , armv4t-unknown-linux-gnueabi",
        "LINUX        , ARM_32     , GLIBC  , armv5te-unknown-linux-gnueabi",
        "LINUX        , ARM_32     , GLIBC  , armv7-unknown-linux-gnueabi",
        "LINUX        , ARM_32     , GLIBC  , armv7-unknown-linux-gnueabihf",
        "LINUX        , ARM_32     , MUSL   , arm-unknown-linux-musleabi",
        "LINUX        , ARM_32     , MUSL   , arm-unknown-linux-musleabihf",
        "LINUX        , ARM_32     , MUSL   , armv5te-unknown-linux-musleabi",
        "LINUX        , ARM_32     , MUSL   , armv7-unknown-linux-musleabi",
        "LINUX        , ARM_32     , MUSL   , armv7-unknown-linux-musleabihf",
        "LINUX        , ARM_32     , UCLIBC , armv5te-unknown-linux-uclibceabi",
        "LINUX        , ARM_64     , GLIBC  , aarch64-unknown-linux-gnu",
        "LINUX        , ARM_64     , GLIBC  , aarch64-unknown-linux-gnu_ilp32",
        "LINUX        , ARM_64     , MUSL   , aarch64-unknown-linux-musl",
        "LINUX        , ARM_64_BE  , GLIBC  , aarch64_be-unknown-linux-gnu",
        "LINUX        , ARM_64_BE  , GLIBC  , aarch64_be-unknown-linux-gnu_ilp32",
        "LINUX        , IBMZ_64    , GLIBC  , s390x-unknown-linux-gnu",
        "LINUX        , IBMZ_64    , MUSL   , s390x-unknown-linux-musl",
        "LINUX        , MIPS_32    , GLIBC  , mips-unknown-linux-gnu",
        "LINUX        , MIPS_32    , GLIBC  , mipsisa32r6-unknown-linux-gnu",
        "LINUX        , MIPS_32    , MUSL   , mips-unknown-linux-musl",
        "LINUX        , MIPS_32    , UCLIBC , mips-unknown-linux-uclibc",
        "LINUX        , MIPS_32_LE , GLIBC  , mipsel-unknown-linux-gnu",
        "LINUX        , MIPS_32_LE , GLIBC  , mipsisa32r6el-unknown-linux-gnu",
        "LINUX        , MIPS_32_LE , MUSL   , mipsel-unknown-linux-musl",
        "LINUX        , MIPS_32_LE , UCLIBC , mipsel-unknown-linux-uclibc",
        "LINUX        , MIPS_64    , GLIBC  , mips64-unknown-linux-gnuabi64",
        "LINUX        , MIPS_64    , GLIBC  , mipsisa64r6-unknown-linux-gnuabi64",
        "LINUX        , MIPS_64    , MUSL   , mips64-unknown-linux-muslabi64",
        "LINUX        , MIPS_64_LE , GLIBC  , mips64el-unknown-linux-gnuabi64",
        "LINUX        , MIPS_64_LE , GLIBC  , mipsisa64r6el-unknown-linux-gnuabi64",
        "LINUX        , MIPS_64_LE , MUSL   , mips64el-unknown-linux-muslabi64",
        "LINUX        , PPC_32     , GLIBC  , powerpc-unknown-linux-gnu",
        "LINUX        , PPC_32     , GLIBC  , powerpc-unknown-linux-gnuspe",
        "LINUX        , PPC_32     , MUSL   , powerpc-unknown-linux-musl",
        "LINUX        , PPC_64     , GLIBC  , powerpc64-unknown-linux-gnu",
        "LINUX        , PPC_64     , MUSL   , powerpc64-unknown-linux-musl",
        "LINUX        , PPC_64_LE  , GLIBC  , powerpc64le-unknown-linux-gnu",
        "LINUX        , PPC_64_LE  , MUSL   , powerpc64le-unknown-linux-musl",
        "LINUX        , RISCV_32   , GLIBC  , riscv32gc-unknown-linux-gnu",
        "LINUX        , RISCV_32   , MUSL   , riscv32gc-unknown-linux-musl",
        "LINUX        , RISCV_64   , GLIBC  , riscv64gc-unknown-linux-gnu",
        "LINUX        , RISCV_64   , MUSL   , riscv64gc-unknown-linux-musl",
        "LINUX        , SPARC_32   , GLIBC  , sparc-unknown-linux-gnu",
        "LINUX        , SPARC_64   , GLIBC  , sparc64-unknown-linux-gnu",
        "LINUX        , X86_32     , GLIBC  , i586-unknown-linux-gnu",
        "LINUX        , X86_32     , GLIBC  , i686-unknown-linux-gnu",
        "LINUX        , X86_32     , MUSL   , i586-unknown-linux-musl",
        "LINUX        , X86_32     , MUSL   , i686-unknown-linux-musl",
        "LINUX        , X86_64     ,        , x86_64-fortanix-unknown-sgx", // FIXME false positive (?) because fortanix ends in nix
        "LINUX        , X86_64     ,        , x86_64-unknown-none-linuxkernel",
        "LINUX        , X86_64     , GLIBC  , x86_64-unknown-linux-gnu",
        "LINUX        , X86_64     , GLIBC  , x86_64-unknown-linux-gnux32",
        "LINUX        , X86_64     , MUSL   , x86_64-unknown-linux-musl",
        "NETBSD       , ARM_32     ,        , armv6-unknown-netbsd-eabihf",
        "NETBSD       , ARM_32     ,        , armv7-unknown-netbsd-eabihf",
        "NETBSD       , ARM_64     ,        , aarch64-unknown-netbsd",
        "NETBSD       , PPC_32     ,        , powerpc-unknown-netbsd",
        "NETBSD       , SPARC_64   ,        , sparc64-unknown-netbsd",
        "NETBSD       , X86_32     ,        , i686-unknown-netbsd",
        "NETBSD       , X86_64     ,        , x86_64-unknown-netbsd",
        "OPENBSD      , ARM_64     ,        , aarch64-unknown-openbsd",
        "OPENBSD      , PPC_32     ,        , powerpc-unknown-openbsd",
        "OPENBSD      , SPARC_64   ,        , sparc64-unknown-openbsd",
        "OPENBSD      , X86_32     ,        , i686-unknown-openbsd",
        "OPENBSD      , X86_64     ,        , x86_64-unknown-openbsd",
        "REDOX        , ARM_64     ,        , aarch64-unknown-redox",
        "REDOX        , X86_64     ,        , x86_64-unknown-redox",
        "SOLARIS      , SPARC_64   ,        , sparcv9-sun-solaris",
        "SOLARIS      , X86_64     ,        , x86_64-pc-solaris",
        "SOLARIS      , X86_64     ,        , x86_64-sun-solaris",
        "VXWORKS      , ARM_32     ,        , armv7-wrs-vxworks-eabihf",
        "VXWORKS      , ARM_64     ,        , aarch64-wrs-vxworks",
        "VXWORKS      , PPC_32     ,        , powerpc-wrs-vxworks",
        "VXWORKS      , PPC_32     ,        , powerpc-wrs-vxworks-spe",
        "VXWORKS      , PPC_64     ,        , powerpc64-wrs-vxworks",
        "VXWORKS      , X86_32     ,        , i686-wrs-vxworks",
        "VXWORKS      , X86_64     ,        , x86_64-wrs-vxworks",
        "WINDOWS      ,            , MSVC   , thumbv7a-pc-windows-msvc",
        "WINDOWS      ,            , MSVC   , thumbv7a-uwp-windows-msvc",
        "WINDOWS      , ARM_64     , MSVC   , aarch64-pc-windows-msvc",
        "WINDOWS      , ARM_64     , MSVC   , aarch64-uwp-windows-msvc",
        "WINDOWS      , X86_32     , GLIBC  , i686-pc-windows-gnu",
        "WINDOWS      , X86_32     , GLIBC  , i686-uwp-windows-gnu",
        "WINDOWS      , X86_32     , MSVC   , i586-pc-windows-msvc",
        "WINDOWS      , X86_32     , MSVC   , i686-pc-windows-msvc",
        "WINDOWS      , X86_32     , MSVC   , i686-uwp-windows-msvc",
        "WINDOWS      , X86_64     , GLIBC  , x86_64-pc-windows-gnu",
        "WINDOWS      , X86_64     , GLIBC  , x86_64-uwp-windows-gnu",
        "WINDOWS      , X86_64     , MSVC   , x86_64-pc-windows-msvc",
        "WINDOWS      , X86_64     , MSVC   , x86_64-uwp-windows-msvc",
        // endregion rust
        // region zig
        // $ zig targets | jq '.libc'
        // https://ziglang.org/learn/overview/#support-table
        "        ,            , MUSL  , wasm32-freestanding-musl",
        "DARWIN  , X86_64     , GLIBC , x86_64-macos-gnu",
        "LINUX   , ARM_32     , GLIBC , arm-linux-gnueabi",
        "LINUX   , ARM_32     , GLIBC , arm-linux-gnueabihf",
        "LINUX   , ARM_32     , MUSL  , arm-linux-musleabi",
        "LINUX   , ARM_32     , MUSL  , arm-linux-musleabihf",
        "LINUX   , ARM_32_BE  , GLIBC , armeb-linux-gnueabi",
        "LINUX   , ARM_32_BE  , GLIBC , armeb-linux-gnueabihf",
        "LINUX   , ARM_32_BE  , MUSL  , armeb-linux-musleabi",
        "LINUX   , ARM_32_BE  , MUSL  , armeb-linux-musleabihf",
        "LINUX   , ARM_64     , GLIBC , aarch64-linux-gnu",
        "LINUX   , ARM_64     , MUSL  , aarch64-linux-musl",
        "LINUX   , ARM_64_BE  , GLIBC , aarch64_be-linux-gnu",
        "LINUX   , ARM_64_BE  , MUSL  , aarch64_be-linux-musl",
        "LINUX   , IBMZ_64    , GLIBC , s390x-linux-gnu",
        "LINUX   , IBMZ_64    , MUSL  , s390x-linux-musl",
        "LINUX   , MIPS_32    , GLIBC , mips-linux-gnu",
        "LINUX   , MIPS_32    , MUSL  , mips-linux-musl",
        "LINUX   , MIPS_32_LE , GLIBC , mipsel-linux-gnu",
        "LINUX   , MIPS_32_LE , MUSL  , mipsel-linux-musl",
        "LINUX   , MIPS_64    , GLIBC , mips64-linux-gnuabi64",
        "LINUX   , MIPS_64    , GLIBC , mips64-linux-gnuabin32",
        "LINUX   , MIPS_64    , MUSL  , mips64-linux-musl",
        "LINUX   , MIPS_64_LE , GLIBC , mips64el-linux-gnuabi64",
        "LINUX   , MIPS_64_LE , GLIBC , mips64el-linux-gnuabin32",
        "LINUX   , MIPS_64_LE , MUSL  , mips64el-linux-musl",
        "LINUX   , PPC_32     , GLIBC , powerpc-linux-gnu",
        "LINUX   , PPC_32     , MUSL  , powerpc-linux-musl",
        "LINUX   , PPC_64     , GLIBC , powerpc64-linux-gnu",
        "LINUX   , PPC_64     , MUSL  , powerpc64-linux-musl",
        "LINUX   , PPC_64_LE  , GLIBC , powerpc64le-linux-gnu",
        "LINUX   , PPC_64_LE  , MUSL  , powerpc64le-linux-musl",
        "LINUX   , RISCV_64   , GLIBC , riscv64-linux-gnu",
        "LINUX   , RISCV_64   , MUSL  , riscv64-linux-musl",
        "LINUX   , SPARC_32   , GLIBC , sparc-linux-gnu",
        "LINUX   , SPARC_64   , GLIBC , sparcv9-linux-gnu",
        "LINUX   , X86_32     , GLIBC , i386-linux-gnu",
        "LINUX   , X86_32     , MUSL  , i386-linux-musl",
        "LINUX   , X86_64     , GLIBC , x86_64-linux-gnu",
        "LINUX   , X86_64     , GLIBC , x86_64-linux-gnux32",
        "LINUX   , X86_64     , MUSL  , x86_64-linux-musl",
        "WINDOWS , ARM_32     , GLIBC , arm-windows-gnu",
        "WINDOWS , ARM_32_BE  , GLIBC , armeb-windows-gnu",
        "WINDOWS , ARM_64     , GLIBC , aarch64-windows-gnu",
        "WINDOWS , ARM_64_BE  , GLIBC , aarch64_be-windows-gnu",
        "WINDOWS , X86_32     , GLIBC , i386-windows-gnu",
        "WINDOWS , X86_64     , GLIBC , x86_64-windows-gnu",
        // endregion zig

        // region buf
        // https://github.com/bufbuild/buf/releases
        "DARWIN , ARM_64 , , buf-Darwin-arm64",
        "DARWIN , ARM_64 , , buf-Darwin-arm64.tar.gz",
        "DARWIN , ARM_64 , , protoc-gen-buf-breaking-Darwin-arm64",
        "DARWIN , ARM_64 , , protoc-gen-buf-check-breaking-Darwin-arm64",
        "DARWIN , ARM_64 , , protoc-gen-buf-check-lint-Darwin-arm64",
        "DARWIN , ARM_64 , , protoc-gen-buf-lint-Darwin-arm64",
        "DARWIN , X86_64 , , buf-Darwin-x86_64",
        "DARWIN , X86_64 , , buf-Darwin-x86_64.tar.gz",
        "DARWIN , X86_64 , , protoc-gen-buf-breaking-Darwin-x86_64",
        "DARWIN , X86_64 , , protoc-gen-buf-check-breaking-Darwin-x86_64",
        "DARWIN , X86_64 , , protoc-gen-buf-check-lint-Darwin-x86_64",
        "DARWIN , X86_64 , , protoc-gen-buf-lint-Darwin-x86_64",
        "LINUX  , ARM_64 , , buf-Linux-aarch64",
        "LINUX  , ARM_64 , , buf-Linux-aarch64.tar.gz",
        "LINUX  , ARM_64 , , protoc-gen-buf-breaking-Linux-aarch64",
        "LINUX  , ARM_64 , , protoc-gen-buf-check-breaking-Linux-aarch64",
        "LINUX  , ARM_64 , , protoc-gen-buf-check-lint-Linux-aarch64",
        "LINUX  , ARM_64 , , protoc-gen-buf-lint-Linux-aarch64",
        "LINUX  , X86_64 , , buf-Linux-x86_64",
        "LINUX  , X86_64 , , buf-Linux-x86_64.tar.gz",
        "LINUX  , X86_64 , , protoc-gen-buf-breaking-Linux-x86_64",
        "LINUX  , X86_64 , , protoc-gen-buf-check-breaking-Linux-x86_64",
        "LINUX  , X86_64 , , protoc-gen-buf-check-lint-Linux-x86_64",
        "LINUX  , X86_64 , , protoc-gen-buf-lint-Linux-x86_64",
        // endregion buf
        // region protoc
        // https://github.com/protocolbuffers/protobuf/releases
        "DARWIN  , X86_64    , , protoc-3.17.3-osx-x86_64.zip",
        "LINUX   , ARM_64    , , protoc-3.17.3-linux-aarch_64.zip",
        "LINUX   , IBMZ_64   , , protoc-3.17.3-linux-s390_64.zip",
        "LINUX   , PPC_64_LE , , protoc-3.17.3-linux-ppcle_64.zip",
        "LINUX   , X86_32    , , protoc-3.17.3-linux-x86_32.zip",
        "LINUX   , X86_64    , , protoc-3.17.3-linux-x86_64.zip",
        "WINDOWS , X86_32    , , protoc-3.17.3-win32.zip",
        "WINDOWS , X86_64    , , protoc-3.17.3-win64.zip",
        // endregion protoc
        // region trustin/os-maven-plugin
        // https://github.com/trustin/os-maven-plugin
        "DARWIN  , X86_64    , , protoc-3.16.0-osx-x86_64.exe",
        "LINUX   , ARM_64    , , protoc-3.16.0-linux-aarch_64.exe",
        "LINUX   , IBMZ_64   , , protoc-3.16.0-linux-s390_64.exe",
        "LINUX   , PPC_64_LE , , protoc-3.16.0-linux-ppcle_64.exe",
        "LINUX   , X86_32    , , protoc-3.16.0-linux-x86_32.exe",
        "LINUX   , X86_64    , , protoc-3.16.0-linux-x86_64.exe",
        "WINDOWS , X86_32    , , protoc-3.16.0-windows-x86_32.exe",
        "WINDOWS , X86_64    , , protoc-3.16.0-windows-x86_64.exe",
        // endregion trustin/os-maven-plugin
        // region shellcheck
        // https://github.com/koalaman/shellcheck/releases
        "DARWIN , X86_64 , , shellcheck-v0.7.2.darwin.x86_64.tar.xz",
        "LINUX  , ARM_32 , , shellcheck-v0.7.2.linux.armv6hf.tar.xz",
        "LINUX  , ARM_64 , , shellcheck-v0.7.2.linux.aarch64.tar.xz",
        "LINUX  , X86_64 , , shellcheck-v0.7.2.linux.x86_64.tar.xz",
        // endregion shellcheck
    })
    void parse(
        final @Nullable Os os,
        final @Nullable Arch arch,
        final @Nullable Env env,
        final @NotNull String value
    ) {
        final Platform parse = Platform.parse(value);
        assertAll(
            () -> assertEquals(os, parse.os, "parse.os"),
            () -> assertEquals(arch, parse.arch, "parse.arch"),
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
