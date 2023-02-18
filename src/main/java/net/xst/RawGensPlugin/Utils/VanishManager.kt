package net.xst.RawGensPlugin.Utils

import net.xst.RawGensPlugin.RawGensCorePlugin
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.pluginmsg
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.vanishedPlayers
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*



class VanishManager(val plugin: RawGensCorePlugin){
	fun isVanished(player: Player): Boolean {
		return vanishedPlayers.contains(player.uniqueId)
	}
	fun toggle_vanish(player: Player){
		val is_vanished = isVanished(player)
		if(is_vanished){
			vanishedPlayers.remove(player.uniqueId)
			val online_players = Bukkit.getOnlinePlayers()
			for(other in online_players){
				other.showPlayer(plugin,player)
			}
			player.sendMessage("$pluginmsg You are now visible")
		}else{
			val online_players = Bukkit.getOnlinePlayers()
			for(other in online_players){
				if (other != player) {
					other.hidePlayer(plugin, player)
				}
			}
			player.sendMessage("$pluginmsg you are now hidden")
			vanishedPlayers.add(player.uniqueId)
		}
	}

}

