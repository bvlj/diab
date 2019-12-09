/*
 * Copyright (c) 2020 Bevilacqua Joey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.diab.ui.widget.spark

import android.graphics.RectF

/**
 * Based on RobinHood [Spark](https://github.com/robinhood/spark)
 *
 * Helper class for handling scaling logic.
 */
internal class ScaleHelper(
    adapter: SparkAdapter<*>,
    contentRect: RectF,
    lineWidth: Float,
    fill: Boolean
) {
    private val width: Float
    private val height: Float

    private val size: Int

    private val xScale: Float
    private val yScale: Float
    private val xTranslation: Float
    private val yTranslation: Float

    init {
        val leftPadding = contentRect.left
        val topPadding = contentRect.top

        // subtract lineWidth to offset for 1/2 of the line bleeding out of the content box on
        // either side of the view
        val lineWidthOffset = if (fill) 0f else lineWidth
        width = contentRect.width() - lineWidthOffset
        height = contentRect.height() - lineWidthOffset

        size = adapter.getCount()

        // get data bounds from adapter
        val bounds = adapter.getDataBounds().apply {
            inset(if (width() == 0f) -1f else 0f, if (height() == 0f) -1f else 0f)
        }

        val minX = bounds.left
        val maxX = bounds.right
        val minY = bounds.top
        val maxY = bounds.bottom

        // xScale will compress or expand the min and max x values to be just inside the view
        xScale = width / (maxX - minX)
        // xTranslation will move the x points back between 0 - width
        xTranslation = leftPadding - (minX * xScale) + (lineWidthOffset / 2f)
        // yScale will compress or expand the min and max y values to be just inside the view
        yScale = height / (maxY - minY)
        // yTranslation will move the y points back between 0 - height
        yTranslation = minY * yScale + topPadding + (lineWidthOffset / 2f)
    }

    /**
     * Given the 'raw' X value, scale it to fit within our view.
     */
    fun getX(rawX: Float) = rawX * xScale + xTranslation

    /**
     * Given the 'raw' Y value, scale it to fit within our view. This method also 'flips' the
     * value to be ready for drawing.
     */
    fun getY(rawY: Float) = height - (rawY * yScale) + yTranslation
}
