# Contributing

## Places to change together

Must edit the others if they change any of the following:

### Release new version

| File                                                              | Place(s)                        |
|-------------------------------------------------------------------|---------------------------------|
| [README.md](README.md#installation)                               | Installation                    |
| [build.gradle.kts](build.gradle.kts)                              | `version`                       |
| [`examples/jvm`: build.gradle.kts](examples/jvm/build.gradle.kts) | `version` <br /> `dependencies` |
| [`examples/e2e`: build.gradle.kts](examples/e2e/build.gradle.kts) | `version` <br /> `dependencies` |

### Bump dependency

#### Kotlin

Use the same version for API and all examples.
And write that version in the [README.md](README.md) badge.

| File                                                              | Place(s)  |
|-------------------------------------------------------------------|-----------|
| [README.md](README.md)                                            | Badge     |
| [build.gradle.kts](build.gradle.kts)                              | `plugins` |
| [`examples/jvm`: build.gradle.kts](examples/jvm/build.gradle.kts) | `plugins` |
| [`examples/e2e`: build.gradle.kts](examples/e2e/build.gradle.kts) | `plugins` |

#### Ktor

Use the same version for all dependencies related to Ktor.

| File                                                              | Place(s)       |
|-------------------------------------------------------------------|----------------|
| [build.gradle.kts](build.gradle.kts)                              | `dependencies` |
| [`examples/jvm`: build.gradle.kts](examples/jvm/build.gradle.kts) | `dependencies` |
| [`examples/e2e`: build.gradle.kts](examples/e2e/build.gradle.kts) | `dependencies` |

#### Dokka

Use the same version for dokka and dokkaPlugin.

| File                                 | Place(s)                       |
|--------------------------------------|--------------------------------|
| [build.gradle.kts](build.gradle.kts) | `plugins` <br /> `dokkaPlugin` |

### Add new example

Adding a new example, add it to the examples list.
Also, add to [Places to change together](#places-to-change-together) as the examples should follow the latest version.

| File                                                         | Place(s)                  |
|--------------------------------------------------------------|---------------------------|
| [CONTRIBUTING.md](CONTRIBUTING.md#places-to-change-together) | Places to change together |
| [`examples`: README.md](examples/README.md#examples)         | Examples                  |
