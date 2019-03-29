package net.mm2d.player.domain

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
class DiscoveryEvent private constructor(
    val type: Type,
    val server: Server
) {
    enum class Type {
        DISCOVER,
        LOST
    }

    companion object {
        fun discover(server: Server): DiscoveryEvent {
            return DiscoveryEvent(Type.DISCOVER, server)
        }

        fun lost(server: Server): DiscoveryEvent {
            return DiscoveryEvent(Type.LOST, server)
        }
    }
}
