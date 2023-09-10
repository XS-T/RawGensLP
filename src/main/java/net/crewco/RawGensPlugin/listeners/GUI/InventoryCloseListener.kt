package net.crewco.RawGensPlugin.listeners.GUI

import net.crewco.RawGensPlugin.RawGensLP.Companion.editing
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent

class InventoryCloseListener:Listener {
	@EventHandler
	fun onInventoryClose(e:InventoryCloseEvent){
		val player = e.player
		val name = e.view.title
		if(name.startsWith("Permissions GUI")){
			editing.remove(player.uniqueId)
		}
	}
}