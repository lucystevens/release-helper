# release-helper
A gradle plugin for simplifying parts of the release process.

_Note:_ This plugin is still in pre-release and has [certain limitations](#limitations).

## Tasks
`exportProperties` exports the project version and name (as `PROJECT_VERSION` and `PROJECT_NAME`) to a file specified by the `exportFile` property.
This defaults to the value of the `GITHUB_ENV` environment variable, for easy use in GitHub action workflows.

`bumpVersion` removes any version suffixes (e.g. `-SNAPSHOT`), bumps the minor version, zeroes the patch version, and appends the suffix specified by the `versionSuffix` property.
This version is then written to the `gradle.properties` file.

`finaliseVersion` removes any version suffix, and writes the new version to the `gradle.properties` file.

## Quick start
_build.gradle_
```groovy
plugins {
    id 'uk.co.lukestevens.plugins.release-helper' version '0.1.0'
}
```

_gradle.properties_
```properties
version=0.1.0-SNAPSHOT
```

## Optional configuration
_build.gradle_
```groovy
releaseHelper {
    versionSuffix = '-SNAPSHOT' // Defaults to ''
    exportFile = new File('export.properties') // Defaults to $GITHUB_ENV
}
```

## Limitations
For version writing to work correctly, your version *must only* be specified in the `gradle.properties` file and *must be the only* line in that file.
This limitation is due for fix in the next version.