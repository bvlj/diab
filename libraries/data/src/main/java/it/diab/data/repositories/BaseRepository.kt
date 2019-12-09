/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.data.repositories

import androidx.annotation.VisibleForTesting
import it.diab.data.AppDatabase

abstract class BaseRepository {

    @VisibleForTesting
    fun setDebugMode() {
        AppDatabase.TEST_MODE = true
    }
}
