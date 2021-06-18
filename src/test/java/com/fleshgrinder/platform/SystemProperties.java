package com.fleshgrinder.platform;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class SystemProperties {
    static final @NotNull String OS_NAME = "os.name";
    static final @NotNull String OS_ARCH = "os.arch";
    static final @NotNull String VM_NAME = "java.vm.name";
    static final @NotNull String SUN_BITNESS = "sun.arch.data.model";
    static final @NotNull String IBM_BITNESS = "com.ibm.vm.bitmode";
    static final @NotNull String FS_SEP = "file.separator";

    private SystemProperties() {}

    static void set(final @NotNull String key, final @Nullable String value) {
        if (value != null) System.setProperty(key, value);
    }
}
