package net.crewco.RawGensPlugin.listeners

import com.google.inject.Inject
import net.kyori.adventure.text.Component
import org.bukkit.Server
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent


class TemplateListener @Inject constructor(private val server: Server) : Listener {
	@EventHandler
	fun onPlayerJoin(ev: PlayerJoinEvent) {
		server.broadcast(Component.text("Hello, World25!"))
	}
}