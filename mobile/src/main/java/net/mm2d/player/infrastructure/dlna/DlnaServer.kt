package net.mm2d.player.infrastructure.dlna

import io.reactivex.Observable
import io.reactivex.Single
import net.mm2d.android.upnp.cds.CdsObject
import net.mm2d.android.upnp.cds.MediaServer
import net.mm2d.android.upnp.cds.RootCdsObject
import net.mm2d.player.domain.Entry
import net.mm2d.player.domain.Result
import net.mm2d.player.domain.Server

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class DlnaServer(
    private val mediaServer: MediaServer
) : Server {
    private val rootEntry: DlnaEntry = DlnaEntry(this, null, RootCdsObject(mediaServer.udn))

    override val isAvailable: Boolean
        get() = false

    internal fun browse(objectId: String): Observable<CdsObject> {
        return mediaServer.browse(objectId)
    }

    internal fun hasDeleteFunction(): Boolean {
        return mediaServer.hasDestroyObject()
    }

    internal fun delete(objectId: String): Single<Result> {
        return mediaServer.destroyObject(objectId)
            .map { if (it == MediaServer.NO_ERROR) Result.SUCCESS else Result.ERROR }
    }

    override fun getName(): String {
        return mediaServer.friendlyName
    }

    override fun getRoot(): Entry {
        return rootEntry
    }

    override fun setActive(active: Boolean) {}
}
