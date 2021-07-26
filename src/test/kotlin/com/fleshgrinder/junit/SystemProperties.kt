package com.fleshgrinder.junit

inline fun <R> clearSystemProperties(names: Iterable<String>, action: () -> R): R {
    val tmp = names.mapNotNull { k -> System.clearProperty(k)?.let { k to it } }
    try {
        return action()
    } finally {
        System.getProperties().putAll(tmp)
    }
}

inline fun <R> clearSystemProperties(names: Collection<String>, action: () -> R): R =
    clearSystemProperties(names.asIterable(), action)

inline fun <R> clearSystemProperties(names: Array<out String>, action: () -> R): R =
    clearSystemProperties(names.asIterable(), action)

@JvmName("clearSystemPropertiesVariadic")
inline fun <R> clearSystemProperties(vararg names: String, action: () -> R): R =
    clearSystemProperties(names.asIterable(), action)

inline fun <R> clearSystemProperty(name: String, action: () -> R): R {
    val tmp = System.clearProperty(name)
    try {
        return action()
    } finally {
        if (tmp != null) System.setProperty(name, tmp)
    }
}

inline fun <R> withSystemProperties(props: Map<String, String>, action: () -> R): R {
    val tmp = props.mapNotNull { (k, v) -> System.setProperty(k, v)?.let { k to it } }
    try {
        return action()
    } finally {
        System.getProperties().putAll(tmp)
    }
}

inline fun <R> withSystemProperties(props: Iterable<Pair<String, String>>, action: () -> R): R =
    withSystemProperties(props.toMap(), action)

inline fun <R> withSystemProperties(props: Collection<Pair<String, String>>, action: () -> R): R =
    withSystemProperties(props.toMap(), action)

inline fun <R> withSystemProperties(props: Array<out Pair<String, String>>, action: () -> R): R =
    withSystemProperties(props.toMap(), action)

@JvmName("withSystemPropertiesVariadic")
inline fun <R> withSystemProperties(vararg props: Pair<String, String>, action: () -> R): R =
    withSystemProperties(props.toMap(), action)

inline fun <R> withSystemProperty(name: String, value: String, action: () -> R): R {
    val tmp = System.setProperty(name, value)
    try {
        return action()
    } finally {
        if (tmp != null) System.setProperty(name, tmp)
    }
}
