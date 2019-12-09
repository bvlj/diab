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

package xyz.diab.editor

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import xyz.diab.roboto.events.EventBus
import xyz.diab.roboto.navigation.UiNavigator

abstract class BottomSheetBaseFragment : BottomSheetDialogFragment() {

    protected lateinit var bus: EventBus
    protected val navigator by lazy { UiNavigator(bus) }

    protected val viewScope: CoroutineScope
        get() = viewLifecycleOwner.lifecycleScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity = activity ?: return
        bus = EventBus[activity]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialogInflater = LayoutInflater.from(
            ContextThemeWrapper(requireContext(), R.style.AppTheme)
        )
        return onCreateDialogView(dialogInflater, container, savedInstanceState)
    }

    override fun getTheme() = R.style.AppTheme_BottomSheetDialog

    abstract fun onCreateDialogView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
}
