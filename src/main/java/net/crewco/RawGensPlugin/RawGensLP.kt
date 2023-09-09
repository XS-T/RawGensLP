package net.crewco.RawGensPlugin

import InventoryClickListener
import net.crewco.RawGensPlugin.listeners.GUI.InventoryCloseListener
import net.crewco.RawGensPlugin.listeners.GUI.PlayerInteractListener
import net.crewco.Utils.LuckPermsGui
import net.crewco.Utils.PermissionsGUI
//import net.crewco.Utils.GUI
import net.crewco.common.CrewCoPlugin
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.UUID
import java.util.logging.Level
import kotlin.math.log

class RawGensLP : CrewCoPlugin() {
	companion object{
		lateinit var plugin:RawGensLP
			private set

		lateinit var gui: LuckPermsGui
		lateinit var lpgui:PermissionsGUI
		lateinit var editing:MutableMap<UUID,Player>
		lateinit var logo:String
	}
	override suspend fun onEnableAsync() {
		super.onEnableAsync()

		//Inits
		plugin = this
		gui = LuckPermsGui()
		lpgui = PermissionsGUI(this)
		editing = mutableMapOf()
		logo = ChatColor.translateAlternateColorCodes('&',"&7[&x&b&4&b&4&b&4R&x&8&b&8&b&8&ba&x&6&2&6&2&6&2w&x&8&6&6&3&4&6G&x&a&a&6&3&2&9e&x&a&a&6&3&2&9n&x&a&a&6&3&2&9s&7]>")
		//Config
		plugin.config.options().copyDefaults()
		plugin.saveDefaultConfig()

		//Register Events
		registerListeners(PlayerInteractListener::class,LuckPermsGui::class,PermissionsGUI(this)::class,InventoryCloseListener::class)

	}

	override suspend fun onDisableAsync() {
		super.onDisableAsync()
	}
}