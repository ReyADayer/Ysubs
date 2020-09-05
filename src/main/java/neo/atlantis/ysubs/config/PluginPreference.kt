package neo.atlantis.ysubs.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class PluginPreference(private val plugin: JavaPlugin, private val config: FileConfiguration) {
    private object Keys {
        const val CHANNELS = "channels"
        const val CHANNEL_DATA = "channel_data"
        const val CHANNEL_NAME = "channel_name"
        const val PLAYER_NAME = "player_name"
        const val SUBSCRIBER_COUNT = "subscriberCount"
        const val API_KEY = "api_key"
    }

    fun resetPlayerChannels() {
        set(Keys.CHANNELS, null)
    }

    fun getPlayerChannelId(player: Player): String? {
        return config.getString(getPlayerChannelIdKey(player))
    }

    fun getPlayerChannelId(uuid: UUID): String? {
        return config.getString(getPlayerChannelIdKey(uuid))
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

    fun getChannelNameCount(channelId: String?): String {
        channelId ?: return ""
        return config.getString(getChannelNameKey(channelId)) ?: ""
    }

    fun setChannelName(channelId: String, name: String) {
        set(getChannelNameKey(channelId), name)
    }

    fun getChannelPlayerNameCount(channelId: String?): String {
        channelId ?: return ""
        return config.getString(getChannelPlayerNameKey(channelId)) ?: ""
    }

    fun setChannelPlayerName(channelId: String, name: String) {
        set(getChannelPlayerNameKey(channelId), name)
    }

    var apiKey: String
        get() = config.getString(Keys.API_KEY) ?: ""
        set(value) = set(Keys.API_KEY, value)

    private fun getPlayerChannelIdKey(player: Player): String = "${Keys.CHANNELS}.${player.uniqueId}"

    private fun getPlayerChannelIdKey(uuid: UUID): String = "${Keys.CHANNELS}.${uuid}"

    private fun getChannelSubscriberCountKey(channelId: String): String = "${Keys.CHANNEL_DATA}.${channelId}.${Keys.SUBSCRIBER_COUNT}"

    private fun getChannelNameKey(channelId: String): String = "${Keys.CHANNEL_DATA}.${channelId}.${Keys.CHANNEL_NAME}"

    private fun getChannelPlayerNameKey(channelId: String): String = "${Keys.CHANNEL_DATA}.${channelId}.${Keys.PLAYER_NAME}"

    private fun set(key: String, value: Any?) {
        config.set(key, value)
        plugin.saveConfig()
    }
}