package neo.atlantis.ysubs.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class PluginPreference(private val plugin: JavaPlugin, private val config: FileConfiguration) {
    private object Keys {
        const val CHANNELS = "channels"
        const val CHANNEL_DATA = "channel_data"
        const val SUBSCRIBER_COUNT = "subscriberCount"
        const val API_KEY = "api_key"
    }

    fun resetPlayerChannels() {
        set(Keys.CHANNELS, null)
    }

    fun getPlayerChannelId(player: Player): String? {
        return config.getString(getPlayerChannelIdKey(player))
    }

    fun setPlayerChannelId(player: Player, channelId: String) {
        set(getPlayerChannelIdKey(player), channelId)
    }

    fun getChannelSubscriberCount(channelId: String?): Int {
        channelId ?: return 0
        return config.getInt(getChannelSubscriberCountKey(channelId), 0)
    }

    fun setChannelSubscriberCount(channelId: String, count: Int) {
        set(getChannelSubscriberCountKey(channelId), count)
    }

    var apiKey: String
        get() = config.getString(Keys.API_KEY) ?: ""
        set(value) = set(Keys.API_KEY, value)

    private fun getPlayerChannelIdKey(player: Player): String = "${Keys.CHANNELS}.${player.uniqueId}"

    private fun getChannelSubscriberCountKey(channelId: String): String = "${Keys.CHANNEL_DATA}.${channelId}.${Keys.SUBSCRIBER_COUNT}"

    private fun set(key: String, value: Any?) {
        config.set(key, value)
        plugin.saveConfig()
    }
}