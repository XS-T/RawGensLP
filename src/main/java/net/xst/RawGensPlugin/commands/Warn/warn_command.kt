package net.xst.RawGensPlugin.commands.Warn

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import com.google.inject.Inject
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.xst.RawGensPlugin.RawGensCorePlugin
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.pluginmsg
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.warnsmanager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class warn_command @Inject constructor(private val plugin: RawGensCorePlugin){
	@CommandMethod("warn <target> <reason>")
	@CommandDescription("Warn a player")
	@CommandPermission("rawgens.command.warn")
	suspend fun warn(player: Player,@Argument("target") target:Player,@Argument("reason")reason: Array<String>) {
		val reason_message = reason.joinToString(" ")
		val users = Bukkit.getOnlinePlayers()
		warnsmanager.addWarning(player.uniqueId,reason_message,Date())
		target.sendMessage("$pluginmsg You have been warned by $player for reason: $reason_message")
	}
}