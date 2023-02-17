package net.xst.RawGensPlugin.commands

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import com.google.inject.Inject
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.xst.RawGensPlugin.RawGensCorePlugin
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class TemplateCommand @Inject constructor(private val plugin: RawGensCorePlugin) {
	@CommandMethod("templatecommand")
	@CommandDescription("Template Command")
	@CommandPermission("templateplugin.command.templatecommand")
	suspend fun template(player: Player) {
	}
}