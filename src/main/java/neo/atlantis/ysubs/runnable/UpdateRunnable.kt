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

        val idsList: MutableList<List<String>> = mutableListOf()
        var list: MutableList<String> = mutableListOf()
        var count = 0
        if (ids.size <= 20) {
            youtubeClient.getChannels(ids)
        } else {
            ids.forEach { id ->
                list.add(id)
                count += 1
                if (count == 20) {
                    count = 0
                    idsList.add(list)
                    list = mutableListOf()
                }
            }
            idsList.forEach {
                youtubeClient.getChannels(it)
            }
        }
    }
}