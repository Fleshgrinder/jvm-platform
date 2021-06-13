package com.fleshgrinder.platform;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

final class Utils {
    private Utils() {}

    /**
     * Transforms {@code UPPER_SNAKE_CASE} to {@code lower-dash-case}.
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
     * @param chars to normalize
     * @return normalized string
     * @throws NullPointerException if {@code chars} is {@code null}
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
     * @param chars to normalize
     * @param strip non-alphanumeric chars ({@code true}) or replace them with a
     *     dash ({@code -}, {@code false})
     * @return normalized string
     * @throws NullPointerException if {@code chars} is {@code null}
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
