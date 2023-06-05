package net.crewco.RawGensPlugin.commands

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import com.google.inject.Inject
import net.crewco.RawGensPlugin.CrewCoTemPlate
import org.bukkit.entity.Player

class TemplateCommand @Inject constructor(private val plugin: CrewCoTemPlate) {
	@CommandMethod("templatecommand")
	@CommandDescription("Template Command")
	@CommandPermission("templateplugin.command.templatecommand")
	suspend fun template(player: Player) {
	}
}