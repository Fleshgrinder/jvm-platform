package com.fleshgrinder.junit

import org.intellij.lang.annotations.Language as L
import com.mifmif.common.regex.Generex
import org.junit.platform.commons.util.Preconditions

fun generex(@L("RegExp") pattern: String): Sequence<String> {
    val generex = Generex(pattern)
    Preconditions.condition(!generex.isInfinite) { "Pattern must not be infinite, got: $pattern" }
    return generex.allMatchedStrings.asSequence()
}

fun generex(@L("RegExp") p1: String, @L("RegExp") p2: String): Sequence<String> =
    sequenceOf(p1, p2).flatMap(::generex)

fun generex(@L("RegExp") p1: String, @L("RegExp") p2: String, @L("RegExp") p3: String): Sequence<String> =
    sequenceOf(p1, p2, p3).flatMap(::generex)

fun generex(@L("RegExp") p1: String, @L("RegExp") p2: String, @L("RegExp") p3: String, @L("RegExp") p4: String): Sequence<String> =
    sequenceOf(p1, p2, p3, p4).flatMap(::generex)

fun generexWithGarbage(@L("RegExp") pattern: String): Sequence<String> =
    generex("(garbage[ ._-])?${pattern.replace(" ?", "[ ._-]?")}([ ._-]garbage)?")

fun generexWithGarbage(@L("RegExp") p1: String, @L("RegExp") p2: String): Sequence<String> =
    sequenceOf(p1, p2).flatMap(::generexWithGarbage)

fun generexWithGarbage(@L("RegExp") p1: String, @L("RegExp") p2: String, @L("RegExp") p3: String): Sequence<String> =
    sequenceOf(p1, p2, p3).flatMap(::generexWithGarbage)

fun generexWithGarbage(@L("RegExp") p1: String, @L("RegExp") p2: String, @L("RegExp") p3: String, @L("RegExp") p4: String): Sequence<String> =
    sequenceOf(p1, p2, p3, p4).flatMap(::generexWithGarbage)
