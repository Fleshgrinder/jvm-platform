# JVM Platform

[![Maven Central](https://img.shields.io/maven-central/v/com.fleshgrinder/jvm-platform)][Maven Central]
[![GitHub CI Workflow](https://img.shields.io/github/workflow/status/Fleshgrinder/jvm-platform/ci)](https://github.com/Fleshgrinder/jvm-platform/actions)
[![Code Coverage](https://img.shields.io/codecov/c/github/Fleshgrinder/jvm-platform)][CodeCov]

**JVM Platform** provides type-safe access to the Java Virtual Machine
(JVM) `os.name` and `os.arch` system properties with tested support for a wide
range of operating systems and architectures. It also provides standardized
platform identifiers that can be trusted and dependent upon, unlike the system
properties that are provided by the JVM. Additional optional support to further
refine a platform or detect features is available (e.g., detect [musl]). The
library is Java 1.8+ compatible, has zero dependencies, does not allocate
anything unless asked, features proper nullability information for perfect IDE
and Kotlin compatibility, and is rigorously tested (2,000+ test cases).

- **Install** from [Maven Central]
- **Usage** is [illustrated here][Usage.java]

## Motivation

There are many libraries out there that provide the ability to retrieve platform
information but all of them come along with a lot of additional baggage that is
not required. Most of them also allocate data globally throughout the entire
lifetime of the program, for no good reason since the information is most often
required only at certain points during execution to make a decision. The
allocation additionally makes little sense because the computation of the
information is extremely lightweight and probably never the bottleneck in any
serious program.

```java
final Platform platform = Platform.current();
System.out.println(platform.os);   // e.g. LINUX
System.out.println(platform.arch); // e.g. X86_64
System.out.println(platform);      // e.g. linux-x86-64
```

A feature that most libraries out there are missing is the ability to determine
the C ABI of the current JVM. [musl] is getting more and more important in a
world with containers and the JVM has support for it since a while, however, it
is not possible to find out if the current JVM was built against musl or glibc,
this complicates things when we have to make decisions on the kind of native
components we can use.

```java
if (Env.current() == Env.MUSL) {
    // should be a musl system
}
```

A feature that is missing from all libraries we found is the ability to parse
any kind of string to determine the platform. Something that is important when
dealing with native software where we might want to parse a filename or path and
get the corresponding information. A good use case here is for instance when we
want to redistribute some native executable that is published somewhere to a
Maven repository with Gradle (see [gradle-exe-plugin]).

```java
final Platform platform = Platform.parse("OpenJDK11U-jdk_x64_linux_hotspot_11.0.11_9.tar.gz");
assert platform.os == LINUX;
assert platform.arch == X86_64;
```

## Project Info

- Licensed under the [Unlicense], see [UNLICENSE.md] for details.
- Contributions are highly appreciated, see [CONTRIBUTING.md] for details.
- We use [semantic versioning] and [keep a changelog], available versions and
  changes are listed on our [releases] page.
- All [releases] are signed with
  `EBE5 EBC0 F49E 38A6 9FC7 EA26 7366 AE4A 6774 8172`
  ([keybase.io/fleshgrinder]).

<!-- @formatter:off -->
[CodeCov]: https://codecov.io/gh/Fleshgrinder/jvm-platform
[CONTRIBUTING.md]: https://github.com/Fleshgrinder/.github/blob/main/CONTRIBUTING.md
[gradle-exe-plugin]: https://github.com/Fleshgrinder/gradle-exe-plugin
[keep a changelog]: https://keepachangelog.com/
[keybase.io/fleshgrinder]: https://keybase.io/fleshgrinder
[Maven Central]: https://search.maven.org/artifact/com.fleshgrinder/jvm-platform
[musl]: https://musl.libc.org/
[releases]: https://github.com/Fleshgrinder/jvm-platform/releases
[semantic versioning]: http://semver.org/
[UNLICENSE.md]: UNLICENSE.md
[Unlicense]: https://unlicense.org/
[Usage.java]: src/test/java/com/fleshgrinder/platform/Usage.java
