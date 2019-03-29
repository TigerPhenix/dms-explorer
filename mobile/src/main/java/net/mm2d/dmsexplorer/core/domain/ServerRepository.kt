/*
 * Copyright (c) 2017 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.dmsexplorer.core.domain

import io.reactivex.Observable


/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
interface ServerRepository {
    fun initialize()

    fun terminate()

    fun reset()

    fun startSearch()

    fun stopSearch()

    fun getDiscoveryObservable(): Observable<DiscoveryEvent>
}
