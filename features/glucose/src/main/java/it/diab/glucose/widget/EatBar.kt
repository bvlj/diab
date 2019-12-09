/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat
import it.diab.data.entities.Glucose
import it.diab.glucose.R

class EatBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val bar: AppCompatSeekBar

    private var currentColor: Int
    private lateinit var coloredProgressDrawable: Drawable

    init {
        View.inflate(context, R.layout.component_eat_bar, this)
        bar = findViewById(R.id.eat_bar_bar)

        currentColor = ContextCompat.getColor(context, R.color.eat_bar_medium)

        bar.apply {
            max = BAR_COLORS.size - 1
            progress = 1

            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    recolor(BAR_COLORS[progress])
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
                override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
            })

            setOnTouchListener { _, _ -> !this@EatBar.isEnabled }
        }
    }

    @Glucose.Companion.EatLevel
    fun getProgress(): Int {
        return bar.progress
    }

    fun setProgress(progress: Int) {
        bar.progress = progress
    }

    @Suppress("DEPRECATION")
    private fun recolor(@ColorRes color: Int) {
        if (!::coloredProgressDrawable.isInitialized && bar.progressDrawable is LayerDrawable) {
            coloredProgressDrawable = (bar.progressDrawable as LayerDrawable)
                .findDrawableByLayerId(android.R.id.progress)
        }

        val tintColor = ContextCompat.getColor(context, color)
        val animator = ValueAnimator.ofArgb(currentColor, tintColor)
        animator.addUpdateListener { value ->
            val animColor = value.animatedValue as Int

            if (Build.VERSION.SDK_INT >= 29) {
                val colorFilter = BlendModeColorFilter(animColor, BlendMode.SRC_IN)
                coloredProgressDrawable.colorFilter = colorFilter
                bar.thumb?.colorFilter = colorFilter
            } else {
                coloredProgressDrawable.setColorFilter(animColor, PorterDuff.Mode.SRC_IN)
                bar.thumb?.setColorFilter(animColor, PorterDuff.Mode.SRC_IN)
            }
        }
        animator.start()

        currentColor = tintColor
    }

    companion object {
        @ColorRes
        private val BAR_COLORS = arrayOf(
            R.color.eat_bar_low,
            R.color.eat_bar_medium,
            R.color.eat_bar_high,
            R.color.eat_bar_max
        )
    }
}
