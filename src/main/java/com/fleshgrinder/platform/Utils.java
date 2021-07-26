package com.fleshgrinder.platform;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

final class Utils {
    private Utils() {}

    /**
     * Transforms {@code UPPER_SNAKE_CASE} to {@code lower-dash-case}.
     *
     * @return the {@code lower-dash-case} string.
     * @throws IndexOutOfBoundsException if given chars are empty.
     * @throws NullPointerException if given chars are {@code null}.
     */
    @Contract(pure = true)
    static @NotNull String id(final @NotNull String chars) {
        final int l = chars.length();
        final StringBuilder sb = new StringBuilder(l);
        int i = 0;
        do {
            final char c = chars.charAt(i);
            if (c == '_') sb.append('-');
            else if ('A' <= c && c <= 'Z') sb.append((char) (c + 32));
            else sb.append(c);
        } while (++i < l);
        return sb.toString();
    }

    /**
     * Normalizes the given chars by lowering all upper chars and replacing all
     * non-alphanumeric chars with a dash ({@code -}).
     *
     * <p>The combination of normalizing to lowercase and a dash as replacement
     * character is chosen to ensure that any valid string identifier of an
     * {@link Os} or {@link Arch} stays intact and that we can perform a fast
     * match even if someone calls a {@code parse} method instead of a
     * {@code fromString} method. Using any other transformation would result
     * in losing this ability.
     *
     * <p>Note that we do not care about the names of the enum values. They
     * should not be used because they are Java coding convention specific and
     * not meant to be portable. Not that it really matters since this is a
     * Java library for Java programs, but it is about the principle of stable
     * and good API design as well as having identifiers that can be used
     * anywhere. Filenames on Unixoid systems simply settled for lower-dash-case
     * a long time ago, and we want to adhere to this convention.
     *
     * @param chars to normalize.
     * @return the normalized string.
     * @throws NullPointerException if {@code chars} is {@code null}.
     * @see #normalize(CharSequence, boolean)
     */
    @Contract(pure = true)
    static @NotNull String normalize(final @NotNull CharSequence chars) {
        return normalize(chars, false);
    }

    /**
     * Normalizes the given chars by lowering all upper chars and either
     * stripping or replacing all non-alphanumeric chars.
     *
     * @param chars to normalize.
     * @param strip non-alphanumeric chars ({@code true}) or replace them with a
     *     dash ({@code -}, {@code false}).
     * @return the normalized string.
     * @throws NullPointerException if {@code chars} is {@code null}.
     * @see #normalize(CharSequence)
     */
    @Contract(pure = true)
    static @NotNull String normalize(final @NotNull CharSequence chars, final boolean strip) {
        final int l = chars.length();
        if (l == 0) return "";
        final StringBuilder sb = new StringBuilder(l);
        int i = 0;
        do {
            final char c = chars.charAt(i);
            if (('0' <= c && c <= '9') || ('a' <= c && c <= 'z')) sb.append(c);
            else if ('A' <= c && c <= 'Z') sb.append((char) (c + 32));
            else if (!strip) sb.append('-');
        } while (++i < l);
        return sb.toString();
    }
}
