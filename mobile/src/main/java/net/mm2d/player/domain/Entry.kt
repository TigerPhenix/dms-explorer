/*
 * Copyright (c) 2017 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.player.domain

import io.reactivex.Observable
import io.reactivex.Single
import net.mm2d.dmsexplorer.domain.entity.ContentType

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
interface Entry {
    val isContent: Boolean

    val isContainer: Boolean

    val isDeletable: Boolean

    val isRoot: Boolean

    fun getType(): ContentType

    fun getServer(): Server

    fun getParent(): Entry?

    fun getName(): String

    fun delete(): Single<Result>

    fun readEntries(noCache: Boolean): Observable<out Entry>

    fun createPlayList(type: ContentType): PlayList
}
