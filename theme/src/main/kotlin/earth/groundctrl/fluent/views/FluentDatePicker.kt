package earth.groundctrl.fluent.views

import javafx.scene.control.DatePicker
import javafx.scene.control.skin.DatePickerSkin
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.scene.text.Text

/* CalendarDatePicker_themeresources.xaml: CalendarGlyph */
private val CALENDAR_GLYPH = 0xE787.toChar().toString()
private val PREVIOUS_GLYPH = 0xEDDB.toChar().toString()
private val NEXT_GLYPH = 0xEDDC.toChar().toString()

class FluentDatePickerSkin(control: DatePicker) : DatePickerSkin(control) {
    init {
        (skinnable.lookup(".arrow-button") as? StackPane)?.children?.add(glyph(CALENDAR_GLYPH))
        skinnable.addEventFilter(MouseEvent.MOUSE_PRESSED) { skinnable.show() }

        var isGlyphsInjected = false
        skinnable.showingProperty().addListener { _, _, showing ->
            if (showing && !isGlyphsInjected) {
                isGlyphsInjected = true
                val calendarContent = getPopupContent()
                calendarContent.lookupAll(".left-arrow").forEach { (it as? StackPane)?.children?.add(glyph(PREVIOUS_GLYPH)) }
                calendarContent.lookupAll(".right-arrow").forEach { (it as? StackPane)?.children?.add(glyph(NEXT_GLYPH)) }
            }
        }
    }

    private fun glyph(character: String) = Text(character).apply { styleClass.add("glyph") }
}
