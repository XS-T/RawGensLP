package net.xst.JailHomePlugin

import net.xst.common.JailHomePlugin
import net.xst.JailHomePlugin.commands.TemplateCommand
import net.xst.JailHomePlugin.listeners.TemplateListener

class JailHomePlugin : JailHomePlugin() {
	override suspend fun onEnableAsync() {
		super.onEnableAsync()

		registerListeners(
			TemplateListener::class
		)

		registerCommands(TemplateCommand::class)
	}

	override suspend fun onDisableAsync() {
		super.onDisableAsync()
	}
}