# Maven Shared Libraries

This repository contains independently versioned and deployable
Java libraries intended for reuse across projects.

The libraries in this repository are infrastructure/shared libraries.
Business-specific services and application code are intentionally
kept in separate customer specialized repositories.

## Libraries

| Library | Purpose |
|---------|---------|
| lib-foo | Reference implementation used to validate repository conventions |
| ...     | ... |

## Library Conventions

Each library:
- is an independent Maven project with its own `pom.xml` file.
- has its own MAVEN wrapper. 
- can be built independently.
- is independently versioned
- is independently verified by CI
- is independently published to GitHub Packages
- uses its own release tag

## Coordinates

Shared libraries use:

`com.devon90.lib:<library-name>`

## Release

Libraries are released independently using tags:

`<library-name>-v<version>`  e.g `lib-foo-v1.0.0`