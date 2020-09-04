package neo.atlantis.ysubs.listener

import neo.atlantis.ysubs.scoreboard.StatsScoreboard
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.koin.core.KoinComponent
import org.koin.core.inject

class PlayerListener : Listener,KoinComponent {
    private val statsScoreboard: StatsScoreboard by inject()

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        statsScoreboard.set(player)
    }
}