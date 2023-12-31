# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Removed

- `FluentApp` constructor does not take `initialTitle` parameter anywhere. Instead, make sure to set some kind of title in `onCreateStage` if you use any `useMica:` or `useHeaderBar:` parameter.

### Changed

- The Mica workaround for non-AMD GPUs is no longer automatically tested in `FluentApp.onCreateStage`. You can use `Windows.isAmdGpu` to do this check manually. The idea behind this change is to be able to set the required system properties in Java's `main` function, i.e. before the JavaFX runtime is initialized.

## [v2023.09] - 2023-09-15

First published version.
