package com.fleshgrinder.platform.fixtures;

import com.fleshgrinder.platform.Arch;
import com.fleshgrinder.platform.Env;
import com.fleshgrinder.platform.Os;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class PlatformSpec {
    public final @Nullable Os os;
    public final @Nullable Arch arch;
    public final @Nullable Env env;

    public PlatformSpec(
        final @Nullable Os os,
        final @Nullable Arch arch,
        final @Nullable Env env
    ) {
        this.os = os;
        this.arch = arch;
        this.env = env;
    }

    public PlatformSpec() {
        this(null, null, null);
    }

    public PlatformSpec(final @Nullable Os os) {
        this(os, null, null);
    }

    public PlatformSpec(final @Nullable Arch arch) {
        this(null, arch, null);
    }

    public PlatformSpec(final @Nullable Env env) {
        this(null, null, env);
    }

    public PlatformSpec(final @Nullable Os os, final @Nullable Arch arch) {
        this(os, arch, null);
    }

    public PlatformSpec(final @Nullable Os os, final @Nullable Env env) {
        this(os, null, env);
    }

    public PlatformSpec(final @Nullable Arch arch, final @Nullable Env env) {
        this(null, arch, env);
    }

    @Override
    public boolean equals(final @Nullable Object other) {
        return this == other || (other instanceof PlatformSpec
            && Objects.equals(os, ((PlatformSpec) other).os)
            && Objects.equals(arch, ((PlatformSpec) other).arch)
            && Objects.equals(env, ((PlatformSpec) other).env)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(os, arch, env);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(os=" + os + ", arch=" + arch + ", env=" + env + ")";
    }
}
