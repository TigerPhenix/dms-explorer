package net.mm2d.player.infrastructure.dlna

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import net.mm2d.android.upnp.cds.CdsObject
import net.mm2d.player.domain.Entry
import net.mm2d.player.domain.PlayList
import net.mm2d.player.domain.Result
import net.mm2d.dmsexplorer.domain.entity.ContentType

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class DlnaEntry internal constructor(
    private val dlnaServer: DlnaServer,
    private val parentEntry: DlnaEntry?,
    private val cdsObject: CdsObject
) : Entry {
    private var subject: Subject<DlnaEntry>? = null
    private var disposable: Disposable? = null

    override val isContent: Boolean
        get() = cdsObject.isItem

    override val isContainer: Boolean
        get() = cdsObject.isContainer

    override val isDeletable: Boolean
        get() = dlnaServer.hasDeleteFunction() && isNotRestricted

    private val isNotRestricted: Boolean
        get() = cdsObject.getIntValue(CdsObject.RESTRICTED, -1) == 0

    override val isRoot: Boolean
        get() = parentEntry == null

    override fun delete(): Single<Result> {
        return if (isDeletable) {
            dlnaServer.delete(cdsObject.objectId)
        } else Single.just(Result.ERROR)
    }

    override fun getType(): ContentType {
        when (cdsObject.type) {
            CdsObject.TYPE_VIDEO -> return ContentType.MOVIE
            CdsObject.TYPE_AUDIO -> return ContentType.MUSIC
            CdsObject.TYPE_IMAGE -> return ContentType.PHOTO
            CdsObject.TYPE_CONTAINER -> return ContentType.CONTAINER
            CdsObject.TYPE_UNKNOWN -> return ContentType.UNKNOWN
        }
        return ContentType.UNKNOWN
    }

    override fun getServer(): DlnaServer {
        return dlnaServer
    }

    override fun getParent(): DlnaEntry? {
        return parentEntry
    }

    override fun getName(): String {
        return cdsObject.title
    }

    override fun readEntries(noCache: Boolean): Observable<DlnaEntry> {
        if (!noCache) {
            subject?.let { return it }
        }
        dispose()
        val subject = ReplaySubject.create<DlnaEntry>().toSerialized()
        disposable = dlnaServer.browse(cdsObject.objectId)
            .observeOn(Schedulers.io())
            .map { createChildEntry(it) }
            .subscribe({ subject.onNext(it) },
                { subject.onError(it) },
                { subject.onComplete() })
        this.subject = subject
        return subject.doOnDispose { dispose() }
    }

    private fun createChildEntry(cdsObject: CdsObject): DlnaEntry {
        return DlnaEntry(dlnaServer, this, cdsObject)
    }

    private fun dispose() {
        disposable?.dispose() ?: return
        disposable = null
        val subject = subject ?: return
        if (!subject.hasComplete()) {
            subject.onComplete()
        }
    }

    override fun createPlayList(type: ContentType): PlayList {
        val observable = readEntries(false)
            .filter { entry -> entry.getType() == type }
        return DlnaPlayList(observable)
    }
}
