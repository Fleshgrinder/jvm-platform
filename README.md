# JVM Platform

[![Maven Central](https://img.shields.io/maven-central/v/com.fleshgrinder/jvm-platform)][Maven Central]
[![GitHub CI Workflow](https://img.shields.io/github/workflow/status/Fleshgrinder/jvm-platform/ci)](https://github.com/Fleshgrinder/jvm-platform/actions)
[![Code Coverage](https://img.shields.io/codecov/c/github/Fleshgrinder/jvm-platform)][CodeCov]
[![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/6452/badge)](https://bestpractices.coreinfrastructure.org/projects/6452)

**JVM Platform** provides type-safe access to the Java Virtual Machine
(JVM) `os.name` and `os.arch` system properties with tested support for a wide
range of operating systems and architectures. It also provides standardized
platform identifiers that can be trusted and dependent upon, unlike the system
properties that are provided by the JVM. Additional optional support to further
refine a platform or detect features is available (e.g., detect [musl]). The
library is Java 1.8+ compatible, has zero dependencies, does not allocate
anything unless asked, features proper nullability information for perfect IDE
and Kotlin compatibility, and is rigorously tested (almost 100,000 test cases).

## Installation

Go to [Maven Central] where you find the latest release and the code required
for your dependency management tool.

## Motivation

### Lightweight

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
System.out.println(platform.getOs());   // e.g. LINUX
System.out.println(platform.getArch()); // e.g. X86_64
System.out.println(platform);           // e.g. linux-x86-64
```

### musl

A feature that most libraries out there are missing is the ability to determine
the C ABI of the current JVM. [musl] is getting more and more important in a
world with containers and the JVM has support for it since a while, however, it
is not possible to find out if the current JVM was built against musl or glibc,
this complicates things when we have to make decisions on the kind of native
components we can use.

```java
if (Platform.hasMusl()) {
    // should be a musl system
}
```

### Parser

A feature that is missing from all libraries we found is the ability to parse
any kind of string to determine the platform. Something that is important when
dealing with native software where we might want to parse a filename or path and
get the corresponding information. A good use case here is for instance when we
want to redistribute some native executable that is published somewhere to a
Maven repository with Gradle (see [gradle-exe-plugin]).

```java
final Platform platform=Platform.parse("OpenJDK11U-jdk_x64_linux_hotspot_11.0.11_9.tar.gz");
assert platform.getOs() == LINUX;
assert platform.getArch() == X86_64;
```

### Nullability

Dealing with nullability is an issue in Java and this is why the code of this
library makes extensive use of the [JetBrains Java Annotations] to specify
nullability everywhere. Additionally, methods that may throw have a counterpart
that returns `null` instead. Making sure that you never have to use exceptions
for control flow and to improve your Kotlin experience.

```kotlin
runCatching { Platform.current() }
Platform.currentOrNull()?.let { }

runCatching { Platform.fromString("linux-x86-64") }
Platform.fromStringOrNull("linux-x86-64")?.let { }

runCatching { Platform.parse("Linux X86-64") }
Platform.parseOrNull("Linux X86-64")?.let { }
```

## Project Info

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
[JetBrains Java Annotations]: https://github.com/JetBrains/java-annotations
[keep a changelog]: https://keepachangelog.com/
[keybase.io/fleshgrinder]: https://keybase.io/fleshgrinder
[Maven Central]: https://search.maven.org/artifact/com.fleshgrinder/jvm-platform
[musl]: https://musl.libc.org/
[releases]: https://github.com/Fleshgrinder/jvm-platform/releases
[semantic versioning]: http://semver.org/
