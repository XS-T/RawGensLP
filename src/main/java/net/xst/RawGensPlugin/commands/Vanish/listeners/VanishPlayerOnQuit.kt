package net.xst.RawGensPlugin.commands.Vanish.listeners

import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.vanishedPlayers
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class VanishPlayerOnQuit:Listener {
	@EventHandler
	fun onLeave(e:PlayerQuitEvent){
		val player = e.player
		if(vanishedPlayers.contains(player.uniqueId)){
			e.quitMessage = ""
		}
	}
}