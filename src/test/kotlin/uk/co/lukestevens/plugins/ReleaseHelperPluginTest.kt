package uk.co.lukestevens.plugins

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * A simple unit test for the 'uk.co.lukestevens.plugins.release-helper' plugin.
 */
class ReleaseHelperPluginTest {
    @Test fun `plugin registers task`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("uk.co.lukestevens.plugins.release-helper")

        // Verify the result
        assertNotNull(project.tasks.findByName("exportProperties"))
        assertNotNull(project.tasks.findByName("finaliseVersion"))
        assertNotNull(project.tasks.findByName("bumpVersion"))
    }
}
