package net.xst.RawGensPlugin.Utils

import net.luckperms.api.node.types.PermissionNode
import net.xst.RawGensPlugin.RawGensCorePlugin
import net.xst.RawGensPlugin.RawGensCorePlugin.Companion.api
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
				other.sendMessage("${ChatColor.YELLOW}${player.displayName} joined the game")
			}
			player.sendMessage("$pluginmsg You are now visible")
			player.scoreboard.getTeam("players")?.addEntry(player.displayName)
		}else{
			val online_players = Bukkit.getOnlinePlayers()
			for(other in online_players){
				//See Vanished
				val user = api.userManager.getUser(other.uniqueId)
				val group = api.groupManager.getGroup(user!!.primaryGroup)
				val permnode = PermissionNode.builder("rawgens.vanish.see").build()
				player.scoreboard.resetScores(player)
				if (other != player && !group?.nodes!!.contains(permnode)) {
					other.hidePlayer(plugin, player)
					other.sendMessage("${ChatColor.YELLOW}${player.displayName} left the game")
				}
			}
			player.sendMessage("$pluginmsg you are now hidden")
			vanishedPlayers.add(player.uniqueId)
		}
	}

}

