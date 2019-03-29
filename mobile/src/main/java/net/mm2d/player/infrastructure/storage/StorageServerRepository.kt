/*
 * Copyright (c) 2017 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.player.infrastructure.storage

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import net.mm2d.player.domain.DiscoveryEvent
import net.mm2d.player.domain.ServerRepository

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class StorageServerRepository : ServerRepository {
    private val discoveryEventSubject = PublishSubject.create<DiscoveryEvent>()

    override fun initialize() {}

    override fun terminate() {}

    override fun reset() {}

    override fun startSearch() {}

    override fun stopSearch() {}

    override fun getDiscoveryObservable(): Observable<DiscoveryEvent> {
        return discoveryEventSubject
    }
}
