package net.xst.RawGensPlugin.commands.Vanish.listeners

import com.google.inject.Inject
import net.xst.RawGensPlugin.RawGensCorePlugin
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.vanishManager
import org.bukkit.Server
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class VanishPlayerOnJoin @Inject constructor(private val server: Server,private val plugin:RawGensCorePlugin):Listener {
	@EventHandler
	fun onPlayerJoin(e:PlayerJoinEvent){
		val player = e.player
		val vanishedplayers = server.onlinePlayers.filter {vanishManager.isVanished(it)}
		if(vanishedplayers.isNotEmpty()){
			vanishedplayers.forEach{player.hidePlayer(plugin,it)}
		}
	}
}