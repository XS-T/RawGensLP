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
		val xCord = plugin.config.getDouble("rawgens.spawn.x")
		val yCord = plugin.config.getDouble("rawgens.spawn.y")
		val zCord = plugin.config.getDouble("rawgens.spawn.z")
		val pitch = plugin.config.getDouble("rawgens.spawn.pitch")
		val yaw = plugin.config.getDouble("rawgens.spawn.yaw")
		val worldName = plugin.config.getString("rawgens.spawn.world")

		val world = Bukkit.getWorld(worldName!!)
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