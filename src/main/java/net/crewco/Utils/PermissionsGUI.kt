package net.crewco.Utils

import com.google.inject.Inject
import net.crewco.RawGensPlugin.RawGensLP
import net.crewco.RawGensPlugin.RawGensLP.Companion.editing
import net.crewco.RawGensPlugin.RawGensLP.Companion.logo
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.model.group.Group
import net.luckperms.api.node.types.InheritanceNode

class PermissionsGUI @Inject constructor(private val plugin: RawGensLP) : Listener {
	private val luckPerms: LuckPerms = LuckPermsProvider.get()
	private val groupsPerPage = 3 // Number of groups to display per page
	private val groupPages = mutableMapOf<Player, Int>() // To store the current page for each player

	init {
		// Create the initial pages when the plugin loads
		createPages()
	}

	private fun createPages() {
		val onlinePlayers = Bukkit.getOnlinePlayers()
		for (player in onlinePlayers) {
			groupPages[player] = 0 // Initialize each player's group page to 0
		}
	}

	private fun updatePages(player: Player, page: Int, target: Player) {
		val startIndex = page * groupsPerPage
		val endIndex = (startIndex + groupsPerPage).coerceAtMost(luckPerms.groupManager.loadedGroups.size)

		val inventory = Bukkit.createInventory(null, 54, "Groups of ${target.displayName} - Page ${page + 1}")

		// Fill the inventory with black glass panes for a nice background
		val blackGlass = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
		val glassMeta = blackGlass.itemMeta
		glassMeta.setDisplayName(" ") // Empty display name
		blackGlass.itemMeta = glassMeta

		for (i in 0 until 54) {
			inventory.setItem(i, blackGlass)
		}

		// Add a back arrow
		if (page > 0) {
			val backArrow = ItemStack(Material.ARROW)
			val backMeta = backArrow.itemMeta
			backMeta.setDisplayName("Back")
			backArrow.itemMeta = backMeta
			inventory.setItem(45, backArrow)
		}

		// Add a close button
		val closeButton = ItemStack(Material.BARRIER)
		val closeMeta = closeButton.itemMeta
		closeMeta.setDisplayName("Close")
		closeButton.itemMeta = closeMeta
		inventory.setItem(49, closeButton)

		// Fill the inventory with group buttons for the current page
		for (i in startIndex until endIndex) {
			val group = luckPerms.groupManager.loadedGroups.toList()[i]
			val groupItem = createGroupItem(group, player, target)
			inventory.setItem(i - startIndex, groupItem)
		}

		// Add a "Next Page" button as the last item if there are more pages
		if (endIndex < luckPerms.groupManager.loadedGroups.size) {
			val nextPageButton = ItemStack(Material.ARROW)
			val meta = nextPageButton.itemMeta
			meta.setDisplayName("Next Page")
			nextPageButton.itemMeta = meta
			inventory.setItem(53, nextPageButton)
		}

		// Add a "Previous Page" button as the first item if there are previous pages
		if (page > 0) {
			val previousPageButton = ItemStack(Material.ARROW)
			val meta = previousPageButton.itemMeta
			meta.setDisplayName("Previous Page")
			previousPageButton.itemMeta = meta
			inventory.setItem(45, previousPageButton)
		}

		// Store the inventory as the player's current page
		player.openInventory(inventory)
	}

	private fun createGroupItem(group: Group, player: Player, target: Player): ItemStack {
		val itemStack = ItemStack(Material.CHEST)
		val meta = itemStack.itemMeta

		// Set the display name to the group name
		meta.setDisplayName(group.name)

		// Check if the player is in the group
		val user = luckPerms.userManager.getUser(target.uniqueId)
		val nodeToCheck = InheritanceNode.builder(group.name).build()
		if (user != null && user.nodes.contains(nodeToCheck)) {
			meta.lore = listOf("§aEnabled")
		} else {
			meta.lore = listOf("§cDisabled")
		}

		itemStack.itemMeta = meta
		return itemStack
	}

	fun openGUI(player: Player, target: Player) {
		val page = groupPages[player] ?: 0
		updatePages(player, page, target)
	}

	private fun nextPage(player: Player, target: Player) {
		val currentPage = groupPages[player] ?: 0
		val totalPages = (luckPerms.groupManager.loadedGroups.size + groupsPerPage - 1) / groupsPerPage

		if (currentPage < totalPages - 1) { // Check if there are more pages
			groupPages[player] = currentPage + 1
			updatePages(player, currentPage + 1, target)
		}
	}

	private fun previousPage(player: Player, target: Player) {
		val currentPage = groupPages[player] ?: 0

		if (currentPage > 0) { // Check if there are previous pages
			groupPages[player] = currentPage - 1
			updatePages(player, currentPage - 1, target)
		}
	}

	@EventHandler
	fun onInventoryClick(event: InventoryClickEvent) {
		val player = event.whoClicked as? Player ?: return
		val clickedInventory = event.clickedInventory ?: return
		try {
			val target = editing[player.uniqueId]!!
			if (clickedInventory.holder == player.inventory.holder) return // Ignore player's own inventory

			if (event.view.title.startsWith("Groups of")) {
				val currentPage = groupPages[player] ?: 0
				val page = event.view.title.substringAfter("Page ").toIntOrNull()

				if (page != null && currentPage == page - 1) {
					val clickedItem = event.currentItem ?: return

					if (clickedItem.type == Material.CHEST) {
						val groupName = clickedItem.itemMeta?.displayName ?: return

						val user = luckPerms.userManager.getUser(target.uniqueId)
						val nodeToCheck = InheritanceNode.builder(groupName).build()

						if (user != null) {
							if (user.nodes.contains(nodeToCheck)) {
								// Player is already a member of the group, remove them from the group
								user.data().remove(nodeToCheck)
								player.sendMessage("$logo Removed ${target.displayName} from the group $groupName")
								target.sendMessage("$logo ${player.displayName} Removed you from the group $groupName")
							} else {
								// Player is not a member of the group, add them to the group
								val node = InheritanceNode.builder(groupName).build()
								user.data().add(node)
								player.sendMessage("$logo Added ${target.displayName} to the group $groupName")
								target.sendMessage("$logo ${player.displayName} Added you to the group $groupName")
							}

							luckPerms.userManager.saveUser(user)
							// Update the clicked item's lore based on the player's current permissions
							updateGroupItemLore(clickedItem, target)
						}
					} else if (clickedItem.type == Material.ARROW) {
						event.isCancelled = true // Cancel the event to prevent item clicking

						if (event.slot == 45) {
							// Previous Page
							previousPage(player, target)
						} else if (event.slot == 53) {
							// Next Page
							nextPage(player, target)
						}
					} else if (clickedItem.type == Material.BARRIER) {
						event.isCancelled = true // Cancel the event to prevent item clicking
						// Close the GUI
						player.closeInventory()
					}
				}
			}
		} catch (_: Exception) {

		}
	}

	@EventHandler
	fun onPlayerJoin(event: PlayerJoinEvent) {
		val player = event.player
		groupPages[player] = 0 // Initialize the player's group page to 0 when they join
	}

	@EventHandler
	fun onPlayerQuit(event: PlayerQuitEvent) {
		val player = event.player
		groupPages.remove(player) // Remove the player's group page when they quit
	}

	private fun updateGroupItemLore(itemStack: ItemStack, player: Player) {
		val meta = itemStack.itemMeta ?: return
		val groupName = meta.displayName ?: return
		val user = luckPerms.userManager.getUser(player.uniqueId)
		val nodeToCheck = InheritanceNode.builder(groupName).build()

		if (user != null && user.nodes.contains(nodeToCheck)) {
			meta.lore = listOf("§aEnabled")
		} else {
			meta.lore = listOf("§cDisabled")
		}

		itemStack.itemMeta = meta
	}
}
