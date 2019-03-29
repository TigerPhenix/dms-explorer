/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.player.domain

import io.reactivex.Observable

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class ServerRepositories(vararg repositories: ServerRepository) : ServerRepository {
    private val repositories: Collection<ServerRepository> = listOf(*repositories)

    override fun initialize() {
        repositories.forEach { it.initialize() }
    }

    override fun terminate() {
        repositories.forEach { it.terminate() }
    }

    override fun reset() {
        repositories.forEach { it.reset() }
    }

    override fun startSearch() {
        repositories.forEach { it.startSearch() }
    }

    override fun stopSearch() {
        repositories.forEach { it.stopSearch() }
    }

    override fun getDiscoveryObservable(): Observable<DiscoveryEvent> {
        return Observable.fromIterable(repositories)
            .flatMap { it.getDiscoveryObservable() }
    }
}
