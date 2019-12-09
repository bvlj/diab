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
import android.graphics.PathMeasure
import xyz.diab.ui.widget.spark.SparkView

/**
 * Based on RobinHood [Spark](https://github.com/robinhood/spark)
 *
 * Animates the sparkLine by path-tracing from the first point to the last.
 */
class LineSparkAnimator : Animator(), SparkAnimator {
    private val animator = ValueAnimator.ofFloat(0f, 1f)

    override fun getAnimation(sparkView: SparkView): Animator? {
        val linePath = sparkView.getSparkLinePath()

        // get path length
        val pathMeasure = PathMeasure(linePath, false)
        val endLength = pathMeasure.length
        if (endLength <= 0) return null

        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            val animatedPathLength = animatedValue * endLength

            linePath.reset()
            pathMeasure.getSegment(0f, animatedPathLength, linePath, true)

            // set the updated path for the animation
            sparkView.setAnimationPath(linePath)
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
