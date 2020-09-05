package neo.atlantis.ysubs.command

import neo.atlantis.ysubs.api.YoutubeClient
import neo.atlantis.ysubs.config.PluginPreference
import neo.atlantis.ysubs.runnable.UpdateByChannelIdRunnable
import neo.atlantis.ysubs.runnable.UpdateRunnable
import org.bukkit.Server
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.koin.core.inject
import java.util.*

class YsubsCommand : BaseCommand() {
    private val plugin: JavaPlugin by inject()
    private val server: Server by inject()
    private val pluginPreference: PluginPreference by inject()
    private val youtubeClient: YoutubeClient by inject()

    override fun onCommandByPlayer(player: Player, command: Command, label: String, args: CommandArgs): Boolean {
        return when (args[0]) {
            "set" -> {
                val selectedPlayerName = args[1] ?: return false
                val selectedPlayer = server.getPlayer(selectedPlayerName) ?: return false
                val channelId = args[2] ?: return false
                pluginPreference.setPlayerChannelId(selectedPlayer, channelId)
                true
            }
            "reload" -> {
                val selectedPlayerName = args[1]
                if (selectedPlayerName == null) {
                    val players = server.onlinePlayers.toList()
                    UpdateRunnable(players).runTaskLaterAsynchronously(plugin, 1)
                } else {
                    val selectedPlayer = server.getPlayer(selectedPlayerName) ?: return false
                    UpdateRunnable(listOf(selectedPlayer)).runTaskLaterAsynchronously(plugin, 1)
                }
                true
            }
            "reloadbyuuid" -> {
                val uuid = args[1] ?: return false
                val id = pluginPreference.getPlayerChannelId(UUID.fromString(uuid)) ?: return false
                UpdateByChannelIdRunnable(id).runTaskLaterAsynchronously(plugin, 1)
                true
            }
            "reloadbychannelid" -> {
                val id = args[1] ?: return false
                UpdateByChannelIdRunnable(id).runTaskLaterAsynchronously(plugin, 1)
                true
            }
            "reloadall" -> {
                val ids = pluginPreference.getChannelIds()
                object : BukkitRunnable() {
                    override fun run() {
                        val idsList: MutableList<List<String>> = mutableListOf()
                        var list: MutableList<String> = mutableListOf()
                        var count = 0
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
                }.runTaskLaterAsynchronously(plugin, 1)
                true
            }
            "key" -> {
                val key = args[1] ?: return false
                pluginPreference.apiKey = key
                true
            }
            else -> false
        }
    }

    override fun onCommandByOther(sender: CommandSender, command: Command, label: String, args: CommandArgs): Boolean {
        sender.sendMessage("You must be a player!")
        return false
    }
}