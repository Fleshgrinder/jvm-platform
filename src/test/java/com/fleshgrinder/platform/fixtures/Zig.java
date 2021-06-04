package com.fleshgrinder.platform.fixtures;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashMap;
import java.util.Map;

import static com.fleshgrinder.platform.Arch.*;
import static com.fleshgrinder.platform.Env.*;
import static com.fleshgrinder.platform.Os.*;

/**
 * {@code zig targets | jq '.libc'}
 *
 * https://ziglang.org/learn/overview/#support-table
 */
public final class Zig {
    public static final @NotNull @Unmodifiable Map<String, PlatformSpec> TARGETS =
        new HashMap<String, PlatformSpec>() {{
            put("aarch64_be-linux-gnu", new PlatformSpec(LINUX, ARM_64_BE, GLIBC));
            put("aarch64_be-linux-musl", new PlatformSpec(LINUX, ARM_64_BE, MUSL));
            put("aarch64_be-windows-gnu", new PlatformSpec(WINDOWS, ARM_64_BE, GLIBC));
            put("aarch64-linux-gnu", new PlatformSpec(LINUX, ARM_64, GLIBC));
            put("aarch64-linux-musl", new PlatformSpec(LINUX, ARM_64, MUSL));
            put("aarch64-windows-gnu", new PlatformSpec(WINDOWS, ARM_64, GLIBC));
            put("armeb-linux-gnueabi", new PlatformSpec(LINUX, ARM_32_BE, GLIBC));
            put("armeb-linux-gnueabihf", new PlatformSpec(LINUX, ARM_32_BE, GLIBC));
            put("armeb-linux-musleabi", new PlatformSpec(LINUX, ARM_32_BE, MUSL));
            put("armeb-linux-musleabihf", new PlatformSpec(LINUX, ARM_32_BE, MUSL));
            put("armeb-windows-gnu", new PlatformSpec(WINDOWS, ARM_32_BE, GLIBC));
            put("arm-linux-gnueabi", new PlatformSpec(LINUX, ARM_32, GLIBC));
            put("arm-linux-gnueabihf", new PlatformSpec(LINUX, ARM_32, GLIBC));
            put("arm-linux-musleabi", new PlatformSpec(LINUX, ARM_32, MUSL));
            put("arm-linux-musleabihf", new PlatformSpec(LINUX, ARM_32, MUSL));
            put("arm-windows-gnu", new PlatformSpec(WINDOWS, ARM_32, GLIBC));
            put("i386-linux-gnu", new PlatformSpec(LINUX, X86_32, GLIBC));
            put("i386-linux-musl", new PlatformSpec(LINUX, X86_32, MUSL));
            put("i386-windows-gnu", new PlatformSpec(WINDOWS, X86_32, GLIBC));
            put("mips64el-linux-gnuabi64", new PlatformSpec(LINUX, MIPS_64_LE, GLIBC));
            put("mips64el-linux-gnuabin32", new PlatformSpec(LINUX, MIPS_64_LE, GLIBC));
            put("mips64el-linux-musl", new PlatformSpec(LINUX, MIPS_64_LE, MUSL));
            put("mips64-linux-gnuabi64", new PlatformSpec(LINUX, MIPS_64, GLIBC));
            put("mips64-linux-gnuabin32", new PlatformSpec(LINUX, MIPS_64, GLIBC));
            put("mips64-linux-musl", new PlatformSpec(LINUX, MIPS_64, MUSL));
            put("mipsel-linux-gnu", new PlatformSpec(LINUX, MIPS_32_LE, GLIBC));
            put("mipsel-linux-musl", new PlatformSpec(LINUX, MIPS_32_LE, MUSL));
            put("mips-linux-gnu", new PlatformSpec(LINUX, MIPS_32, GLIBC));
            put("mips-linux-musl", new PlatformSpec(LINUX, MIPS_32, MUSL));
            put("powerpc64le-linux-gnu", new PlatformSpec(LINUX, PPC_64_LE, GLIBC));
            put("powerpc64le-linux-musl", new PlatformSpec(LINUX, PPC_64_LE, MUSL));
            put("powerpc64-linux-gnu", new PlatformSpec(LINUX, PPC_64, GLIBC));
            put("powerpc64-linux-musl", new PlatformSpec(LINUX, PPC_64, MUSL));
            put("powerpc-linux-gnu", new PlatformSpec(LINUX, PPC_32, GLIBC));
            put("powerpc-linux-musl", new PlatformSpec(LINUX, PPC_32, MUSL));
            put("riscv64-linux-gnu", new PlatformSpec(LINUX, RISCV_64, GLIBC));
            put("riscv64-linux-musl", new PlatformSpec(LINUX, RISCV_64, MUSL));
            put("s390x-linux-gnu", new PlatformSpec(LINUX, IBMZ_64, GLIBC));
            put("s390x-linux-musl", new PlatformSpec(LINUX, IBMZ_64, MUSL));
            put("sparc-linux-gnu", new PlatformSpec(LINUX, SPARC_32, GLIBC));
            put("sparcv9-linux-gnu", new PlatformSpec(LINUX, SPARC_64, GLIBC));
            put("wasm32-freestanding-musl", new PlatformSpec(MUSL));
            put("x86_64-linux-gnu", new PlatformSpec(LINUX, X86_64, GLIBC));
            put("x86_64-linux-gnux32", new PlatformSpec(LINUX, X86_64, GLIBC));
            put("x86_64-linux-musl", new PlatformSpec(LINUX, X86_64, MUSL));
            put("x86_64-windows-gnu", new PlatformSpec(WINDOWS, X86_64, GLIBC));
            put("x86_64-macos-gnu", new PlatformSpec(DARWIN, X86_64, GLIBC));
        }};
}
