package com.fleshgrinder.platform.fixtures;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashMap;
import java.util.Map;

import static com.fleshgrinder.platform.Arch.*;
import static com.fleshgrinder.platform.Os.*;

/**
 * https://github.com/koalaman/shellcheck/releases
 */
public final class Shellcheck {
    public static final @NotNull @Unmodifiable Map<String, PlatformSpec> TARGETS =
        new HashMap<String, PlatformSpec>() {{
            put("shellcheck-v0.7.2.darwin.x86_64.tar.xz", new PlatformSpec(DARWIN, X86_64));
            put("shellcheck-v0.7.2.linux.aarch64.tar.xz", new PlatformSpec(LINUX, ARM_64));
            put("shellcheck-v0.7.2.linux.armv6hf.tar.xz", new PlatformSpec(LINUX, ARM_32));
            put("shellcheck-v0.7.2.linux.x86_64.tar.xz", new PlatformSpec(LINUX, X86_64));
        }};
}
