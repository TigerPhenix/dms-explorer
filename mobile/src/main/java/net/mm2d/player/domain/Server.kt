/*
 * Copyright (c) 2017 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.player.domain

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
interface Server {
    val isAvailable: Boolean

    fun getName(): String

    fun getRoot(): Entry

    fun setActive(active: Boolean)
}
