/*
 * Copyright (c) 2017 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.dmsexplorer.core.domain

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
interface PlayList : Iterable<Entry> {
    fun setCurrent(index: Int)
    fun getCurrent(): Entry
    fun next(): Entry
    fun previous(): Entry
    operator fun get(index: Int): Entry
    fun size(): Int
}
