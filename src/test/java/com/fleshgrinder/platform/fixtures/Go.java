package com.fleshgrinder.platform.fixtures;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashMap;
import java.util.Map;

import static com.fleshgrinder.platform.Arch.*;
import static com.fleshgrinder.platform.Os.*;

/**
 * {@code go tool dist list}
 *
 * https://github.com/golang/go/blob/master/src/go/build/syslist.go
 */
public final class Go {
    public static final @NotNull @Unmodifiable Map<String, PlatformSpec> TARGETS =
        new HashMap<String, PlatformSpec>() {{
            put("aix/ppc64", new PlatformSpec(AIX, PPC_64));
            put("android/386", new PlatformSpec(LINUX, X86_32));
            put("android/amd64", new PlatformSpec(LINUX, X86_64));
            put("android/arm", new PlatformSpec(LINUX, ARM_32));
            put("android/arm64", new PlatformSpec(LINUX, ARM_64));
            put("darwin/amd64", new PlatformSpec(DARWIN, X86_64));
            put("darwin/arm64", new PlatformSpec(DARWIN, ARM_64));
            put("dragonfly/amd64", new PlatformSpec(DRAGONFLYBSD, X86_64));
            put("freebsd/386", new PlatformSpec(FREEBSD, X86_32));
            put("freebsd/amd64", new PlatformSpec(FREEBSD, X86_64));
            put("freebsd/arm", new PlatformSpec(FREEBSD, ARM_32));
            put("freebsd/arm64", new PlatformSpec(FREEBSD, ARM_64));
            put("illumos/amd64", new PlatformSpec(ILLUMOS, X86_64));
            put("ios/amd64", new PlatformSpec(DARWIN, X86_64));
            put("ios/arm64", new PlatformSpec(DARWIN, ARM_64));
            put("js/wasm", new PlatformSpec());
            put("linux/386", new PlatformSpec(LINUX, X86_32));
            put("linux/amd64", new PlatformSpec(LINUX, X86_64));
            put("linux/arm", new PlatformSpec(LINUX, ARM_32));
            put("linux/arm64", new PlatformSpec(LINUX, ARM_64));
            put("linux/mips", new PlatformSpec(LINUX, MIPS_32));
            put("linux/mips64", new PlatformSpec(LINUX, MIPS_64));
            put("linux/mips64le", new PlatformSpec(LINUX, MIPS_64_LE));
            put("linux/mipsle", new PlatformSpec(LINUX, MIPS_32_LE));
            put("linux/ppc64", new PlatformSpec(LINUX, PPC_64));
            put("linux/ppc64le", new PlatformSpec(LINUX, PPC_64_LE));
            put("linux/riscv64", new PlatformSpec(LINUX, RISCV_64));
            put("linux/s390x", new PlatformSpec(LINUX, IBMZ_64));
            put("netbsd/386", new PlatformSpec(NETBSD, X86_32));
            put("netbsd/amd64", new PlatformSpec(NETBSD, X86_64));
            put("netbsd/arm", new PlatformSpec(NETBSD, ARM_32));
            put("netbsd/arm64", new PlatformSpec(NETBSD, ARM_64));
            put("openbsd/386", new PlatformSpec(OPENBSD, X86_32));
            put("openbsd/amd64", new PlatformSpec(OPENBSD, X86_64));
            put("openbsd/arm", new PlatformSpec(OPENBSD, ARM_32));
            put("openbsd/arm64", new PlatformSpec(OPENBSD, ARM_64));
            put("openbsd/mips64", new PlatformSpec(OPENBSD, MIPS_64));
            put("plan9/386", new PlatformSpec(PLAN9, X86_32));
            put("plan9/amd64", new PlatformSpec(PLAN9, X86_64));
            put("plan9/arm", new PlatformSpec(PLAN9, ARM_32));
            put("solaris/amd64", new PlatformSpec(SOLARIS, X86_64));
            put("windows/386", new PlatformSpec(WINDOWS, X86_32));
            put("windows/amd64", new PlatformSpec(WINDOWS, X86_64));
            put("windows/arm", new PlatformSpec(WINDOWS, ARM_32));
        }};
}
