# JVM Platform

**JVM Platform** provides type-safe access to the Java Virtual Machine (JVM)
`os.name` and `os.arch` system properties with tested support for a wide range
of operating systems and architectures. It also provides standardized platform
identifiers that can be trusted and dependent upon, unlike the system properties
that are provided by the JVM. Additional optional support to further refine a
platform or detect features is available (e.g., detect [musl]).

## Testing
https://github.com/dbhi/qus

<!-- @formatter:off -->
[musl]: https://musl.libc.org/
