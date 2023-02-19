package net.xst.RawGensPlugin.commands.Freeze.listeners

import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.freezeManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class Freeze_listener : Listener{
	@EventHandler
	fun onFreeze(e:PlayerMoveEvent){
		val player = e.player
		val frozen_players = freezeManager.frozen_players
		if(frozen_players.contains(player.uniqueId)){
			e.isCancelled = true
		}
	}
}