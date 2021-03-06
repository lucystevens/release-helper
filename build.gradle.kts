plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.12.0"

    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.5.32"
}

group = "uk.co.lukestevens"

repositories {
    mavenLocal()
    mavenCentral()
}

pluginBundle {
    website = "https://github.com/lukecmstevens/release-helper"
    vcsUrl = "https://github.com/lukecmstevens/release-helper"
    tags = listOf("githubactions", "versioning")
}

gradlePlugin {
    plugins {
        create("ReleaseHelperPlugin") {
            id = "uk.co.lukestevens.plugins.release-helper"
            displayName = "Release Helper Plugin"
            description = "A gradle plugin for simplifying parts of the release process"
            implementationClass = "uk.co.lukestevens.plugins.ReleaseHelperPlugin"
        }
    }
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") { }

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations.getByName("functionalTestImplementation")
    .extendsFrom(configurations.getByName("testImplementation"))

// Add a task to run the functional tests
val functionalTest by tasks.creating(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
}

val check by tasks.getting(Task::class) {
    // Run the functional tests as part of `check`
    dependsOn(functionalTest)
}
