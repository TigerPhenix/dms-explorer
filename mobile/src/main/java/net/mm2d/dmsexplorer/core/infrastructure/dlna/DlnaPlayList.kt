package net.mm2d.dmsexplorer.core.infrastructure.dlna

import android.annotation.SuppressLint
import io.reactivex.Observable
import net.mm2d.dmsexplorer.core.domain.Entry
import net.mm2d.dmsexplorer.core.domain.PlayList
import java.util.*

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
@SuppressLint("CheckResult")
class DlnaPlayList(
    observable: Observable<DlnaEntry>
) : PlayList {
    private var cursor: Int = 0
    private val entries = Collections.synchronizedList(ArrayList<Entry>())

    init {
        observable.subscribe { entries.add(it) }
    }

    override fun setCurrent(index: Int) {
        cursor = index
    }

    override fun getCurrent(): Entry {
        return entries[cursor]
    }

    override fun get(index: Int): Entry {
        return entries[index]
    }

    override fun size(): Int {
        return entries.size
    }

    override fun iterator(): Iterator<Entry> {
        return entries.iterator()
    }

    override operator fun next(): Entry {
        if (++cursor >= entries.size) cursor = 0
        return entries[cursor]
    }

    override fun previous(): Entry {
        if (--cursor < 0) cursor = entries.size - 1
        return entries[cursor]
    }
}
