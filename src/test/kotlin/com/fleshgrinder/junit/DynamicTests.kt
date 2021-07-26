package com.fleshgrinder.junit

import java.util.Arrays
import java.util.stream.Stream
import org.junit.jupiter.api.DynamicTest

typealias Tests = Stream<DynamicTest>

fun Any?.toDisplayName(): String =
    toString().ifEmpty { "<empty>" }

fun <T> Array<T>.toTests(name: (T) -> String = Any?::toDisplayName, test: (T) -> Unit): Tests =
    DynamicTest.stream(iterator(), name, test)

fun <T> Collection<T>.toTests(name: (T) -> String = Any?::toDisplayName, test: (T) -> Unit): Tests =
    DynamicTest.stream(iterator(), name, test)

fun <T> Iterable<T>.toTests(name: (T) -> String = Any?::toDisplayName, test: (T) -> Unit): Tests =
    DynamicTest.stream(iterator(), name, test)

fun <T> Iterator<T>.toTests(name: (T) -> String = Any?::toDisplayName, test: (T) -> Unit): Tests =
    DynamicTest.stream(this, name, test)

fun <T> Sequence<T>.toTests(name: (T) -> String = Any?::toDisplayName, test: (T) -> Unit): Tests =
    DynamicTest.stream(iterator(), name, test)

fun <T> Stream<T>.toTests(name: (T) -> String = Any?::toDisplayName, test: (T) -> Unit): Tests =
    DynamicTest.stream(this, name, test)

fun <T> testsOf(vararg input: T, name: (T) -> String = Any?::toDisplayName, test: (T) -> Unit): Tests =
    DynamicTest.stream(Arrays.stream(input), name, test)

inline fun <reified E : Enum<E>> enumTestsOf(noinline name: (E) -> String = Enum<E>::toString, noinline test: (E) -> Unit): Tests =
    enumValues<E>().toTests(name, test)

inline fun <reified E : Enum<E>> enumTestsOf(vararg exclude: E, noinline name: (E) -> String = Enum<E>::toString, noinline test: (E) -> Unit): Tests =
    enumValues<E>().filterNot { it in exclude }.toTests(name, test)
