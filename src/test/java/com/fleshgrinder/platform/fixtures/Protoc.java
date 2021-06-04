package com.fleshgrinder.platform.fixtures;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashMap;
import java.util.Map;

import static com.fleshgrinder.platform.Arch.*;
import static com.fleshgrinder.platform.Os.*;

/**
 * https://repo1.maven.org/maven2/com/google/protobuf/protoc/3.16.0/
 */
public final class Protoc {
    public static final @NotNull @Unmodifiable Map<String, PlatformSpec> TARGETS =
        new HashMap<String, PlatformSpec>() {{
            put("protoc-3.16.0-linux-aarch_64.exe", new PlatformSpec(LINUX, ARM_64));
            put("protoc-3.16.0-linux-ppcle_64.exe", new PlatformSpec(LINUX, PPC_64_LE));
            put("protoc-3.16.0-linux-s390_64.exe", new PlatformSpec(LINUX, IBMZ_64));
            put("protoc-3.16.0-linux-x86_32.exe", new PlatformSpec(LINUX, X86_32));
            put("protoc-3.16.0-linux-x86_64.exe", new PlatformSpec(LINUX, X86_64));
            put("protoc-3.16.0-osx-x86_64.exe", new PlatformSpec(DARWIN, X86_64));
            put("protoc-3.16.0-windows-x86_32.exe", new PlatformSpec(WINDOWS, X86_32));
            put("protoc-3.16.0-windows-x86_64.exe", new PlatformSpec(WINDOWS, X86_64));
        }};
}
