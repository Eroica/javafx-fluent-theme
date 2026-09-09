package earth.groundctrl.fluent.views

import earth.groundctrl.fluent.ui.FluentInterpolator
import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.scene.Cursor
import javafx.scene.control.TitledPane
import javafx.scene.control.skin.TitledPaneSkin
import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import javafx.util.Duration

/* Expander_themeresources.xaml: ExpanderChevronDownGlyph */
private val CHEVRON_GLYPH = 0xE70D.toChar().toString()

/* Somewhere in Expander.xaml, best effort pick for now */
private val EXPAND_DURATION = Duration.millis(333.0)
private val COLLAPSE_DURATION = Duration.millis(167.0)

/* ExpanderChevronButtonSize */
private const val CHEVRON_SIZE = 32.0
/* ExpanderChevronMargin ("20,0,8,0") */
private const val CHEVRON_MARGIN_RIGHT = 8.0

class FluentTitledPaneSkin(titledPane: TitledPane) : TitledPaneSkin(titledPane) {
    private val glyph = Text(CHEVRON_GLYPH).apply { styleClass.add("glyph") }
    private val chevronButton = StackPane(glyph).apply {
        styleClass.add("expander-chevron-button")
        isManaged = false
        cursor = Cursor.HAND
        setOnMouseClicked { if (titledPane.isCollapsible) titledPane.isExpanded = !titledPane.isExpanded }
    }
    private val collapseInterpolator: Interpolator = Interpolator.ofSpline(1.0, 1.0, 0.0, 1.0)
    private var rotation: Timeline? = null

    init {
        glyph.rotate = if (titledPane.isExpanded) 180.0 else 0.0

        children.add(chevronButton)

        titledPane.expandedProperty().addListener { _, _, expanded ->
            rotation?.stop()
            val duration = if (expanded) EXPAND_DURATION else COLLAPSE_DURATION
            val interpolator = if (expanded) FluentInterpolator else collapseInterpolator
            rotation = Timeline(
                KeyFrame(duration, KeyValue(glyph.rotateProperty(), if (expanded) 180.0 else 0.0, interpolator))
            ).apply { play() }
        }
    }

    override fun computeMinHeight(width: Double, topInset: Double, rightInset: Double, bottomInset: Double, leftInset: Double): Double =
        super.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset) + headerHeightShortfall(width)

    override fun computePrefHeight(width: Double, topInset: Double, rightInset: Double, bottomInset: Double, leftInset: Double): Double =
        super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset) + headerHeightShortfall(width)

    override fun layoutChildren(x: Double, y: Double, w: Double, h: Double) {
        super.layoutChildren(x, y, w, h)
        val title = skinnable.lookup(".title")
        if (title != null) {
            val shortfall = headerHeightShortfall(w)
            if (shortfall > 0.0) {
                title.resize(w, title.layoutBounds.height + shortfall)
                val content = skinnable.lookup(".content")
                if (content != null) {
                    content.relocate(content.layoutX, content.layoutY + shortfall)
                    content.resize(content.layoutBounds.width, (content.layoutBounds.height - shortfall).coerceAtLeast(0.0))
                }
            }
        }

        chevronButton.resize(CHEVRON_SIZE, CHEVRON_SIZE)
        chevronButton.layoutX = x + w - CHEVRON_MARGIN_RIGHT - CHEVRON_SIZE
        val headerY = title?.layoutY ?: y
        val headerHeight = title?.layoutBounds?.height ?: CHEVRON_SIZE
        chevronButton.layoutY = headerY + (headerHeight - CHEVRON_SIZE) / 2.0
    }

    private fun headerHeightShortfall(width: Double): Double {
        val title = skinnable.lookup(".title") ?: return 0.0
        return (title.minHeight(width) - title.prefHeight(width)).coerceAtLeast(0.0)
    }
}
