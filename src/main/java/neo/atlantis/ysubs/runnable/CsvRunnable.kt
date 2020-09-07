package neo.atlantis.ysubs.runnable

import neo.atlantis.ysubs.config.PluginPreference
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class CsvRunnable(private val filename: String) : BukkitRunnable(), KoinComponent {
    private val plugin: JavaPlugin by inject()
    private val server: Server by inject()
    private val pluginPreference: PluginPreference by inject()

    override fun run() {
        try {
            val csvFile = FileInputStream("${plugin.dataFolder.absolutePath}/$filename")
            val fileReader = BufferedReader(InputStreamReader(csvFile))
            var i: Int = 0
            fileReader.forEachLine {
                if (it.isNotBlank()) {
                    // 3行目以降のみ取得する
                    if (i > 1) {
                        val line = it.split(",").toTypedArray()
                        val playerUUIDString: String? = line[0]
                        if (!playerUUIDString.isNullOrEmpty()) {
                            val playerUuid = UUID.fromString(playerUUIDString)
                            val channelId: String? = line[2]
                            val channelName: String? = line[3]
                            val subscriberCount: Long? = line[4].toLongOrNull()
                            if (!channelId.isNullOrEmpty() && !channelName.isNullOrEmpty() && subscriberCount != null) {
                                pluginPreference.setPlayerChannelId(playerUuid, channelId)
                                pluginPreference.setChannelSubscriberCount(channelId, subscriberCount)
                                pluginPreference.setChannelName(channelId, channelName)
                            } else {
                                pluginPreference.setPlayerChannelId(playerUuid, "")
                            }
                            server.broadcast("Save ${line[1]}'s channel statistics.", Server.BROADCAST_CHANNEL_ADMINISTRATIVE)
                        }
                    }
                }
                i += 1
            }
        } catch (e: IOException) {
            server.broadcast("Failed $e", Server.BROADCAST_CHANNEL_ADMINISTRATIVE)
        }
    }
}