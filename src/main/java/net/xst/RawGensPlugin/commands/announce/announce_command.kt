package net.xst.RawGensPlugin.commands.announce

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class announce_command {
	@CommandMethod("announce <message>")
	@CommandDescription("announces a message")
	@CommandPermission("rawgens.command.announce")
	suspend fun announce(player: Player,@Argument("message") message:Array<String>) {
		val online_players = Bukkit.getOnlinePlayers()
		val broadcast = ChatColor.translateAlternateColorCodes('&',"&7[&x&f&f&0&0&0&0A&x&f&5&0&0&0&0n&x&e&b&0&0&0&0n&x&e&1&0&0&0&0o&x&d&7&0&0&0&0u&x&c&d&0&0&0&0c&x&c&3&0&0&0&0e&x&b&9&0&0&0&0m&x&a&f&0&0&0&0e&x&a&5&0&0&0&0n&x&9&b&0&0&0&0t&7]")
		var parsedmessage = message.joinToString(" ")
		if(parsedmessage.contains('&')){
			parsedmessage = ChatColor.translateAlternateColorCodes('&',parsedmessage)
		}
		for(other in online_players){
			other.sendMessage("$broadcast $parsedmessage")
		}
	}
}