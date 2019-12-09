/*
 * Copyright (c) 2018 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.widget

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import it.diab.glucose.R
import it.diab.glucose.util.VibrationUtil

class NumericKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val btn0: View
    private val btn1: View
    private val btn2: View
    private val btn3: View
    private val btn4: View
    private val btn5: View
    private val btn6: View
    private val btn7: View
    private val btn8: View
    private val btn9: View
    private val btnDel: TextView

    private lateinit var inputView: TextView
    private var onTextChanged: (Int) -> Unit = {}

    val input: Int
        get() =
            if (TextUtils.isEmpty(inputView.text)) -1
            else inputView.text.toString().toInt()

    init {
        View.inflate(context, R.layout.component_numeric_keyboard, this)
        btn0 = findViewById(R.id.keyboard_key_0)
        btn1 = findViewById(R.id.keyboard_key_1)
        btn2 = findViewById(R.id.keyboard_key_2)
        btn3 = findViewById(R.id.keyboard_key_3)
        btn4 = findViewById(R.id.keyboard_key_4)
        btn5 = findViewById(R.id.keyboard_key_5)
        btn6 = findViewById(R.id.keyboard_key_6)
        btn7 = findViewById(R.id.keyboard_key_7)
        btn8 = findViewById(R.id.keyboard_key_8)
        btn9 = findViewById(R.id.keyboard_key_9)
        btnDel = findViewById(R.id.keyboard_key_del)
    }

    fun bindTextView(textView: TextView, callback: (Int) -> Unit = {}) {
        inputView = textView
        onTextChanged = callback
        setupKeys()
    }

    private fun setupKeys() {
        btn0.setOnClickListener { input(0) }
        btn1.setOnClickListener { input(1) }
        btn2.setOnClickListener { input(2) }
        btn3.setOnClickListener { input(3) }
        btn4.setOnClickListener { input(4) }
        btn5.setOnClickListener { input(5) }
        btn6.setOnClickListener { input(6) }
        btn7.setOnClickListener { input(7) }
        btn8.setOnClickListener { input(8) }
        btn9.setOnClickListener { input(9) }

        val delSpan = SpannableStringBuilder().apply {
            append(" ")
            setSpan(ImageSpan(context, R.drawable.ic_backspace), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        btnDel.apply {
            setText(delSpan, TextView.BufferType.SPANNABLE)

            setOnClickListener { del() }
            setOnLongClickListener {
                inputView.text = "0"
                VibrationUtil.vibrateForImportantClick(it)
                true
            }
        }
    }

    private fun input(n: Int) {
        var inStr = inputView.text.toString()
        if (inStr.length > 2) {
            return
        }

        while (inStr.startsWith("0")) {
            inStr = inStr.substring(1, inStr.length)
        }
        inStr += n.toString()
        inputView.text = inStr
        onTextChanged(input)
    }

    private fun del() {
        val inStr = inputView.text.toString()
        if (inStr.length > 1) {
            inputView.text = inStr.substring(0, inStr.length - 1)
        } else {
            inputView.text = "0"
        }
        onTextChanged(input)
    }
}
