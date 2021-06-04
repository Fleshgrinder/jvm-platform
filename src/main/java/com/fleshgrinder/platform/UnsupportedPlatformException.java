package com.fleshgrinder.platform;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Thrown if an aspect of a platform is not supported.
 */
public final class UnsupportedPlatformException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public UnsupportedPlatformException(
        final @NotNull String message
    ) {super(message);}

    public static @NotNull UnsupportedPlatformException fromValue(
        final @NotNull String aspect,
        final @Nullable CharSequence value
    ) {return new UnsupportedPlatformException("Unknown " + aspect + ": " + (value == null ? "<null>" : value));}

    public static <T> @NotNull UnsupportedPlatformException fromValue(
        final @NotNull Class<T> aspect,
        final @Nullable CharSequence value
    ) {return fromValue(aspect.getSimpleName(), value);}

    public static <T> @NotNull UnsupportedPlatformException fromSystemProperty(
        final @NotNull Class<T> aspect,
        final @NotNull String propertyName
    ) {return fromValue(aspect, System.getProperty(propertyName));}
}
