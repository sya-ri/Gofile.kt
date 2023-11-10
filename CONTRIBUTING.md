# Contributing

## Places to change together

Must edit the others if they change any of the following:

### Release new version

| File                                                                    | Place(s)                        |
|-------------------------------------------------------------------------|---------------------------------|
| [README.md](README.md#installation)                                     | Installation                    |
| [build.gradle.kts](build.gradle.kts)                                    | `version`                       |
| [`examples/jvm`: build.gradle.kts](examples/jvm/build.gradle.kts)       | `version` <br /> `dependencies` |
| [`examples/native`: build.gradle.kts](examples/native/build.gradle.kts) | `version` <br /> `dependencies` |
| [`examples/e2e`: build.gradle.kts](examples/e2e/build.gradle.kts)       | `version` <br /> `dependencies` |

### Add new example

Adding a new example, add it to the examples list.
Also, add to [Places to change together](#places-to-change-together) as the examples should follow the latest version.

| File                                                         | Place(s)                  |
|--------------------------------------------------------------|---------------------------|
| [CONTRIBUTING.md](CONTRIBUTING.md#places-to-change-together) | Places to change together |
| [`examples`: README.md](examples/README.md#examples)         | Examples                  |
