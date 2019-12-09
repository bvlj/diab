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

import android.animation.Animator
import android.content.Context
import android.database.DataSetObserver
import android.graphics.Canvas
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.view.ViewConfiguration
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import androidx.annotation.Keep
import androidx.annotation.StyleRes
import xyz.diab.ui.R
import xyz.diab.ui.widget.spark.animation.LineSparkAnimator
import xyz.diab.ui.widget.spark.animation.SparkAnimator
import kotlin.math.min

/**
 * Based on RobinHood [Spark](https://github.com/robinhood/spark)
 *
 * A [SparkView] is a simplified line chart with no axes.
 */
@Keep
@Suppress("Unused")
class SparkView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.spark_SparkViewStyle,
    @StyleRes defStyleRes: Int = R.style.AppTheme_SparkView
) : View(context, attrs, defStyleAttr, defStyleRes), ScrubGestureDetector.ScrubListener {

    // Styleable values
    @ColorInt
    var baseLineColor: Int
        private set
    var baseLineWidth: Float
        private set

    var cornerRadius: Float
        private set

    @ColorInt
    var fillColor: Int
        private set
    @Companion.FillType
    var fillType: Int
        private set

    @ColorInt
    var lineColor: Int
        private set
    var lineWidth: Float
        private set

    @ColorInt
    var scrubLineColor: Int
        private set
    var scrubLineWidth: Float
        private set
    var scrubEnabled: Boolean
        private set

    var animator: SparkAnimator? = null

    // onDraw data
    private val renderPath = Path()
    private val sparkPath = Path()
    private val baseLinePath = Path()
    private val scrubLinePath = Path()

    // Adapter
    var adapter: SparkAdapter<*>? = null
        private set

    var xPoints = arrayOf<Float>()
        private set
    var yPoints = arrayOf<Float>()
        private set

    // Misc fields
    private val sparkLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val sparkFillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val baseLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val scrubLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var scrubListener: OnScrubListener? = null
    private val scrubGestureDetector: ScrubGestureDetector

    private var scaleHelper: ScaleHelper? = null
    private var pathAnimator: Animator? = null
    private val contentRect = RectF()

    private val dataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            super.onChanged()
            populatePath()

            if (animator != null) doPathAnimation()
        }

        override fun onInvalidated() {
            super.onInvalidated()
            clearData()
        }
    }

    init {
        val styledAttrs = context.obtainStyledAttributes(
            attrs,
            R.styleable.SparkView,
            defStyleAttr,
            defStyleRes
        )

        baseLineColor = styledAttrs.getColor(R.styleable.SparkView_spark_baseLineColor, 0)
        baseLineWidth = styledAttrs.getDimension(R.styleable.SparkView_spark_lineWidth, 0f)

        cornerRadius = styledAttrs.getDimension(R.styleable.SparkView_spark_cornerRadius, 0f)

        fillColor = styledAttrs.getColor(R.styleable.SparkView_spark_fillColor, 0)
        fillType = styledAttrs.getInt(R.styleable.SparkView_spark_fillType, FILL_NONE)

        lineColor = styledAttrs.getColor(R.styleable.SparkView_spark_lineColor, 0)
        lineWidth = styledAttrs.getDimension(R.styleable.SparkView_spark_lineWidth, 0f)

        scrubEnabled = styledAttrs.getBoolean(R.styleable.SparkView_spark_scrubEnabled, true)
        scrubLineColor =
            styledAttrs.getColor(R.styleable.SparkView_spark_scrubLineColor, baseLineColor)
        scrubLineWidth =
            styledAttrs.getDimension(R.styleable.SparkView_spark_scrubLineWidth, lineWidth)

        val animateChanges =
            styledAttrs.getBoolean(R.styleable.SparkView_spark_animateChanges, false)

        styledAttrs.recycle()

        sparkLinePaint.apply {
            color = lineColor
            strokeCap = Paint.Cap.ROUND
            strokeWidth = lineWidth
            style = Paint.Style.STROKE

            if (cornerRadius != 0f) pathEffect = CornerPathEffect(cornerRadius)
        }
        sparkFillPaint.apply {
            set(sparkLinePaint)
            color = fillColor
            strokeWidth = 0f
            style = Paint.Style.FILL
        }
        baseLinePaint.apply {
            color = baseLineColor
            strokeWidth = baseLineWidth
            style = Paint.Style.STROKE
        }
        scrubLinePaint.apply {
            color = scrubLineColor
            strokeCap = Paint.Cap.ROUND
            strokeWidth = scrubLineWidth
            style = Paint.Style.STROKE
        }

        val handler = Handler()
        val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
        scrubGestureDetector = ScrubGestureDetector(this, handler, touchSlop)
        scrubGestureDetector.enabled = scrubEnabled
        setOnTouchListener(scrubGestureDetector)

        if (animateChanges) {
            animator = LineSparkAnimator()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateContentRect()
        populatePath()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        updateContentRect()
        populatePath()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        canvas.drawPath(baseLinePath, baseLinePaint)
        if (fillType != FILL_NONE) {
            canvas.drawPath(renderPath, sparkFillPaint)
        }

        canvas.drawPath(renderPath, sparkLinePaint)
        canvas.drawPath(scrubLinePath, scrubLinePaint)
    }

    override fun onScrubbed(x: Float, y: Float) {
        if ((adapter?.getCount() ?: 0) == 0) return

        scrubListener?.let { listener ->
            parent.requestDisallowInterceptTouchEvent(true)

            adapter?.let { adapter ->
                val index = getNearestIndex(xPoints, x)
                listener.onScrubbed(adapter.getItem(index))
            }
        }

        setScrubLine(x)
    }

    override fun onScrubEnded() {
        scrubLinePath.reset()

        scrubListener?.onScrubbed(null)
        invalidate()
    }

    /**
     * Get the scaled (pixel) coordinate of your given x value. If no scale is currently computed
     * (for instance [SparkAdapter] has not been set or has less than 2 points of data). This
     * method will return the unscaled value.
     *
     * @param x the value to scale (should be the same units as your graph's data points)
     * @return the pixel coordinates of where this point is located in SparkView's bounds
     */

    fun getScaledX(x: Float): Float =
        scaleHelper?.getX(x) ?: x

    /**
     * Get the scaled (pixel) coordinate of your given y value. If no scale is currently computed
     * (for instance [SparkAdapter] has not been set or has less than 2 points of data). This
     * method will return the unscaled value.
     *
     * @param y the value to scale (should be the same units as your graph's data points)
     * @return the pixel coordinates of where this point is located in SparkView's bounds
     */
    fun getScaledY(y: Float): Float =
        scaleHelper?.getY(y) ?: y

    /**
     * Gets a copy of the sparkLine path
     */
    fun getSparkLinePath(): Path =
        Path(sparkPath)

    /**
     * Set the path to animate in onDraw, used for getAnimation purposes
     */
    fun setAnimationPath(animationPath: Path) {
        this.renderPath.apply {
            reset()
            addPath(animationPath)
            rLineTo(0f, 0f)
        }
        invalidate()
    }

    fun setLineColor(@ColorInt color: Int) {
        lineColor = color
        sparkLinePaint.color = color
        invalidate()
    }

    fun setFillColor(@ColorInt color: Int) {
        fillColor = color
        sparkFillPaint.color = color
        invalidate()
    }

    fun setLineWidth(width: Float) {
        lineWidth = width
        sparkLinePaint.strokeWidth = width
        invalidate()
    }

    fun setCornerRadius(radius: Float) {
        cornerRadius = radius
        if (radius == 0f) {
            sparkLinePaint.pathEffect = null
            sparkFillPaint.pathEffect = null
        } else {
            val effect = CornerPathEffect(radius)
            sparkLinePaint.pathEffect = effect
            sparkFillPaint.pathEffect = effect
        }
        invalidate()
    }

    fun setFillType(@FillType type: Int) {
        if (fillType == type) return
        fillType = type
        populatePath()
    }

    fun setBaseLineColor(@ColorInt color: Int) {
        baseLineColor = color
        baseLinePaint.color = color
        invalidate()
    }

    fun setBaseLineWidth(width: Float) {
        baseLineWidth = width
        baseLinePaint.strokeWidth = width
        invalidate()
    }

    fun setScrubLineColor(@ColorInt color: Int) {
        scrubLineColor = color
        scrubLinePaint.color = color
        invalidate()
    }

    fun setScrubLineWidth(width: Float) {
        scrubLineWidth = width
        scrubLinePaint.strokeWidth = width
        invalidate()
    }

    fun setScrubEnabled(enabled: Boolean) {
        scrubEnabled = enabled
        scrubGestureDetector.enabled = enabled
        invalidate()
    }

    fun setAdapter(adapter: SparkAdapter<*>?) {
        this.adapter?.unregisterDataSetObserver(dataSetObserver)

        adapter?.registerDataSetObserver(dataSetObserver)
        this.adapter = adapter

        populatePath()
    }

    /**
     * Populates the [sparkPath] with points
     */
    private fun populatePath() {
        val adapter = adapter ?: return
        if (width == 0 || height == 0) return

        val adapterCount = adapter.getCount() - 1
        // to draw anything, we need 2 or more points

        if (adapterCount < 2) {
            clearData()
            return
        }

        val scaleHelper = ScaleHelper(adapter, contentRect, lineWidth, isFill())
        this.scaleHelper = scaleHelper

        xPoints = Array(adapterCount) { 0f }
        yPoints = Array(adapterCount) { 0f }

        var i = 0
        while (i < adapterCount) {
            val x = scaleHelper.getX(adapter.getX(i))
            val y = scaleHelper.getY(adapter.getY(i))

            xPoints[i] = x
            yPoints[i] = y

            android.util.Log.w("OHAI", "x: ${adapter.getX(i)} ; y: ${adapter.getY(i)}")

            if (i == 0) {
                sparkPath.moveTo(x, y)
            } else {
                sparkPath.lineTo(x, y)
            }
            i++
        }

        // if we're filling the graph in, close the path's circuit
        getFillEdge()?.let { fillEdge ->
            val lastX = xPoints[adapterCount - 1]
            // line up or down to the fill edge
            sparkPath.lineTo(lastX, fillEdge)
            // line straight left to far edge of the view
            sparkPath.lineTo(paddingStart.toFloat(), fillEdge)
            // closes line back on the first point
            sparkPath.close()
        }

        // make our base line path
        baseLinePath.reset()
        if (adapter.hasBaseLine()) {
            val scaledBaseLine = scaleHelper.getY(adapter.getBaseLine())
            baseLinePath.moveTo(0f, scaledBaseLine)
            baseLinePath.lineTo(width.toFloat(), scaledBaseLine)
        }

        renderPath.apply {
            reset()
            addPath(sparkPath)
        }

        invalidate()
    }

    private fun clearData() {
        scaleHelper = null
        renderPath.reset()
        sparkPath.reset()
        baseLinePath.reset()
        invalidate()
    }

    private fun updateContentRect() {
        contentRect.set(
            paddingStart.toFloat(),
            paddingTop.toFloat(),
            (width - paddingEnd).toFloat(),
            (height - paddingBottom).toFloat()
        )
    }

    private fun isFill() = when (fillType) {
        FILL_UP,
        FILL_DOWN,
        FILL_TOWARD_ZERO -> true
        else -> false
    }

    private fun getFillEdge() = when (fillType) {
        FILL_UP -> paddingTop.toFloat()
        FILL_DOWN -> (height - paddingBottom).toFloat()
        FILL_TOWARD_ZERO -> min(
            scaleHelper?.getY(0f) ?: Float.MIN_VALUE,
            (height - paddingBottom).toFloat()
        )
        else -> null
    }

    private fun setScrubLine(x: Float) {
        val boundedX = resolveBoundedScrubLine(x)
        scrubLinePath.apply {
            reset()
            moveTo(boundedX, paddingTop.toFloat())
            lineTo(boundedX, (height - paddingBottom).toFloat())
        }
        invalidate()
    }

    private fun resolveBoundedScrubLine(x: Float): Float {
        val offset = scrubLineWidth / 2f

        val leftBound = paddingStart + offset
        if (x < leftBound) return leftBound

        val rightBound = width - paddingEnd - offset
        if (x > rightBound) return rightBound

        return x
    }

    private fun getAnimator() =
        animator?.getAnimation(this)

    private fun doPathAnimation() {
        pathAnimator?.cancel()
        pathAnimator = getAnimator()
        pathAnimator?.start()
    }

    /**
     * Listener for a user scrubbing (dragging their finger along) the graph.
     */
    interface OnScrubListener {

        /**
         * Indicates the user is currently scrubbing over the given value. A null value indicates
         * that the user has stopped scrubbing.
         */
        fun onScrubbed(value: Any?)
    }

    companion object {
        const val FILL_NONE = 0
        const val FILL_UP = 1
        const val FILL_DOWN = 2
        const val FILL_TOWARD_ZERO = 3

        @IntDef(FILL_NONE, FILL_UP, FILL_DOWN, FILL_TOWARD_ZERO)
        @Retention(AnnotationRetention.SOURCE)
        annotation class FillType

        internal fun getNearestIndex(points: Array<Float>, x: Float): Int {
            var index = points.binarySearch(x)

            // if binary search returns positive, we had an exact match, return that index
            if (index >= 0) return index

            // otherwise, calculate the binary search's specified insertion index
            index = -1 - index
            // if we're inserting at 0, then our guaranteed nearest index is 0
            if (index == 0) return index

            // if we're inserting at the very end, then our guaranteed nearest index is the final one
            if (index == points.size) return index - 1

            // otherwise we need to check which of our two neighbors we're closer to
            val deltaUp = points[index] - x
            val deltaDown = x - points[index - 1]
            if (deltaUp > deltaDown) {
                // if the below neighbor is closer, decrement our index
                index--
            }

            return index
        }
    }
}
