package net.xst.RawGensPlugin.commands.Freeze

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import net.xst.RawGensPlugin.RawGensCorePlugin
import org.bukkit.entity.Player

class unfreeze_command {
	@CommandMethod("unfreeze <target>")
	@CommandDescription("unfreezes a player")
	@CommandPermission("rawgens.command.unfreeze")
	suspend fun freeze(player: Player, @Argument("target") target: Player) {
		RawGensCorePlugin.freezeManager.unfreeze(target,player)
	}
}