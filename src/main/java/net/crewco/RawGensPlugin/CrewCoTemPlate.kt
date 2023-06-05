package net.crewco.RawGensPlugin

import net.crewco.common.CrewCoPlugin

class CrewCoTemPlate : CrewCoPlugin() {
	companion object{
		lateinit var plugin:CrewCoTemPlate
			private set
	}
	override suspend fun onEnableAsync() {
		super.onEnableAsync()

		//Inits
		plugin = this

		//Config
		plugin.config.options().copyDefaults()
		plugin.saveDefaultConfig()

	}

	override suspend fun onDisableAsync() {
		super.onDisableAsync()
	}
}