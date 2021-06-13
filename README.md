# JVM Platform

![GitHub CI Workflow](https://img.shields.io/github/workflow/status/Fleshgrinder/jvm-platform/ci)
[![Maven Central](https://img.shields.io/maven-central/v/com.fleshgrinder/jvm-platform.svg)][Maven Central]
[![Code Coverage](https://codecov.io/gh/Fleshgrinder/jvm-platform/branch/master/graph/badge.svg)][CodeCov]

**JVM Platform** provides type-safe access to the Java Virtual Machine (
JVM) `os.name` and `os.arch` system properties with tested support for a wide
range of operating systems and architectures. It also provides standardized
platform identifiers that can be trusted and dependent upon, unlike the system
properties that are provided by the JVM. Additional optional support to further
refine a platform or detect features is available (e.g., detect [musl]). The
library has zero dependencies, does not allocate anything globally, proper
nullability information for perfect IDE and Kotlin compatibility, and is
rigorously tested.

The library is available via [Maven Central], you can get the code for your
dependency manager from their with the version you desire.

Usage of the library is [illustrated in a test](src/test/java/com/fleshgrinder/platform/Usage.java).

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
[CONTRIBUTING.md]: https://github.com/Fleshgrinder/.github/CONTRIBUTING.md
[keep a changelog]: https://keepachangelog.com/
[keybase.io/fleshgrinder]: https://keybase.io/fleshgrinder
[Maven Central]: https://search.maven.org/artifact/com.fleshgrinder/jvm-platform
[musl]: https://musl.libc.org/
[releases]: https://github.com/Fleshgrinder/jvm-platform/releases
[semantic versioning]: http://semver.org/
[UNLICENSE.md]: UNLICENSE.md
[Unlicense]: https://unlicense.org/
