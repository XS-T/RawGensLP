package net.crewco.Utils

import com.google.inject.Inject
import net.crewco.RawGensPlugin.RawGensLP
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin
import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.model.user.User
import net.luckperms.api.node.Node
import org.bukkit.OfflinePlayer

class PermissionsGUI @Inject constructor(private val plugin: RawGensLP) : Listener {
	private val luckPerms: LuckPerms = LuckPermsProvider.get()

	fun openGUI(player:Player) {
		val user: User = luckPerms.userManager.getUser(player.uniqueId) ?: return
		val permissions: Set<String> = user.nodes
			.filter { it.value }
			.map { it.key }
			.toSet()
		val primaryGroup: String = user.primaryGroup

		val gui: Inventory = Bukkit.createInventory(null, 54, "Permissions GUI of ${player.displayName}")

		// Add permissions to the GUI
		var index = 0
		for (permission in permissions) {
			if (index >= 45) break // Limit the number of permissions shown

			val item = ItemStack(Material.BOOK)
			val meta: ItemMeta = item.itemMeta

			// Set the display name as a string
			meta.setDisplayName(permission)
			item.itemMeta = meta

			gui.setItem(index, item)
			index++
		}

		// Add the primary group to the GUI
		val groupItem = ItemStack(Material.CHEST)
		val groupMeta: ItemMeta = groupItem.itemMeta

		// Set the display name as a string
		groupMeta.setDisplayName(primaryGroup)
		groupItem.itemMeta = groupMeta

		gui.setItem(45, groupItem) // Place it in a specific slot

		// Add buttons for actions (e.g., grant/revoke permissions)

		player.openInventory(gui)
	}

	@EventHandler
	fun onInventoryClick(event: InventoryClickEvent) {
		val player = event.whoClicked as? Player ?: return
		val clickedItem = event.currentItem ?: return

		if (event.view.title == "Permissions GUI") {
			event.isCancelled = true // Prevent item moving or other inventory actions

			val clickedDisplayName = clickedItem.itemMeta?.displayName ?: return

			// Check if the clicked item is a permission node
			if (clickedItem.type == Material.BOOK) {
				// Check if the player already has the permission
				val user = luckPerms.userManager.getUser(player.uniqueId) ?: return
				val permissionNode = clickedDisplayName

				// Create a Node instance to check
				val nodeToCheck = Node.builder(permissionNode).build()

				if (user.nodes.contains(nodeToCheck)) {
					// Permission node already exists, revoke it
					user.data().remove(nodeToCheck)
					player.sendMessage("Revoked permission: $permissionNode")
				} else {
					// Permission node doesn't exist, grant it
					val node = Node.builder(permissionNode).build()
					user.data().add(node)
					player.sendMessage("Granted permission: $permissionNode")
				}

				// Save changes to LuckPerms
				luckPerms.userManager.saveUser(user)
			}
		}
	}
}
