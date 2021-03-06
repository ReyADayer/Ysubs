package neo.atlantis.ysubs.scoreboard

import neo.atlantis.ysubs.config.PluginPreference
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective

class StatsScoreboard(
        private val server: Server,
        private val pluginPreference: PluginPreference) {
    companion object {
        private const val TAG = "count"
    }

    private val scoreboard = server.scoreboardManager?.mainScoreboard!!
    private val objective = getObjective()
    fun set(player: Player) {
        val channelId = pluginPreference.getPlayerChannelId(player)
        val count: Int = pluginPreference.getChannelSubscriberCount(channelId)
        val score = objective.getScore(player.name)
        score.score = count
        showVoteScore()
    }

    private fun showVoteScore() {
        server.onlinePlayers.forEach {
            it.scoreboard = scoreboard
        }
    }

    private fun getObjective(): Objective {
        scoreboard.getObjective(TAG)?.let {
            return it
        }

        return scoreboard.registerNewObjective(TAG, TAG, "登録者").apply {
            displaySlot = DisplaySlot.BELOW_NAME
        }
    }
}