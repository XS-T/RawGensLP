package net.xst.RawGensPlugin.commands.Warn

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.xst.RawGensPlugin.RawGensCorePlugin
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.pluginmsg
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.warnsmanager
import net.xst.RawGensPlugin.Utils.WarnPlayer

import org.bukkit.entity.Player
import java.io.File
import java.util.*

class history_command {
	@CommandMethod("history <target>")
	@CommandDescription("View a player's warnings")
	@CommandPermission("rawgens.command.history")
	suspend fun history(player: Player,@Argument("target") target:Player) {
		var warnList = warnsmanager.getWarnings(target.uniqueId)
		/*if (warnList.isEmpty()) {
			warnList = readWarningsFromJson(target.uniqueId)!!
		}*/

		if (warnList.isNotEmpty()) {
			player.sendMessage("$pluginmsg Player ${target.name} has ${warnList.size} warning(s):")
			warnList.forEachIndexed { index, warnPlayer ->
				player.sendMessage(Component.text("$index: ${warnPlayer.reason} (${warnPlayer.date})",NamedTextColor.GREEN))
			}
		} else {
			player.sendMessage("$pluginmsg Player ${target.name} has no warnings")
		}
	}

	private fun readWarningsFromJson(playerUuid: UUID): List<WarnPlayer>? {
		val playerFile = File("warnings.json")
		val gson = GsonBuilder().setPrettyPrinting().create()
		if (playerFile.exists()) {
			return gson.fromJson(playerFile.readText(), object : TypeToken<List<WarnPlayer>>() {}.type)!!
		}
		return null
	}
}
