package it.diab.settings.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import it.diab.core.override.BaseFitHandler
import it.diab.core.util.SystemUtil
import it.diab.settings.R
import it.diab.ui.util.extensions.getAttr

internal class FitPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = context.getAttr(R.attr.preferenceStyle, android.R.attr.preferenceStyle),
    defStyleRes: Int = 0
) : Preference(context, attrs, defStyleAttr, defStyleRes) {
    private val fitHandler = SystemUtil.getOverrideObject(
        BaseFitHandler::class.java, context, R.string.config_class_fit_handler
    )

    init {
        if (!fitHandler.isEnabled) {
            isVisible = false
        }
    }

    fun getFitFragment(): Fragment? =
        if (fitHandler.isEnabled) fitHandler.getFragment()
        else null
}
