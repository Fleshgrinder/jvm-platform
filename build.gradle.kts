plugins {
    id("idea")
    id("java-library")

    // Only used in tests
    kotlin("jvm") version "1.5.21"
    id("jacoco")

    id("maven-publish")
    id("signing")
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

val javaVersion = file(".java-version").readText().trim()

dependencies {
    val jba = "21.0.0"
    compileOnly("org.jetbrains:annotations:$jba")
    testCompileOnly("org.jetbrains:annotations:$jba")

    testImplementation(platform(kotlin("bom")))
    testImplementation(kotlin("stdlib-jdk8"))
    testImplementation(platform("org.junit:junit-bom:5.7.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.github.mifmif:generex:1.0.2")
    testImplementation("nl.jqno.equalsverifier:equalsverifier:3.6.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

repositories.mavenCentral()

tasks {
    jar.configure {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }

    test.configure {
        useJUnitPlatform()
        systemProperty("java.io.tmpdir", temporaryDir.absolutePath)
    }

    jacocoTestReport.configure {
        shouldRunAfter(test)
        reports {
            xml.required.set(System.getenv().containsKey("CI"))
        }
    }

    check.configure {
        dependsOn(jacocoTestReport)
    }
}

idea {
    project {
        jdkName = javaVersion
        vcs = "Git"
        setLanguageLevel(javaVersion)
    }
    module {
        isDownloadJavadoc = false
        isDownloadSources = !System.getenv().containsKey("CI")
    }
}

java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = sourceCompatibility
    withJavadocJar()
    withSourcesJar()
}

signing {
    isRequired = !version.toString().endsWith("-SNAPSHOT")
    sign(publishing.publications)
}

nexusPublishing.repositories.sonatype().stagingProfileId.set(providers.gradleProperty("sonatype.stagingProfileId"))
publishing.publications.register<MavenPublication>("sonatype") {
    from(components["java"])
    pom {
        val gitHost = providers.gradleProperty("git.host")
        val gitOwner = providers.gradleProperty("git.owner")
        val gitUrl = gitHost.zip(gitOwner) { host, owner -> "https://$host/$owner/${project.name}" }
        name.set(providers.gradleProperty("displayName"))
        description.set(project.description)
        inceptionYear.set(providers.gradleProperty("inceptionYear"))
        properties.put(
            "commit",
            provider {
                val head = rootDir.resolve(".git/HEAD").readText().trim()
                if (head.startsWith("ref:")) {
                    rootDir.resolve(".git/${head.substringAfter(' ')}").readText().trim()
                } else {
                    head
                }
            },
        )
        url.set(gitUrl)
        developers {
            developer {
                id.set(providers.gradleProperty("author.id"))
                name.set(providers.gradleProperty("author.name"))
                email.set(providers.gradleProperty("author.email"))
                url.set(providers.gradleProperty("author.url"))
            }
        }
        issueManagement {
            system.set(gitHost)
            url.set(gitUrl.map { "$it/issues" })
        }
        licenses {
            license {
                name.set(providers.gradleProperty("license.name"))
                comments.set(providers.gradleProperty("license.comments"))
                url.set(providers.gradleProperty("license.url"))
                distribution.set(providers.gradleProperty("license.distribution"))
            }
        }
        scm {
            url.set(gitUrl)
            connection.set(gitUrl.map { "scm:$it.git" })
            developerConnection.set(gitHost.zip(gitOwner) { host, owner -> "scm:git@$host:$owner/${project.name}.git" })
        }
    }
    suppressAllPomMetadataWarnings()
    versionMapping {
        allVariants {
            fromResolutionOf(configurations.compileClasspath.name)
        }
    }
}
