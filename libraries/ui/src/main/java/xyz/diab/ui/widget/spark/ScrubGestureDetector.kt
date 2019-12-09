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

import android.annotation.SuppressLint
import android.os.Handler
import android.view.MotionEvent
import android.view.View

/**
 * Based on RobinHood [Spark](https://github.com/robinhood/spark)
 *
 * Exposes simple methods for detecting scrub events.
 */
class ScrubGestureDetector(
    private val scrubListener: ScrubListener,
    private val handler: Handler,
    private val touchSlop: Int
) : View.OnTouchListener {

    private var downX = 0f
    private var downY = 0f

    private val longPressRunnable = Runnable {
        scrubListener.onScrubbed(downX, downY)
    }

    var enabled = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (!enabled) return false
        return when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // store the time to compute whether future events are 'long presses'
                downX = event.x
                downY = event.y

                handler.postDelayed(longPressRunnable, LONG_PRESS_TIMEOUT_MS)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                // calculate the elapsed time since the down event
                val timeDelta = event.eventTime - event.downTime

                // if the user has intentionally long-pressed
                if (timeDelta >= LONG_PRESS_TIMEOUT_MS) {
                    handler.removeCallbacks(longPressRunnable)
                    scrubListener.onScrubbed(event.x, event.y)
                } else {
                    // if we moved before longPress, remove the callback if we exceeded the tap slop
                    val deltaX = event.x - downX
                    val deltaY = event.y - downY
                    if (deltaX >= touchSlop || deltaY >= touchSlop) {
                        handler.removeCallbacks(longPressRunnable)
                        // We got a MOVE event that exceeded tap slop but before the long-press
                        // threshold, we don't care about this series of events anymore.
                        return false
                    }
                }
                return true
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                handler.removeCallbacks(longPressRunnable)
                scrubListener.onScrubEnded()
                return true
            }
            else -> false
        }
    }

    interface ScrubListener {

        fun onScrubbed(x: Float, y: Float)

        fun onScrubEnded()
    }

    companion object {
        private const val LONG_PRESS_TIMEOUT_MS = 250L
    }
}
