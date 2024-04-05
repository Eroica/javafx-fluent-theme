# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [v2023.04] - 2023-04-05

### Removed

- `FluentApp` constructor does not take `initialTitle` parameter anywhere. Instead, make sure to set some kind of title in `onCreateStage` if you use any `useMica:` or `useHeaderBar:` parameter.

### Changed

- Source files have been organized into a package hierarchy to accommodate Java projects. The import prefix is `earth.groundctrl.fluent`.

- The Mica workaround for non-AMD GPUs is no longer automatically tested in `FluentApp.onCreateStage`. You can use `Windows.isAmdGpu` to do this check manually. The idea behind this change is to be able to set the required system properties in Java's `main` function, i.e. before the JavaFX runtime is initialized.

### Fixed

- Fixed "Determinate ProgressBar" sample in demo app.

## [v2023.09] - 2023-09-15

First published version.
