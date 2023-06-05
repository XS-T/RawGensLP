package net.crewco.common.util

import org.bukkit.plugin.Plugin
import java.util.concurrent.Executor

class SpigotExecutor(val plugin: Plugin) : Executor {
	override fun execute(runnable: Runnable) {
		plugin.server.scheduler.runTaskAsynchronously(plugin, runnable)
	}
}