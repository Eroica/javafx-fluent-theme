package earth.groundctrl.fluent.views

import javafx.geometry.Orientation
import javafx.scene.layout.Pane
import javafx.scene.text.Text

class InfoBarMessagePanel : Pane() {
    /* InfoBarMessageHorizontalOrientationMargin.left */
    var horizontalGap = 12.0

    /* InfoBarTitleHorizontalOrientationMargin.top / InfoBarMessageHorizontalOrientationMargin.top */
    var horizontalRowTop = 14.0

    /* InfoBarMessageVerticalOrientationMargin.top */
    var verticalGap = 4.0

    /* InfoBarPanelVerticalOrientationPadding.top/bottom */
    var verticalPaddingTop = 14.0
    var verticalPaddingBottom = 18.0

    /* InfoBarMinHeight */
    var minHeightThreshold = 48.0

    override fun getContentBias(): Orientation = Orientation.HORIZONTAL

    private fun title(): Text? = children.getOrNull(0) as? Text
    private fun message(): Text? = children.getOrNull(1) as? Text

    private fun setWrapWidth(text: Text, width: Double) {
        text.wrappingWidth = width.coerceAtLeast(0.0)
    }

    private fun heightForWidth(width: Double): Double {
        val title = title() ?: return 0.0
        val message = message() ?: return 0.0
        if (width <= 0.0) return 0.0

        setWrapWidth(title, 0.0)
        val titleW = title.prefWidth(-1.0)
        val titleH = title.prefHeight(titleW)

        setWrapWidth(message, 0.0)
        val messageNaturalW = message.prefWidth(-1.0)
        val fitsHorizontally = (titleW + horizontalGap + messageNaturalW) <= width

        return if (fitsHorizontally) {
            val heightOfTallestInHorizontal = horizontalRowTop + maxOf(titleH, message.prefHeight(messageNaturalW))
            if (heightOfTallestInHorizontal <= minHeightThreshold) {
                heightOfTallestInHorizontal
            } else {
                verticalPaddingTop + titleH + verticalGap + run {
                    setWrapWidth(message, width)
                    message.prefHeight(width)
                } + verticalPaddingBottom
            }
        } else {
            setWrapWidth(message, width)
            verticalPaddingTop + titleH + verticalGap + message.prefHeight(width) + verticalPaddingBottom
        }
    }

    override fun computePrefHeight(width: Double): Double = heightForWidth(if (width > 0.0) width else this.width)
    override fun computeMinHeight(width: Double): Double = computePrefHeight(width)

    override fun layoutChildren() {
        val title = title() ?: return
        val message = message() ?: return

        val left = snappedLeftInset()
        val top = snappedTopInset()
        val width = (this.width - left - snappedRightInset()).coerceAtLeast(0.0)

        setWrapWidth(title, 0.0)
        val titleW = title.prefWidth(-1.0)
        val titleH = title.prefHeight(titleW)

        setWrapWidth(message, 0.0)
        val messageNaturalW = message.prefWidth(-1.0)
        val fitsHorizontally = (titleW + horizontalGap + messageNaturalW) <= width

        val stackedInstead = fitsHorizontally &&
            (horizontalRowTop + maxOf(titleH, message.prefHeight(messageNaturalW))) > minHeightThreshold

        if (fitsHorizontally && !stackedInstead) {
            title.resizeRelocate(left, top + horizontalRowTop, titleW, titleH)
            val messageX = left + titleW + horizontalGap
            val messageW = (width - titleW - horizontalGap).coerceAtLeast(0.0)
            setWrapWidth(message, messageW)
            message.resizeRelocate(messageX, top + horizontalRowTop, messageW, message.prefHeight(messageW))
        } else {
            title.resizeRelocate(left, top + verticalPaddingTop, titleW, titleH)
            setWrapWidth(message, width)
            val messageY = top + verticalPaddingTop + titleH + verticalGap
            val messageH = message.prefHeight(width)
            message.resizeRelocate(left, messageY, width, messageH)
        }
    }
}
