# javafx-fluent-theme

![](/docs/Demo.png)

This is a faithful recreation of Windows 11's _fluent theme_ (WinUI) for JavaFX. It is **directly based on WinUI's XAML resources**, and uses Windows' platform APIs to automatically react to the current system theme and accent colors. **All controls are tinted by the active accent color!**

In addition, you can [apply Windows 11's _Mica_ effect to your window](#enabling-windows-11s-mica-effect), or [override Windows' default window title bar](#removing-windows-default-title-bar) for complete client-side decorations.

<details><summary>Example gallery light theme</summary>

![](/docs/ControlsLight.png)
</details>

<details><summary>Example gallery dark theme</summary>

![](/docs/ControlsDark.png)
</details>

## TL;DR

* Download the JAR and `FluentLib.dll` (x64) from the [Releases](https://github.com/Eroica/javafx-fluent-theme/releases)
* Sub-class `FluentApp`
  * Initialize your scene in FluentApp's `onCreateStage` (instead of `start`)
  * DON'T call any `initStyle(...)`
  * For Mica effect, make sure your Scene has a transparent background
* Call `FluentApp.initialize()` as early as possible, e.g. in your `fun main()`

By default, this will apply the current system theme (light/dark), watch it for changes, and enable Mica and a client-side header bar for the application.

```kotlin
fun main() {
    FluentApp.initialize()
    launch(FluentDemo::class.java)
}

class FluentDemo : FluentApp() {
    override fun onCreateStage(primaryStage: Stage) {
        primaryStage.minWidth = 800.0
        primaryStage.minHeight = 600.0
        primaryStage.title = "JavaFX Fluent Demo"
        primaryStage.scene = Scene(StackPane(), Color.TRANSPARENT)
    }
}
```

See [Usage](#usage) for detailed explanations.

See [here for the list of styled controls](#table-of-javafxwinui-controls) (and which are still missing).

See [Development](#development) to learn how it works.

***

## Usage

As a first step, run `FluentApp.initialize()` as soon as possible, before calling any JavaFX code. For example, in this `Main.kt` file:

```kotlin
fun main() {
    FluentApp.initialize()
    launch(YourApp::class.java)
}
```

This takes care of loading the FluentLib DLL, and does some checks so that Windows 11's Mica effect works (see [Issues](#issues)).

For the application class, it's a little easier if you sub-class `FluentApp` (method 1), but there is also manual way (method 2):

### Sub-classing FluentApp (method 1)

`FluentApp` comes with two abstract methods: `onCreateStage` and `onStageCreated`. In general, whatever you do in `start()`, you should now do in `onCreateStage`, with some exceptions:

* Don't change the Stage style using `initStyle`.
* **You must set a title for the Stage;** this should be unique to the app because FluentLib uses the title to find the low-level Win32 window handle
* Don't call `primaryStage.show()`. FluentApp takes care of that.

Before `FluentApp` is instantiated, you can configure these static fields:

* `FluentApp.useMica: Boolean`: Whether to apply Windows 11's Mica effect (default: true)
* `FluentApp.useHeaderBar: Boolean`: Whether to use "client-side decorations" (default: true)
* `FluentApp.setTheme(ColorScheme?)`: Set a fixed theme (`ColorScheme.DARK`/`ColorScheme.LIGHT`) or `null` to use system theme (default: `null`)

A non-`null` value to `setTheme` will set the theme persistently, i.e. will not react to Windows changes anymore. Activate automatic changes again by calling `FluentApp.setTheme(null)` (see [Demo application](#demo-application)).

### Manual setup (method 2)

If you want to set up the theme manually without subclassing `FluentApp`, make sure to:

* Override `modena.css` as early as possible; you can use `fluent-light.css` and `fluent-dark.css` from this package's resources
* If you want to use Mica/other effects, load the DLL as early as possible: `System.loadLibrary("FluentLib")`
* If you want to use Mica/other effects, set your stage's style to `StageStyle.UNIFIED`
* **Set a unique title for the stage**
* Only use methods from FluentLib (wrapped by `lib/Windows.kt`) **after** `primaryStage.show()`

### Enabling Windows 11's Mica effect

Windows 11's "Mica" effect can be activated with this library's `Windows.setMicaFor(String, Boolean)` method (after `FluentLib.dll` is loaded). However, you need to make sure that:

* Your stage's style is `StageStyle.UNIFIED`
* In your JavaFX stage, there is no background color set wherever Mica should "shine through", e.g. any node should have `-fx-background-color: transparent`, and the `Scene` should be instantiated with `Scene(root, Color.TRANSPARENT)`
* Call `Windows.setMicaFor(String, Boolean)` providing your window title **after** the stage is `show()`n.
* You can enable or disable the effect using the same method.

#### Issues

While it's generally possible to get the Mica effect without using Microsoft's blessed ways, it seems it is a little **unreliable under JavaFX when using certain types of GPUs**. More info is available here: [Graphic issues on certain GPUs](https://github.com/mimoguz/custom_window/issues/2)

When you see a "glitched" Mica effect, try using software rendering first by setting this as early as possible (i.e. before calling JavaFX' `launch`):

`System.setProperty("prism.order", "sw")`

If Mica works with this, there is a good chance that you only need to enable a hidden flag **on non-AMD GPUs**. Remove the `prism.order` flag, and run this code as early as possible:

```kotlin
System.setProperty("prism.forceUploadingPainter", "true")
System.setProperty("javafx.animation.fullspeed", "true") // When on monitors >60Hz
```

`FluentApp.initialize()` takes care of that automatically, but my current check for AMD GPUs isn't very sophisticated. Feel free to raise any issues if e.g. the demo application looks "weird" on your setup (usually, a black background).

### Removing Windows' default title bar

Windows' default title bar normally shows a small icon and the window title. You can remove this title bar and merge the content area with the window controls to use this "unused" space. Many macOS and GTK applications use a similar design which arguably looks a little more modern.

The Win32 equivalent is called `DwmExtendFrameIntoTitleBar`. (There is actually code in JavaFX that uses this, but it doesn't seem to work reliably.) With this theme, you get a more reliable way to achieve this window styling. Follow this if you want to do it manually:

* The Stage's window style must be `StageStyle.UNIFIED`.
* **After** `primaryStage.show()`, call `Windows.setHeaderBarFor(String, Boolean)` providing your window's title to remove the title bar.
* The effect can be toggled back and forth using the same method.

Alternatively, take a look at subclassing `FluentApp`.

Depending on your scene background, the window controls at the top right might disappear. This is because the background is painted over these controls (although Windows will still react to click events there). To see window controls, make sure not to set any background or only use `transparent` where the window controls might be painted over.

Combined with the Mica effect from above (which requires a transparent background), the window controls will appear again, giving you a nice, Windows 11-styled application:

![](/docs/HeaderBarMica.png)

Here, the first node of my JavaFX scene is a custom `HeaderBar` node (which is basically an HBox) which just shows a button (the back arrow).

Without a title bar, you will lose the option to drag the window around with a mouse. Because of that, this library also provides a `DragPane` which you should put somewhere in your `HeaderBar` node. The nice thing about this is that it will capture Windows' native events, and e.g. trigger Windows' snap layouts when moving the window around.

An example header bar:

```fxml
<HeaderBar>
    <padding>
        <Insets top="4.0" right="8.0" bottom="4.0" left="8.0"/>
    </padding>
    <Button text="&#xe830;" styleClass="subtle, fluent, icon" disable="true"></Button>
    <DragPane HBox.hgrow="ALWAYS"/>
</HeaderBar>
```

## Development

The theme and example application are written in Kotlin and built with Gradle. `FluentLib.dll` is required to call Windows' internal functions. It is created by a Visual Studio project located in `FluentLib/`.

```
theme/      # CSS theme and custom JavaFX controls which can be used as a library
FluentLib/  # Windows DLL to get access to Mica/title bar replacement
demo/       # demo application that uses the theme and DLL
```

### How it works

The "base" fluent theme is referencing the system colors with placeholders (CSS variables), and at runtime, `FluentApp` creates an in-memory stylesheet of the base style and the colors from `Preferences`. The in-memory stylesheet invalidates itself when there is a change to `Preferences` (e.g. theme or accent color has changed), and dynamically creates a new stylesheet. This was actually taken from JavaFX' Modena example app.

The base theme itself is based on my previous, hand-written theme, but referencing the real XAML values from Microsoft's `microsoft-ui-xaml` repository. This makes javafx-fluent-theme _very_ true to the original.

### Non-goals

* Support on macOS or using JavaFX with GTK
* Compatibility with Windows 10 (Windows 11 replaces the previous _Segoe MDL2 Assets_ with _Segoe Fluent Icons_ which is used throughout this project, and which is not guaranteed to be available on Windows 10).

### Support

If you or a company require more widgets than currently available, fuller control, deeper integration with Windows APIs, or general JavaFX help, you can acquire a commercial license for _javafx-fluent-theme_. You can get in touch by sponsoring this project on GitHub.

***

# Demo application

![](/docs/Demo.png)

The `demo` application is roughly designed after "WinUI 3 Gallery". Take a look at the list of controls below to see what is missing.

I renamed "System Backdrops (Mica/Acrylic)" (found under "Styles") to "Window background", and "TitleBar" (under "Windowing") to "HeaderBar".

Additionally, `Spinner` is put under "Basic input" instead of "Text" (where WinUI puts "NumberBox").

Note:

* The back button is only showcasing what you can put in place of a title bar, and not working yet.
* There is no application icon yet.

## Building

The demo application requires `FluentLib.dll` in its working directory, so either build it from `FluentLib/`, or download a pre-built x64 DLL from the Releases, and put it under `demo/`.

***

# Appendix

## Design guidelines

`javafx-fluent-theme` enables grayscale antialiasing which is the default for Window applications using UWP or WinUI.

In my own designs, I use sentence case when labelling buttons and other controls. Additionally, my "primary button" is always located at the bottom right of a dialog (instead of being the first element).

## Table of JavaFX/WinUI controls

(Status: Done = styled & done; Missing = Not added yet; Skipped = not planning on working on it)

| JavaFX            | WinUI                          | Status  |
|-------------------|--------------------------------|---------|
| Button            | Button                         | Done    |
| CheckBox          | CheckBox                       | Done    |
| ChoiceBox         | ComboBox                       | Done    |
| ColorPicker       | ColorPicker                    | Missing |
| ComboBox          | ComboBox†                      | Done    |
| ContextMenu       | (several)                      | Done    |
| DatePicker        | CalendarDatePicker, DatePicker | Done    |
| HTMLEditor        | -                              | Skipped |
| Hyperlink         | HyperlinkButton                | Done    |
| ImageView         | Image                          | Done    |
| Label             | TextBlock                      | Done    |
| ListView          | ListView                       | Done    |
| MediaView         | MediaPlayerElement             | Skipped |
| MenuBar           | MenuBar                        | Done    |
| MenuButon         | DropDownButton                 | Done    |
| Pagination        | PipsPager                      | Missing |
| PasswordField     | PasswordBox                    | Done    |
| ProgressBar       | ProgressBar                    | Done    |
| ProgressIndicator | ProgressRing                   | Skipped |
| RadioButton       | RadioButton                    | Done    |
| ScrollBar         | ScrollBar                      | Done    |
| Separator         | (AppBarSeparator)              | Done    |
| Slider            | Slider                         | Done    |
| Spinner           | NumberBox                      | Done    |
| SplitMenuButton   | SplitButton                    | Missing |
| TableView         | DataGrid/DataTable             | Skipped |
| TextArea          | TextBox                        | Done    |
| TextField         | TextBox                        | Done    |
| ToggleButton      | ToggleButton                   | Done    |
| Tooltip           | ToolTip‡                       | Done    |
| TreeTableView     | DataGrid (I think)             | Skipped |
| TreeView          | TreeView                       | Done    |
| WebView           | WebView2                       | Skipped |

<sup>†</sup> Use `FluentComboBox` for WinUI-themed dropdownl ist.

<sup>‡</sup>Instantiate `FluentTooltip` for fluent animations (fade in/out).

Additionally, the following container styles are available or currently in preparation:

| JavaFX     | WinUI         | Status  | Class(es)                         |
|------------|---------------|---------|-----------------------------------|
| Dialog     | ContentDialog | Done    | FluentDialog, FluentStageDialog † |
| SplitPane  | SplitView     | Missing | -                                 |
| TabPane    | TabView       | Done    | TabPane                           |
| TitledPane | Expander      | Done    | TitledPane                        |
| ScrollPane | ScrollView    | Done    | ScrollPane                        |
| ToolBar    | CommandBar    | Missing | -                                 |
| -          | InfoBar       | Done    | InfoBar                           |

<sup>†</sup> Both dialog classes add a "smoke overlay" to the parent window. This works out-of-the-box when sub-classing `FluentApp`. In other cases, make sure the parent window has an outermost StackPane. (And calling a dialog from a dialog isn't a good idea ...)

## Gallery

Here are my JavaFX applications that I styled with this theme:

![](/docs/ExampleApp1.png)

![](/docs/ExampleApp2.png)

***

# License

This project is published under the zlib license (see `LICENSE` file in this repository).

```
Copyright (C) 2023-2026 Eroica

This software is provided 'as-is', without any express or implied
warranty.  In no event will the authors be held liable for any damages
arising from the use of this software.

Permission is granted to anyone to use this software for any purpose,
including commercial applications, and to alter it and redistribute it
freely, subject to the following restrictions:

1. The origin of this software must not be misrepresented; you must not
   claim that you wrote the original software. If you use this software
   in a product, an acknowledgment in the product documentation would be
   appreciated but is not required.
2. Altered source versions must be plainly marked as such, and must not be
   misrepresented as being the original software.
3. This notice may not be removed or altered from any source distribution.
```

![](/docs/Developed-By-a-Human-Not-By-AI-Badge-white.png)
