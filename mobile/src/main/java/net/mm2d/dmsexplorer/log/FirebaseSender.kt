/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.dmsexplorer.log

import android.content.Context
import android.os.Bundle
import androidx.annotation.Size

import com.google.firebase.analytics.FirebaseAnalytics

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal class FirebaseSender(context: Context) : Sender {
    private val analytics = FirebaseAnalytics.getInstance(context)

    override fun logEvent(
        @Size(min = 1L, max = 40L) name: String,
        params: Bundle?
    ) {
        analytics.logEvent(name, params)
    }
}
