package net.xst.RawGensPlugin.commands.Spawn

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import com.google.inject.Inject
import net.xst.RawGensPlugin.RawGensCorePlugin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

class spawn_command @Inject constructor(private val plugin: RawGensCorePlugin) {
	@CommandMethod("spawn")
	@CommandDescription("Takes you to spawn")
	suspend fun onspawn(player: Player) {
		val config = plugin.config
		val xCord = config.getDouble("x")
		val yCord = config.getDouble("y")
		val zCord = config.getDouble("z")
		val pitch = config.getDouble("pitch")
		val yaw = config.getDouble("yaw")
		val worldName = config.getString("world-name")
		player.sendMessage(worldName!!)

		val world = Bukkit.getWorld(worldName)
		if (world == null) {
			plugin.logger.warning("Invalid world name specified in config: $worldName")
			return
		}

		val location = Location(world, xCord, yCord, zCord, yaw.toFloat(), pitch.toFloat())
		if (!player.teleport(location)) {
			plugin.logger.warning("Error teleporting player to spawn")
		}
	}

}