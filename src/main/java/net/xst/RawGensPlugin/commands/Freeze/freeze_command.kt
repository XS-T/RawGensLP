package net.xst.RawGensPlugin.commands.Freeze

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.freezeManager
import org.bukkit.entity.Player

class freeze_command {
	@CommandMethod("freeze <target> <reason>")
	@CommandDescription("freezes a player")
	@CommandPermission("rawgens.command.freeze")
	suspend fun freeze(player: Player,@Argument("target") target:Player,@Argument("reason") reason:Array<String>) {
		val message = reason.joinToString(" ")
		freezeManager.freeze(target,message,player)
	}
}