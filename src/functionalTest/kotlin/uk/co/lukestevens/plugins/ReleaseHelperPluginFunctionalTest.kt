package uk.co.lukestevens.plugins

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * A simple functional test for the 'uk.co.lukestevens.plugins.release-helper' plugin.
 */
class ReleaseHelperPluginFunctionalTest {

    private lateinit var projectDir: File

    @BeforeTest fun setup(){
        // Setup the test build
        projectDir = File("build/functionalTest")
        projectDir.mkdirs()
        projectDir.resolve("settings.gradle").writeText("""rootProject.name = "release-helper-functional-test"""")
        projectDir.resolve("gradle.properties").writeText("version=1.0.0-test")
        projectDir.resolve("build.gradle").writeText("""
            plugins {
                id('uk.co.lukestevens.plugins.release-helper')
            }
            
            releaseHelper {
                versionSuffix = '-test'
                exportFile = new File(projectDir, 'testexport.properties')
            }
        """.trimIndent())
    }

    fun runTask(task: String): BuildResult = GradleRunner.create()
            .forwardOutput()
            .withPluginClasspath()
            .withArguments(task)
            .withProjectDir(projectDir)
            .build()

    @Test fun `exportProperties writes version and name`() {
        // Run the build
        runTask("exportProperties")

        // Verify the result
        projectDir.resolve("testexport.properties").readLines().apply {
            assertEquals(2, size)
            assertEquals("PROJECT_VERSION=1.0.0-test", get(0))
            assertEquals("PROJECT_NAME=release-helper-functional-test", get(1))
        }
    }

    @Test fun `bumpVersion increments version`() {
        // Run the build
        runTask("bumpVersion")

        // Verify the result
        projectDir.resolve("gradle.properties").readLines().apply {
            assertEquals(1, size)
            assertEquals("version=1.1.0-test", get(0))
        }
    }

    @Test fun `finaliseVersion removes suffix`() {
        // Run the build
        runTask("finaliseVersion")

        // Verify the result
        projectDir.resolve("gradle.properties").readLines().apply {
            assertEquals(1, size)
            assertEquals("version=1.0.0", get(0))
        }
    }

    @AfterTest fun teardown(){
        projectDir.resolve("settings.gradle").delete()
        projectDir.resolve("gradle.properties").delete()
        projectDir.resolve("build.gradle").delete()
        projectDir.resolve("testexport.properties").delete()
    }
}
