package net.xst.RawGensPlugin.Utils

import net.luckperms.api.node.types.PermissionNode
import net.xst.RawGensPlugin.RawGensCorePlugin
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.api
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.pluginmsg
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.vanishedPlayers
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID

class FreezeManager(private val plugin: RawGensCorePlugin){
	val frozen_players = mutableSetOf<UUID>()
	fun freeze(player:Player,message:String,freezer:Player){
		frozen_players.add(player.uniqueId)
		player.sendMessage("$pluginmsg you have been frozen by ${freezer.displayName} for reason $message")
		val online_players = Bukkit.getOnlinePlayers()
		val permnode = PermissionNode.builder("rawgens.vanish.see").build()
		for(other in online_players){
			val user = api.userManager.getUser(other.uniqueId)
			val group = api.groupManager.getGroup(user!!.primaryGroup)
			if(other != player && !group?.nodes!!.contains(permnode)){
				other.hidePlayer(plugin,player)
			}
		}
	}
	fun unfreeze(player:Player,freezer: Player){
		frozen_players.remove(player.uniqueId)
		player.sendMessage("$pluginmsg you have been unfrozen by ${freezer.displayName}")
		val online_players = Bukkit.getOnlinePlayers()
		for(other in online_players){
			other.showPlayer(plugin,player)
		}

	}
}