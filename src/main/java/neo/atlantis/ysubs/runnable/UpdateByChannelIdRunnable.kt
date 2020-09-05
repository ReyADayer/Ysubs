package neo.atlantis.ysubs.runnable

import neo.atlantis.ysubs.api.YoutubeClient
import org.bukkit.scheduler.BukkitRunnable
import org.koin.core.KoinComponent
import org.koin.core.inject

class UpdateByChannelIdRunnable(
        private val channelId: String,
) : BukkitRunnable(), KoinComponent {
    private val youtubeClient: YoutubeClient by inject()

    override fun run() {
        youtubeClient.getChannels(listOf(channelId))
    }
}