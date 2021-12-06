package uk.co.lukestevens.plugins

import org.gradle.api.provider.Property
import java.io.File

interface ReleaseHelperExtension {
    val versionSuffix: Property<String>
    val exportFile: Property<File>
}