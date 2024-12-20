# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [v2024.12b] - 2024-12-20 (WIP)

### Added

- Added `buildnumber` to Windows API functions
- Added `onStageCreated` callback for `FluentApp`. Use this for anything that should run after FluentApp's built-in `stage.show()` call.
- Added fluent styling for `TreeView`, `ListView`, and `TableView`
  - Added optional `FluentListView` styling with fluent animations
- Added a custom control for WinUI's InfoBar: `InfoBar`
- Added fluent styling for `Dialog`
- Fixed some style issues for `Spinner`
- Updated `ComboBox`/`ChoiceBox` sample
  - Added optional `FluentComboBox` styling with fluent animations

### Changed

- Changed the `FluentApp` constructor to zero arguments. Initial Mica and HeaderBar flags are set via static fields.
- Updated and built with JDK 22, JavaFX 23
- GPU check doesn't use `wmic` anymore (because it's been deprecated?)

### Removed

- Removed the current TitledPane skin because it looks a little awkward. Will be replaced by a proper `Expander` control in the future.

### Fixed

- Fixed exception in Slider skin
- Fixed hover styling of CheckBox (if it was also focused)
- Updated styling of RadioButtons (WinUI's have a darker background)
- Fixed several code examples in the demo application
- Fixed bug in GPU check that prevented the demo from starting

## [v2024.05] - 2024-04-05

### Added

- Added `FluentApp.initialize()` which takes care of setting up the necessary environment prior to showing a JavaFX stage.

## [v2024.04] - 2024-04-05

### Removed

- `FluentApp` constructor does not take `initialTitle` parameter anywhere. Instead, make sure to set some kind of title in `onCreateStage` if you use any `useMica:` or `useHeaderBar:` parameter.

### Changed

- Source files have been organized into a package hierarchy to accommodate Java projects. The import prefix is `earth.groundctrl.fluent`.

- The Mica workaround for non-AMD GPUs is no longer automatically tested in `FluentApp.onCreateStage`. You can use `Windows.isAmdGpu` to do this check manually. The idea behind this change is to be able to set the required system properties in Java's `main` function, i.e. before the JavaFX runtime is initialized.

### Fixed

- Fixed "Determinate ProgressBar" sample in demo app.

## [v2023.09] - 2023-09-15

First published version.
