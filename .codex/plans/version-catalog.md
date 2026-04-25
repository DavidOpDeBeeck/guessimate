# Version Catalog

## Feature overview

Introduced a Gradle version catalog so dependency and plugin versions are managed from a single TOML file instead of being hardcoded across build scripts.

## Status

Completed

## Files created/modified with paths

- `gradle/libs.versions.toml`
- `build.gradle.kts`
- `guessimate-api/build.gradle.kts`
- `guessimate-lobby/api/build.gradle.kts`
- `guessimate-lobby/application/build.gradle.kts`
- `guessimate-session/api/build.gradle.kts`
- `guessimate-session/application/build.gradle.kts`

## Implementation details

- Added shared version entries for Spring Boot, SMD, JUnit, ArchUnit, Testcontainers, Guava, and Commons Collections.
- Added library aliases for Spring Boot starters, infrastructure libraries, test libraries, and SMD dependencies.
- Added a plugin alias for `org.springframework.boot` and switched `guessimate-api` to use it.
- Removed the root `constraints`-based version pinning and replaced it with direct catalog-backed dependency declarations.
- Kept the Spring Boot BOM in the root build so managed dependencies can remain versionless where appropriate.
- Used `VersionCatalogsExtension` in the root `subprojects {}` block because the generated `libs` accessor is not directly available there at runtime.

## UI behavior

No UI changes.

## Data flow

Build scripts now resolve versioned dependencies and plugins through the `libs` version catalog:

1. Gradle loads `gradle/libs.versions.toml`.
2. Build scripts reference catalog aliases via `libs.*`.
3. The root build applies the Spring Boot BOM from the catalog for managed dependency versions.

## Testing notes

- Verified with `.\gradlew.bat compileJava --no-daemon`
