/*
 * Copyright (c) 2019 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.player

import net.mm2d.android.upnp.AvControlPointManager
import net.mm2d.log.Logger
import net.mm2d.player.domain.ServerRepositories
import net.mm2d.player.infrastructure.dlna.DlnaServerRepository

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class MainRepository {
    private val mAvControlPointManager: AvControlPointManager = AvControlPointManager()
    private val mServerRepositories: ServerRepositories

    init {
        mServerRepositories = ServerRepositories(DlnaServerRepository(mAvControlPointManager))
        mServerRepositories.getDiscoveryObservable()
            .subscribe { discoveryEvent -> Logger.e(discoveryEvent.type.toString() + " " + discoveryEvent.server) }
    }

    fun search() {
        mServerRepositories.startSearch()
    }
}
