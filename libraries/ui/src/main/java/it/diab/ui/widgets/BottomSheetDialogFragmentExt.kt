/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.ui.widgets

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import it.diab.ui.R

abstract class BottomSheetDialogFragmentExt : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dialogInflater = LayoutInflater.from(ContextThemeWrapper(requireContext(), R.style.AppTheme))
        return onCreateDialogView(dialogInflater, container, savedInstanceState)
    }

    override fun getTheme() = R.style.AppTheme_BottomSheetDialog

    abstract fun onCreateDialogView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
}
