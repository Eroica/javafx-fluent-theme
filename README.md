# javafx-fluent-theme

![](/docs/Demo.png)

This is a custom theme for JavaFX to make your application look like a Windows 11 (WinUI) program. It replaces `modena.css` with styles that follow Microsoft's _Fluent design_ language. In addition, you can [apply Windows 11's _Mica_ effect to your window](#enabling-windows-11s-mica-effect), or [override Windows' default window title bar](#removing-windows-default-title-bar) for complete client-side decorations.

Take a look at the [example application](#demo-application), or see here [how to achieve Windows' built-in effects](#usage).

See [here for the list of styled controls](#table-of-javafxwinui-controls), and to see which is still missing.

![](/docs/Gallery.png)

***

The theme and example application are written in Kotlin and built with Gradle. `FluentLib.dll` is required to call Windows' internal functions. It is created by a Visual Studio project located in `FluentLib/`.

```
theme/      # CSS theme and custom JavaFX controls which can be used as a library
FluentLib/  # Windows DLL to get access to Mica/title bar replacement
demo/       # demo application that uses the theme and DLL
```

The theme (currently, only a light theme is available) is built by SASS with files in `theme/src/main/css`.

## Usage

If you import this project as a dependency, you get access to the overridden styles as well as some Win32 functions to alter the look of the application window. Alternatively, you can just copy the CSS file into your project if you just want to change the look of the controls. However, some controls use custom skins to e.g. enable animations. See the [list of controls here](#table-of-javafxwinui-controls).

The library contains an abstract `FluentApp` class that sets up the custom theme and any other effects (see below) for you. It only has a single abstract method (`onCreateStage(Stage)`) which is executed between JavaFX' `start(Stage)` and a built-in `primaryStage.show()`. This is important because overriding the windows' look can only happen after it's already visible, so use `onCreateStage` for the usual setup like setting up the root scene.

If you want to set up the theme manually without subclassing `FluentApp`, make sure to:

* Override `modena.css` as early as possible with `setUserAgentStylesheet("fluent-light.css")`
* If you want to use Mica/other effects, load the DLL as early as possible: `System.loadLibrary("FluentLib")`
* If you want to use Mica/other effects, set your stage's style to `StageStyle.UNIFIED`
* Only use methods from the FluentLib DLL **after** `primaryStage.show()`

### Enabling Windows 11's Mica effect

Windows 11's "Mica" effect can be activated with this library's `Windows.setMicaFor(String, Boolean)` method (coming from `FluentLib.dll`). However, you need to make sure that:

* Your stage's style is `StageStyle.UNIFIED`
* There is no background set wherever Mica should "shine through", e.g. any node should have `-fx-background-color: transparent`, and the `Scene` should be instantiated with `Scene(root, Color.TRANSPARENT)`
* Call `Windows.setMicaFor(String, Boolean)` providing your window title **after** the stage is `show()`n.
* You can enable or disable the effect using the same method.

#### Issues

While it's generally possible to get the Mica effect without using Microsoft's blessed ways (e.g. when using WPF), it seems it is a little unreliable under JavaFX **when using certain types of GPUs**. More info is available here: [Graphic issues on certain GPUs](https://github.com/mimoguz/custom_window/issues/2)

When you see a "glitched" Mica effect, try using software rendering first by setting:

`System.setProperty("prism.order", "sw")`

If Mica works with this, there is a good chance that you only need to enable a hidden flag **on non-AMD GPUs**. Remove the `prism.order` flag, and run this code as early as possible:

```kotlin
System.setProperty("prism.forceUploadingPainter", "true")
System.setProperty("javafx.animation.fullspeed", "true") // When on monitors >60Hz
```

You can use `Windows.isAmdGpu()` for a simple check for AMD GPUs on the system, but this check isn't very sophisticated.

### Removing Windows' default title bar

Windows' default title bar normally shows a small icon and the window title. You can remove this title bar and merge the content area with the window controls to use this "unused" space. Many macOS and GTK applications use a similar design which arguably looks a little more modern.

The Win32 equivalent is called `DwmExtendFrameIntoTitleBar`. There is actually code in JavaFX that uses this, but it doesn't seem to work reliably. With `javafx-fluent-theme`, you get another direct way to achieve this Window styling. Follow this:

* The Stage's window style must be `StageStyle.UNIFIED`.
* **After** `primaryStage.show()`, call `Windows.setHeaderBarFor(String, Boolean)` providing your window's title to remove the title bar.
* The effect can be toggled back and forth using the same method.

Depending on your scene background, the window controls at the top right might disappear. This is because the background is painted over these controls (although Windows will still react to click events there). To see the window controls, make sure not to set any background (e.g. with CSS) or only use `transparent` where the window controls might be painted over.

Combined with the Mica effect from above (which requires a transparent background), the window controls will appear again, giving you a nice, Windows 11-styled application:

![](/docs/HeaderBarMica.png)

Here, the first node of my JavaFX scene is a custom `HeaderBar` node (sub-classing HBox) which just shows a button (the back arrow).

Without a title bar, you will lose the option to drag the window around with a mouse. While it's possible to have the window draggable by using JavaFX events, this library also provides a `DragPane` which you should put somewhere in your `HeaderBar` node. The nice thing about this is that it will capture Windows' native events, and e.g. trigger Windows' snap layouts when moving the window around.

An example header bar:

```fxml
<HeaderBar>
    <padding>
        <Insets top="4.0" right="8.0" bottom="4.0" left="8.0"/>
    </padding>
    <Button text="&#xe830;" styleClass="borderless-button, back-button" disable="true"></Button>
    <DragPane HBox.hgrow="ALWAYS"/>
</HeaderBar>
```

As with the Mica example above, you can subclass `FluentApp` instead and pass `useHeaderBar:` to its constructor to have this done at startup.

---

# Demo application

![](/docs/Demo.png)

The `demo` application is roughly designed after "WinUI 3 Gallery". So far I mostly added the _controls_ as opposed to containers like `StackPanel`/`StackPane`. Take a look at the list of controls below to see what is missing.

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

`javafx-fluent-theme` will set grayscale antialiasing which is the default for Window applications using UWP (in the past) or WinUI.

I mostly tried to follow the designs found in Microsoft's gallery application. When I first started my designs, most of them came from "WinUI 2 Gallery" and only later "WinUI 3 Gallery" caught up with Microsoft's latest designs, so there might still be some subtle differences between Microsoft's most current styles.

In my own designs, I try not to use any title case when labelling buttons or other controls, but only capitalize the first letter.

### Non-goals

* Support on macOS or using JavaFX with GTK
* Compatibility with Windows 10 (Windows 11 replaces the previous _Segoe MDL2 Assets_ with _Segoe Fluent Icons_ which is used throughout this project, and which is not guaranteed to be available on Windows 10).


## Table of JavaFX/WinUI controls

(Status: Done = styled & done; WIP = Added but not everything styled yet; Missing = Not added yet; Skipped = not planning on working on it)

| JavaFX            | WinUI                          | Status  | CSS only?† |
|-------------------|--------------------------------|---------|------------|
| Button            | Button                         | Done    | YES        |
| CheckBox          | CheckBox                       | Done    | YES        |
| ChoiceBox         | ComboBox                       | Done    | YES        |
| ColorPicker       | ColorPicker                    | Missing | -          |
| ComboBox          | ComboBox                       | Done    | YES        |
| ContextMenu       | (several)                      | Done    | NO‡        |
| DatePicker        | CalendarDatePicker, DatePicker | Done    | YES        |
| HTMLEditor        | -                              | Skipped | -          |
| Hyperlink         | HyperlinkButton                | Done    | YES        |
| ImageView         | Image                          | Done    | YES        |
| Label             | TextBlock                      | Done    | YES        |
| ListView          | ListView                       | Missing | -          |
| MediaView         | MediaPlayerElement             | Skipped | -          |
| MenuBar           | MenuBar                        | WIP     | YES        |
| MenuButon         | DropDownButton                 | Done    | YES        |
| Pagination        | -                              | Skipped | -          |
| PasswordField     | PasswordBox                    | Done    | YES        |
| ProgressBar       | ProgressBar                    | Done    | YES        |
| ProgressIndicator | ProgressRing                   | Skipped | -          |
| RadioButton       | RadioButton                    | Done    | YES        |
| ScrollBar         | ScrollBar                      | Done    | YES        |
| Separator         | (AppBarSeparator)              | Done    | YES        |
| Slider            | Slider                         | Done    | NO         |
| Spinner           | NumberBox                      | Done    | NO         |
| SplitMenuButton   | SplitButton                    | Missing | -          |
| TableView         | DataGrid                       | Missing | -          |
| TextArea          | TextBox                        | Done    | YES        |
| TextField         | TextBox                        | Done    | YES        |
| ToggleButton      | ToggleButton                   | Done    | YES        |
| Tooltip           | ToolTip                        | Done    | NO‡        |
| TreeTableView     | DataGrid (I think)             | Skipped | -          |
| TreeView          | TreeView                       | WIP     | -          |
| WebView           | WebView2                       | Skipped | -          |

<sup>†</sup>This means that no custom `Skin` is used to add any custom behavior. You could simply copy-paste the CSS attributes to get the design.

<sup>‡</sup>The control can be styled only with CSS, but you instantiate `Fluent[Name]` if you want to have "fluent animations".

Additionally, the following containers/panes are available or currently in preparation:

| JavaFX     | WinUI         | Status  | CSS only? |
|------------|---------------|---------|-----------|
| Dialog     | ContentDialog | Missing | -         |
| SplitPane  | SplitView     | Missing | -         |
| TabPane    | TabView       | Missing | -         |
| TitledPane | Expander      | Done    | YES       |
| ToolBar    | CommandBar    | Missing | -         |


## Gallery

Here are my JavaFX applications that I styled with this theme:

![](/docs/ExampleApp1.png)

![](/docs/ExampleApp2.png)

---

This project is published under the zlib license (see `LICENSE` file in this repository).

```
Copyright (C) 2023-2024 Eroica

This software is provided 'as-is', without any express or implied
warranty. In no event will the authors be held liable for any damages
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
