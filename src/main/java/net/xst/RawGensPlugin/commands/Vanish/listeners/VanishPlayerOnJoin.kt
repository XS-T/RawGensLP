package net.xst.RawGensPlugin.commands.Vanish.listeners

import com.google.inject.Inject
import net.luckperms.api.node.types.PermissionNode
import net.xst.RawGensPlugin.RawGensCorePlugin
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.api
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.pluginmsg
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.vanishManager
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.vanishedPlayers
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*

class VanishPlayerOnJoin @Inject constructor(private val server: Server,private val plugin:RawGensCorePlugin):Listener {
	@EventHandler
	fun onPlayerJoin(e: PlayerJoinEvent) {
		val player = e.player
		if(!vanishedPlayers.contains(player.uniqueId)){
			for(hidden_players in vanishedPlayers){
				val online_players = Bukkit.getOnlinePlayers()
				val hp = Bukkit.getOfflinePlayer(hidden_players)
				val permnode = PermissionNode.builder("rawgens.vanish.see").build()
				for(other in online_players){
					if(api.userManager.getUser(other.uniqueId)?.nodes!!.contains(permnode)){
						other.showPlayer(plugin,hp as Player)
					}else{
						other.hidePlayer(plugin,hp as Player)
					}
				}
			}
		}else{
			if (vanishedPlayers.isNotEmpty()) {
				if(vanishedPlayers.contains(player.uniqueId)){
					val online_players = Bukkit.getOnlinePlayers()
					for(other in online_players){
						if(other !=player){
							other.hidePlayer(plugin,player)
						}
						e.joinMessage = ""
						player.scoreboard.resetScores(player)
					}
					player.sendMessage("$pluginmsg Your Still hidden :)")
				}
			}
		}
	}
}