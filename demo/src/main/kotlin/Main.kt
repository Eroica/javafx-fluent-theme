/* Copyright (C) 2023 Eroica
 *
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

import javafx.application.Application.launch
import javafx.beans.InvalidationListener
import javafx.scene.Scene
import javafx.scene.paint.Color
import javafx.stage.Stage

fun main() {
    launch(FluentDemo::class.java)
}

class FluentDemo : FluentApp("JavaFX Fluent UI Gallery", true, true) {
    override fun onCreateStage(primaryStage: Stage) {
        primaryStage.minWidth = 800.0
        primaryStage.minHeight = 600.0
        primaryStage.scene = Scene(MainActivity(primaryStage.title, hostServices).apply {
        }, Color.TRANSPARENT).apply {
            stylesheets.add("style.css")
        }
        primaryStage.focusedProperty().addListener(InvalidationListener {
            if (primaryStage.isFocused) {
                primaryStage.scene.root.styleClass.remove("no-focus")
            } else {
                primaryStage.scene.root.styleClass.add("no-focus")
            }
        })
    }
}
