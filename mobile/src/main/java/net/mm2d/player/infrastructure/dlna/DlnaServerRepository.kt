/*
 * Copyright (c) 2017 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.player.infrastructure.dlna

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import net.mm2d.android.upnp.AvControlPointManager
import net.mm2d.android.upnp.cds.MediaServer
import net.mm2d.android.upnp.cds.MsControlPoint
import net.mm2d.android.upnp.cds.MsControlPoint.MsDiscoveryListener
import net.mm2d.player.domain.DiscoveryEvent
import net.mm2d.player.domain.ServerRepository
import java.util.*

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class DlnaServerRepository(
    private val avControlPointManager: AvControlPointManager
) :
    ServerRepository {
    private val discoveryEventSubject = PublishSubject.create<DiscoveryEvent>()
    private val msControlPoint: MsControlPoint = avControlPointManager.msControlPoint
    private val dlnaServers = ArrayList<DlnaServer>()

    init {
        msControlPoint.setMsDiscoveryListener(object : MsDiscoveryListener {
            override fun onDiscover(server: MediaServer) {
                discover(DlnaServer(server))
            }

            override fun onLost(server: MediaServer) {
                lost(DlnaServer(server))
            }
        })
    }

    private fun discover(server: DlnaServer) {
        dlnaServers.add(server)
        discoveryEventSubject.onNext(DiscoveryEvent.discover(server))
    }

    private fun lost(server: DlnaServer) {
        dlnaServers.remove(server)
        discoveryEventSubject.onNext(DiscoveryEvent.lost(server))
    }

    override fun initialize() {}

    override fun terminate() {}

    override fun reset() {}

    override fun startSearch() {
        avControlPointManager.search()
    }

    override fun stopSearch() {}

    override fun getDiscoveryObservable(): Observable<DiscoveryEvent> {
        return discoveryEventSubject
    }
}
