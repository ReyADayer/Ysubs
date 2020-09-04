package neo.atlantis.ysubs

import neo.atlantis.ysubs.api.YoutubeClient
import neo.atlantis.ysubs.command.YsubsCommand
import neo.atlantis.ysubs.config.PluginPreference
import neo.atlantis.ysubs.ext.initCommand
import neo.atlantis.ysubs.ext.registerListener
import neo.atlantis.ysubs.ext.scheduleAsyncRunnable
import neo.atlantis.ysubs.listener.PlayerListener
import neo.atlantis.ysubs.runnable.UpdateRunnable
import neo.atlantis.ysubs.scoreboard.StatsScoreboard
import net.atlantis.jinrocraft.PluginCommands
import net.atlantis.jinrocraft.PluginPermissions
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.annotation.command.Command
import org.bukkit.plugin.java.annotation.command.Commands
import org.bukkit.plugin.java.annotation.permission.Permission
import org.bukkit.plugin.java.annotation.permission.Permissions
import org.bukkit.plugin.java.annotation.plugin.ApiVersion
import org.bukkit.plugin.java.annotation.plugin.Description
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.plugin.java.annotation.plugin.author.Author
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@Plugin(name = "Ysubs", version = "1.0-SNAPSHOT")
@Description("Youtube Subs Counter")
@Author("ReyADayer")
@ApiVersion(ApiVersion.Target.v1_15)
@Permissions(
        Permission(
                name = PluginPermissions.ADMIN,
                desc = "Gives access to Ysubs admin commands",
                defaultValue = PermissionDefault.OP
        )
)
@Commands(
        Command(
                name = PluginCommands.YSUBS,
                desc = "ysubs command",
                permission = PluginPermissions.ADMIN,
                usage = "/<command>",
                permissionMessage = "You don't have <permission>"
        )
)
class Ysubs : JavaPlugin() {
    override fun onEnable() {
        saveDefaultConfig()

        setupKoin()

        registerListener(PlayerListener())

        initCommand(PluginCommands.YSUBS, YsubsCommand())
    }

    override fun onDisable() {
        stopKoin()
    }

    private val myModule = module {
        single<JavaPlugin> { this@Ysubs }
        single { config }
        single { server }
        single { PluginPreference(get(), get()) }
        single { StatsScoreboard(get(), get()) }
        factory { YoutubeClient(get(), get()) }
    }

    private fun setupKoin() {
        startKoin {
            modules(listOf(myModule))
        }
    }
}