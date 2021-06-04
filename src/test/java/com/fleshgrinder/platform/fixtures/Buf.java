package com.fleshgrinder.platform.fixtures;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashMap;
import java.util.Map;

import static com.fleshgrinder.platform.Arch.*;
import static com.fleshgrinder.platform.Os.*;

/**
 * https://github.com/bufbuild/buf/releases
 */
public final class Buf {
    public static final @NotNull @Unmodifiable Map<String, PlatformSpec> TARGETS =
        new HashMap<String, PlatformSpec>() {{
            put("buf-Darwin-arm64", new PlatformSpec(DARWIN, ARM_64));
            put("buf-Darwin-arm64.tar.gz", new PlatformSpec(DARWIN, ARM_64));
            put("buf-Darwin-x86_64", new PlatformSpec(DARWIN, X86_64));
            put("buf-Darwin-x86_64.tar.gz", new PlatformSpec(DARWIN, X86_64));
            put("buf-Linux-aarch64", new PlatformSpec(LINUX, ARM_64));
            put("buf-Linux-aarch64.tar.gz", new PlatformSpec(LINUX, ARM_64));
            put("buf-Linux-x86_64", new PlatformSpec(LINUX, X86_64));
            put("buf-Linux-x86_64.tar.gz", new PlatformSpec(LINUX, X86_64));
            put("protoc-gen-buf-breaking-Darwin-arm64", new PlatformSpec(DARWIN, ARM_64));
            put("protoc-gen-buf-breaking-Darwin-x86_64", new PlatformSpec(DARWIN, X86_64));
            put("protoc-gen-buf-breaking-Linux-aarch64", new PlatformSpec(LINUX, ARM_64));
            put("protoc-gen-buf-breaking-Linux-x86_64", new PlatformSpec(LINUX, X86_64));
            put("protoc-gen-buf-check-breaking-Darwin-arm64", new PlatformSpec(DARWIN, ARM_64));
            put("protoc-gen-buf-check-breaking-Darwin-x86_64", new PlatformSpec(DARWIN, X86_64));
            put("protoc-gen-buf-check-breaking-Linux-aarch64", new PlatformSpec(LINUX, ARM_64));
            put("protoc-gen-buf-check-breaking-Linux-x86_64", new PlatformSpec(LINUX, X86_64));
            put("protoc-gen-buf-check-lint-Darwin-arm64", new PlatformSpec(DARWIN, ARM_64));
            put("protoc-gen-buf-check-lint-Darwin-x86_64", new PlatformSpec(DARWIN, X86_64));
            put("protoc-gen-buf-check-lint-Linux-aarch64", new PlatformSpec(LINUX, ARM_64));
            put("protoc-gen-buf-check-lint-Linux-x86_64", new PlatformSpec(LINUX, X86_64));
            put("protoc-gen-buf-lint-Darwin-arm64", new PlatformSpec(DARWIN, ARM_64));
            put("protoc-gen-buf-lint-Darwin-x86_64", new PlatformSpec(DARWIN, X86_64));
            put("protoc-gen-buf-lint-Linux-aarch64", new PlatformSpec(LINUX, ARM_64));
            put("protoc-gen-buf-lint-Linux-x86_64", new PlatformSpec(LINUX, X86_64));
        }};
}
