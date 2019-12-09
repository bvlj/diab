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

package xyz.diab.ui.widget.spark.animation

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.graphics.Path
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import xyz.diab.ui.widget.spark.SparkView

/**
 * Based on RobinHood [Spark](https://github.com/robinhood/spark)
 *
 * Animates each point vertically from the previous position to the current position.
 */
class MorphSparkAnimator : Animator(), SparkAnimator {

    private val animator = ValueAnimator.ofFloat(0f, 1f)
    private val animationPath = Path()

    private var oldYPoints: Array<Float>? = null

    override fun getAnimation(sparkView: SparkView): Animator? {
        val xPoints = sparkView.xPoints
        val yPoints = sparkView.yPoints

        val oldYPoints = oldYPoints ?: emptyArray()

        if (xPoints.isEmpty() || yPoints.isEmpty()) return null

        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            animationPath.reset()

            var step: Float
            var y: Float
            var oldY: Float
            val size = xPoints.size
            var count = 0
            while (count < size) {
                oldY = if (oldYPoints.size > count) oldYPoints[count] else 0f
                step = yPoints[count] - oldY
                y = (step * animatedValue) + oldY

                if (count == 0) {
                    animationPath.moveTo(xPoints[count], y)
                } else {
                    animationPath.lineTo(xPoints[count], y)
                }
                count++
            }

            // set the updated path for the animation
            sparkView.setAnimationPath(animationPath)
        }

        animator.addListener {
            doOnEnd {
                this.oldYPoints = yPoints
            }
        }
        return animator
    }

    override fun isRunning() =
        animator.isRunning

    override fun getDuration() =
        animator.duration

    override fun getStartDelay() =
        animator.startDelay

    override fun setStartDelay(startDelay: Long) {
        animator.startDelay = startDelay
    }

    override fun setInterpolator(value: TimeInterpolator?) {
        animator.interpolator = value
    }

    override fun setDuration(duration: Long): Animator {
        animator.duration = duration
        return this
    }
}
