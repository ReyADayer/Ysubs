package neo.atlantis.ysubs.runnable

import neo.atlantis.ysubs.api.YoutubeClient
import neo.atlantis.ysubs.config.PluginPreference
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.koin.core.KoinComponent
import org.koin.core.inject

class UpdateRunnable(
        private val players: List<Player>,
) : BukkitRunnable(), KoinComponent {
    private val pluginPreference: PluginPreference by inject()
    private val youtubeClient: YoutubeClient by inject()

    override fun run() {
        val ids: List<String> = players.mapNotNull {
            pluginPreference.getPlayerChannelId(it)
        }
        youtubeClient.getChannels(ids)
    }
}