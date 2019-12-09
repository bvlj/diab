/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.export.utils

import android.app.Activity
import android.app.KeyguardManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.fragment.app.Fragment
import it.diab.export.ExportService
import it.diab.export.R

class SecureFilePickerHelper(
    private val fragment: Fragment,
    private val callbacks: Callbacks
) {

    private val res = fragment.resources

    fun authenticate(category: Int) {
        val keyguardManager = fragment.context?.getSystemService(KeyguardManager::class.java)
        val title = res.getString(R.string.export_ask_auth_title)
        val message = res.getString(R.string.export_ask_auth_message)

        val requestIntent = keyguardManager?.createConfirmDeviceCredentialIntent(title, message)
        if (requestIntent == null) {
            callbacks.onAuthentication(category, AuthResult.NOT_NEEDED)
            return
        }

        fragment.startActivityForResult(requestIntent, REQUEST_AUTHENTICATION or category)
    }

    fun pickDestination(category: Int) {
        val type = when (category) {
            ML -> MIME_ML
            XLSX -> MIME_XLSX
            else -> throw IllegalArgumentException("$category is not a valid category")
        }

        val name = when (category) {
            ML -> "diab_ml.zip"
            XLSX -> "diab.xlsx"
            else -> throw IllegalArgumentException("$category is not a valid category")
        }

        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setType(type)
            .putExtra(Intent.EXTRA_TITLE, name)

        fragment.startActivityForResult(intent, REQUEST_PICK or category)
    }

    fun onResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        val success = resultCode == Activity.RESULT_OK
        when {
            (requestCode and REQUEST_AUTHENTICATION) != 0 -> callbacks.onAuthentication(
                requestCode xor REQUEST_AUTHENTICATION,
                if (success) AuthResult.SUCCESS else AuthResult.FAILURE
            )
            (requestCode and REQUEST_PICK) != 0 -> onPicked(
                requestCode xor REQUEST_PICK,
                data?.data
            )
        }
    }

    private fun onPicked(category: Int, data: Uri?) {
        val action = when (category) {
            ML -> ExportService.TARGET_CSV
            XLSX -> ExportService.TARGET_XLSX
            else -> throw IllegalArgumentException("$category is not a valid category")
        }

        val intent = Intent(fragment.requireContext(), ExportService::class.java)
            .putExtra(ExportService.EXPORT_TARGET, action)
            .putExtra(Intent.EXTRA_ORIGINATING_URI, data)

        if (Build.VERSION.SDK_INT >= 26) {
            fragment.requireContext().startForegroundService(intent)
        } else {
            fragment.requireContext().startService(intent)
        }
    }

    interface Callbacks {
        fun onAuthentication(category: Int, result: AuthResult)
    }

    enum class AuthResult {
        SUCCESS,
        FAILURE,
        NOT_NEEDED
    }

    companion object {
        const val ML = 1
        const val XLSX = 1 shl 1

        const val REQUEST_PICK = 1 shl 2
        const val REQUEST_AUTHENTICATION = 1 shl 3

        const val MIME_ML = "application/zip"
        const val MIME_XLSX = "application/octet-stream"
    }
}
