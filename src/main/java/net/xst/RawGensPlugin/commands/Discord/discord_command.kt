package net.xst.RawGensPlugin.commands.Discord

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.pluginmsg
import org.bukkit.entity.Player

class discord_command {
	@CommandMethod("r_discord")
	@CommandDescription("give you the discord link.")
	suspend fun onDiscord(player: Player) {
		player.sendMessage("$pluginmsg https://discord.gg/peADyT7WPt")
	}

}