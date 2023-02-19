package net.xst.RawGensPlugin.commands.Freeze.listeners

import com.google.inject.Inject
import net.luckperms.api.node.types.PermissionNode
import net.xst.RawGensPlugin.RawGensCorePlugin
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.api
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.freezeManager
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.vanishedPlayers
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class FreezeJoinListener @Inject constructor(private val plugin: RawGensCorePlugin):Listener {
	@EventHandler
	fun onFreeze(e:PlayerJoinEvent){
		val player = e.player
		val online_players = Bukkit.getOnlinePlayers()
		val frozen_players = freezeManager.frozen_players
		val vanishedPlayers = vanishedPlayers
		for(fp in frozen_players){
			val user = api.userManager.getUser(player.uniqueId)
			val group = api.groupManager.getGroup(user!!.primaryGroup)
			val permnode = PermissionNode.builder("rawgens.vanish.see").build()
			//Hides the frozen Players From Normal Players
			if(fp != player.uniqueId && !group!!.nodes.contains(permnode)){
				player.hidePlayer(plugin,Bukkit.getPlayer(fp)!!)
			}else{
				//Hides the Frozen Players on Rejoin
				for(other in online_players){
					val user2 = api.userManager.getUser(other.uniqueId)
					val group2 = api.groupManager.getGroup(user2!!.primaryGroup)
					if(other != player && !group2?.nodes!!.contains(permnode)){
						other.hidePlayer(plugin,Bukkit.getPlayer(fp)!!)
					//Shows the Frozen players to thoes with permisison	
					}else if(group?.nodes!!.contains(permnode)){
						player.showPlayer(plugin,Bukkit.getPlayer(fp)!!)
					}
				}
			}

		}
	}
}