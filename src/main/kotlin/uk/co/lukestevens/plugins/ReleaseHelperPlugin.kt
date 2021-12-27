package uk.co.lukestevens.plugins

import org.gradle.api.Project
import org.gradle.api.Plugin
import java.io.File

class ReleaseHelperPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("releaseHelper", ReleaseHelperExtension::class.java)
        val defaultExportFile = File(System.getenv("GITHUB_ENV")?: "export.properties")

        project.tasks.register("exportProperties") { task ->
            task.doLast {
                extension.exportFile.getOrElse(defaultExportFile).bufferedWriter().use {
                    it.appendLine("PROJECT_VERSION=${project.version}")
                    it.appendLine("PROJECT_NAME=${project.name}")
                }
            }
        }

        project.tasks.register("finaliseVersion") { task ->
            task.doLast {
                val currentVersion = project.version.toString()
                val newVersion = removeSuffix(currentVersion)
                if(currentVersion != newVersion) {
                    project.writeVersion(newVersion)
                }
            }
        }

        project.tasks.register("bumpVersion") { task ->
            task.doLast {
                val currentVersion = project.version.toString()
                val finalVersion = removeSuffix(currentVersion)
                val versionParts = finalVersion.split(".")
                if(versionParts.size < 2){
                    throw IllegalArgumentException("Version must have at least major and minor identifier")
                }

                // Add major and minor versions
                val newVersion = StringBuilder(versionParts[0])
                    .append(".")
                    .append(Integer.parseInt(versionParts[1]) + 1)
                for(i in 2 until versionParts.size){
                    newVersion.append(".0") // zero all other parts
                }
                newVersion.append(extension.versionSuffix.getOrElse(""))

                project.writeVersion(newVersion.toString())
            }
        }
    }

    private fun Project.writeVersion(newVersion: String) {
        File(projectDir, "gradle.properties")
            .bufferedWriter().use {
                it.appendLine("version=$newVersion")
            }
    }

    private fun removeSuffix(version: String): String = version.substringBefore("-")
}
