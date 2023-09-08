package net.crewco.RawGensPlugin

import InventoryClickListener
import LuckPermsGui
import net.crewco.RawGensPlugin.listeners.GUI.PlayerInteractListener
import net.crewco.Utils.PermissionsGUI
//import net.crewco.Utils.GUI
import net.crewco.common.CrewCoPlugin

class RawGensLP : CrewCoPlugin() {
	companion object{
		lateinit var plugin:RawGensLP
			private set

		lateinit var gui:LuckPermsGui
		lateinit var lpgui:PermissionsGUI
	}
	override suspend fun onEnableAsync() {
		super.onEnableAsync()

		//Inits
		plugin = this
		gui = LuckPermsGui()
		lpgui = PermissionsGUI(this)
		//Config
		plugin.config.options().copyDefaults()
		plugin.saveDefaultConfig()

		//Register Events
		registerListeners(InventoryClickListener::class,PlayerInteractListener::class,LuckPermsGui::class,PermissionsGUI(this)::class)

	}

	override suspend fun onDisableAsync() {
		super.onDisableAsync()
	}
}