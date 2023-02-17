package net.xst.RawGensPlugin.Utils

import net.xst.RawGensPlugin.RawGensCorePlugin
import org.bukkit.entity.Player
import java.util.*

class PlayerVanishState(var isVanished: Boolean)

class VanishManager(val plugin: RawGensCorePlugin){
	private val playerStates = mutableMapOf<UUID, PlayerVanishState>()

	fun toggleVanish(player: Player) {
		var playerState = playerStates.getOrPut(player.uniqueId) { PlayerVanishState(false) }
		if(playerState.isVanished){
			player.isInvisible = playerState.isVanished
			player.isInvulnerable = playerState.isVanished
		}
	}

	fun isVanished(player: Player): Boolean {
		return playerStates[player.uniqueId]?.isVanished ?: false
	}
}

