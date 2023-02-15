package net.xst.RawGensPlugin.Utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import net.xst.RawGensPlugin.RawGensCorePlugin
import java.io.File
import java.util.*

class WarnPlayer(val playerUuid: UUID, val reason: String, val date: Date)

class WarnsManager(val plugin: RawGensCorePlugin) {
	private val gson = GsonBuilder().setPrettyPrinting().create()
	private val warnsDir = File(plugin.dataFolder, "warns")

	init {
		if (!warnsDir.exists()) {
			warnsDir.mkdirs()
		}
	}

	fun addWarning(playerUuid: UUID, reason: String, date: Date) {
		val playerFile = File(warnsDir, "$playerUuid.json")
		val warnList = if (playerFile.exists()) {
			gson.fromJson<MutableList<WarnPlayer>>(playerFile.readText(), object : TypeToken<List<WarnPlayer>>() {}.type) ?: mutableListOf()
		} else {
			mutableListOf()
		}

		warnList.add(WarnPlayer(playerUuid, reason, date))

		playerFile.writeText(gson.toJson(warnList))
	}

	fun getWarnings(playerUuid: UUID): List<WarnPlayer> {
		val playerFile = File(warnsDir, "$playerUuid.json")
		val warnList = if (playerFile.exists()) {
			gson.fromJson<List<WarnPlayer>>(playerFile.readText(), object : TypeToken<List<WarnPlayer>>() {}.type) ?: emptyList()
		} else {
			emptyList()
		}

		return warnList
	}

	fun ClearWarnings(playerUuid: UUID) {
		val playerFile = File(warnsDir, "$playerUuid.json")
		if (playerFile.exists()) {
			playerFile.delete()
		}
	}

	fun removeWarning(playerUuid: UUID, index: Int) {
		val playerFile = File(warnsDir, "$playerUuid.json")
		if (playerFile.exists()) {
			val warnList = gson.fromJson<MutableList<WarnPlayer>>(playerFile.readText(), object : TypeToken<MutableList<WarnPlayer>>() {}.type)
			if (warnList.isNotEmpty() && index < warnList.size) {
				warnList.removeAt(index)
				playerFile.writeText(gson.toJson(warnList))
			}
		}
	}
}




