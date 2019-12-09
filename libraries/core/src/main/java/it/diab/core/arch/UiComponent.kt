/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.core.arch

/**
 * A component that contains a [UiView] and manages its logic.
 *
 * A component can be active or passive:
 * a passive component can only listen to [ComponentEvent]s, while an active
 * component can also post them.
 */
interface UiComponent
