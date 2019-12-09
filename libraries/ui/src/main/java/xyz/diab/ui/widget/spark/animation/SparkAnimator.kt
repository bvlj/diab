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
import xyz.diab.ui.widget.spark.SparkView

/**
 * Based on RobinHood [Spark](https://github.com/robinhood/spark)
 *
 *  This interface is for animating SparkView when it changes
 */
interface SparkAnimator {

    /**
     * Returns an Animator that performs the desired animation. Must call
     * [SparkView.setAnimationPath] for each animation frame.
     *
     * See [LineSparkAnimator] and [MorphSparkAnimator] for examples.
     *
     * @param sparkView The SparkView object
     */
    fun getAnimation(sparkView: SparkView): Animator?
}
