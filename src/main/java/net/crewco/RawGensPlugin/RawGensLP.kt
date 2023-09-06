package net.crewco.RawGensPlugin

import InventoryClickListener
import LuckPermsGui
import net.crewco.RawGensPlugin.listeners.GUI.PlayerInteractListener
//import net.crewco.Utils.GUI
import net.crewco.common.CrewCoPlugin

class RawGensLP : CrewCoPlugin() {
	companion object{
		lateinit var plugin:RawGensLP
			private set

		lateinit var gui:LuckPermsGui
	}
	override suspend fun onEnableAsync() {
		super.onEnableAsync()

		//Inits
		plugin = this
		gui = LuckPermsGui()

		//Config
		plugin.config.options().copyDefaults()
		plugin.saveDefaultConfig()

		//Register Events
		registerListeners(InventoryClickListener::class,PlayerInteractListener::class,LuckPermsGui::class)

	}

	override suspend fun onDisableAsync() {
		super.onDisableAsync()
	}
}