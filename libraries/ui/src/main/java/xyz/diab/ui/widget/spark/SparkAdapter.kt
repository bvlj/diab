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

import android.database.DataSetObservable
import android.database.DataSetObserver
import android.graphics.RectF
import kotlin.math.max
import kotlin.math.min

/**
 * Based on RobinHood [Spark](https://github.com/robinhood/spark)
 *
 * A simple adapter class - evenly distributes your points along the x axis,
 * does not draw a base line.
 */
abstract class SparkAdapter<T> {
    private val observable = DataSetObservable()

    /**
     * @return the number of points to be drawn
     */
    abstract fun getCount(): Int

    /**
     * @return the object at the given index
     */
    abstract fun getItem(index: Int): T

    /**
     * @return the float representation of the X value of the point
     *         at the given index
     */
    abstract fun getX(index: Int): Float

    /**
     * @return the float representation of the Y value of the point
     *         at the given index
     */
    abstract fun getY(index: Int): Float

    /**
     * @return true if you wish to draw a "base line" - a horizontal line across the graph used
     * to compare the rest of the graph's points against.
     */
    open fun hasBaseLine(): Boolean {
        return false
    }

    /**
     * @return the float representation of the Y value of the desired baseLine.
     */
    open fun getBaseLine(): Float {
        return 0f
    }

    /**
     * Gets the float representation of the boundaries of the entire dataSet. By default, this will
     * be the min and max of the actual data points in the adapter. This can be overridden for
     * custom behavior. When overriding, make sure to set RectF's values such that:
     *
     * - left = the minimum X value
     * - top = the minimum Y value
     * - right = the maximum X value
     * - bottom = the maximum Y value
     *
     * @return a RectF of the bounds desired around this adapter's data.
     */
    open fun getDataBounds(): RectF {
        val count = getCount() - 1
        val hasBaseLine = hasBaseLine()

        var minX = Float.MAX_VALUE
        var maxX = -Float.MAX_VALUE
        var minY = if (hasBaseLine) getBaseLine() else Float.MAX_VALUE
        var maxY = if (hasBaseLine) minY else -Float.MAX_VALUE

        for (i in 0..count) {
            val x = getX(i)
            minX = min(minX, x)
            maxX = max(maxX, x)

            val y = getY(i)
            minY = min(minY, y)
            maxY = max(maxY, y)
        }

        return RectF(minX, minY, maxX, maxY)
    }

    /**
     * Notifies the attached observers that the underlying data has been changed and any View
     * reflecting the data set should refresh itself.
     */
    fun notifyDataSetChanged() {
        observable.notifyChanged()
    }

    /**
     * Notifies the attached observers that the underlying data is no longer valid or available.
     * Once invoked this adapter is no longer valid and should not report further data set
     * changes.
     */
    fun notifyDataSetInvalidated() {
        observable.notifyInvalidated()
    }

    /**
     * Register a [DataSetObserver] to listen for updates to this adapter's data.
     *
     * @param observer the observer to register
     */
    fun registerDataSetObserver(observer: DataSetObserver?) {
        observable.registerObserver(observer)
    }

    /**
     * Unregister a [DataSetObserver] from updates to this adapter's data.
     *
     * @param observer the observer to unregister
     */
    fun unregisterDataSetObserver(observer: DataSetObserver?) {
        observable.unregisterObserver(observer)
    }
}
