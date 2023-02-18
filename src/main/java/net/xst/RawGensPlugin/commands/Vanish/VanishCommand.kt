package net.xst.RawGensPlugin.commands.Vanish

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import com.google.inject.Inject
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.xst.RawGensPlugin.RawGensCorePlugin
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.vanishManager
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.vanishedPlayers
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*

class VanishCommand @Inject constructor(private val plugin: RawGensCorePlugin){
	@CommandMethod("vanish")
	@CommandPermission("rawgens.command.vanish")
	@CommandDescription("Make yourself invisible to other players")
	suspend fun vanish(player: Player) {
		vanishManager.toggle_vanish(player)
		/*if (player.uniqueId in vanishedPlayers) {
			// Unvanish
			vanishedPlayers.remove(player.uniqueId)
			for (other in Bukkit.getOnlinePlayers()) {
				other.showPlayer(plugin, player)
			}
			player.sendMessage(ChatColor.GREEN.toString() + "You are now visible to other players")
		} else {
			// Vanish
			vanishedPlayers.add(player.uniqueId)
			for (other in Bukkit.getOnlinePlayers()) {
				if (other != player) {
					other.hidePlayer(plugin, player)
				}
			}
			player.sendMessage(ChatColor.GREEN.toString() + "You are now invisible to other players")
		}*/
	}
}