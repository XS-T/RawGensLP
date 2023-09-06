package net.crewco.RawGensPlugin.listeners.GUI

import com.google.inject.Inject
import net.crewco.RawGensPlugin.RawGensLP
import net.crewco.RawGensPlugin.RawGensLP.Companion.gui
//import net.crewco.RawGensPlugin.RawGensLP.Companion.gui
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class PlayerInteractListener @Inject constructor(private val plugin: RawGensLP) :Listener  {
	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		val player = event.player
		val item = player.inventory.itemInMainHand

		if (item.type == Material.DIAMOND) {
			gui.openGui(player)
		}
	}
}