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
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.model.group.Group
import net.luckperms.api.node.types.InheritanceNode

class PermissionsGUI @Inject constructor(private val plugin: RawGensLP) : Listener {
	private val luckPerms: LuckPerms = LuckPermsProvider.get()
	private val groupsPerPage = 44 // Number of groups to display per page
	private val groupPages = mutableMapOf<Player, Int>()

	init {
		createPages()
	}

	private fun createPages() {
		val onlinePlayers = Bukkit.getOnlinePlayers()
		onlinePlayers.forEach { player ->
			groupPages[player] = 0
		}
	}

	private fun updatePages(player: Player) {
		val currentPage = groupPages[player] ?: 0
		val groups = luckPerms.groupManager.loadedGroups.toList()
		val totalPages = (groups.size + groupsPerPage - 1) / groupsPerPage
		val startIndex = currentPage * groupsPerPage
		val endIndex = (startIndex + groupsPerPage).coerceAtMost(groups.size)

		val inventory = Bukkit.createInventory(null, 54, "Permissions GUI - Page ${currentPage + 1}")

		val blackGlass = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
		val glassMeta = blackGlass.itemMeta
		glassMeta.setDisplayName(" ")
		blackGlass.itemMeta = glassMeta

		(0 until 54).forEach { i ->
			inventory.setItem(i, blackGlass)
		}

		if (currentPage > 0) {
			val backArrow = ItemStack(Material.ARROW)
			val backMeta = backArrow.itemMeta
			backMeta.setDisplayName("Back")
			backArrow.itemMeta = backMeta
			inventory.setItem(45, backArrow)
		}

		val closeButton = ItemStack(Material.BARRIER)
		val closeMeta = closeButton.itemMeta
		closeMeta.setDisplayName("Close")
		closeButton.itemMeta = closeMeta
		inventory.setItem(49, closeButton)

		(startIndex until endIndex).forEachIndexed { index, i ->
			val group = groups[i]
			val groupItem = createGroupItem(group, player)
			inventory.setItem(index, groupItem)
		}

		if (currentPage < totalPages - 1) {
			val nextPageButton = ItemStack(Material.ARROW)
			val meta = nextPageButton.itemMeta
			meta.setDisplayName("Next Page")
			nextPageButton.itemMeta = meta
			inventory.setItem(53, nextPageButton)
		}

		player.openInventory(inventory)
	}

	private fun createGroupItem(group: Group, player: Player): ItemStack {
		val itemStack = ItemStack(Material.CHEST)
		val meta = itemStack.itemMeta
		meta.setDisplayName(group.name)

		val user = luckPerms.userManager.getUser(player.uniqueId)
		val nodeToCheck = InheritanceNode.builder(group.name).build()

		if (user != null) {
			if (user.nodes.contains(nodeToCheck)) {
				meta.lore = listOf("§aEnabled")
			} else {
				meta.lore = listOf("§cDisabled")
			}
		}

		itemStack.itemMeta = meta
		return itemStack
	}

	fun openGUI(player: Player) {
		updatePages(player)
	}

	private fun nextPage(player: Player) {
		val currentPage = groupPages[player] ?: 0
		val groups = luckPerms.groupManager.loadedGroups.toList()
		val totalPages = (groups.size + groupsPerPage - 1) / groupsPerPage

		if (currentPage < totalPages - 1) {
			groupPages[player] = currentPage + 1
			updatePages(player)
			player.updateInventory()
		} else {
			player.sendMessage("No more pages.")
		}
	}

	private fun previousPage(player: Player) {
		val currentPage = groupPages[player] ?: 0

		if (currentPage > 0) {
			groupPages[player] = currentPage - 1
			updatePages(player)
		} else {
			player.sendMessage("You are already on the first page.")
		}
	}

	@EventHandler
	fun onInventoryClick(event: InventoryClickEvent) {
		val player = event.whoClicked as? Player ?: return
		val clickedInventory = event.clickedInventory ?: return

		if (clickedInventory.holder == player.inventory.holder) return

		if (event.view.title.startsWith("Permissions GUI")) {
			event.isCancelled = true

			val currentPage = groupPages[player] ?: 0
			val page = event.view.title.substringAfter("Page ").toIntOrNull()

			if (page != null && currentPage == page - 1) {
				val clickedItem = event.currentItem ?: return

				if (clickedItem.type == Material.CHEST) {
					val groupName = clickedItem.itemMeta?.displayName ?: return
					val group = luckPerms.groupManager.getGroup(groupName) ?: return

					val user = luckPerms.userManager.getUser(player.uniqueId)
					val nodeToCheck = InheritanceNode.builder(groupName).build()
					if (user != null) {
						if (user.nodes.contains(nodeToCheck)) {
							user.data().remove(nodeToCheck)
							player.sendMessage("$logo Removed yourself from the group $groupName")
						} else {
							val node = InheritanceNode.builder(groupName).build()
							user.data().add(node)
							player.sendMessage("$logo Added yourself to the group $groupName")
						}

						luckPerms.userManager.saveUser(user)
						updateGroupItemLore(clickedItem, player)
					}
				} else if (clickedItem.type == Material.ARROW && event.slot == 53) {
					nextPage(player)
				} else if (clickedItem.type == Material.BARRIER && event.slot == 49) {
					player.closeInventory()
					groupPages.remove(player)
				} else if (clickedItem.type == Material.ARROW && event.slot == 45) {
					previousPage(player)
				}
			}
		} else {
			//player.sendMessage("Name is:${event.view.title}")
		}
	}

	@EventHandler
	fun onPlayerJoin(event: PlayerJoinEvent) {
		val player = event.player
		groupPages[player] = 0
	}

	@EventHandler
	fun onPlayerQuit(event: PlayerQuitEvent) {
		val player = event.player
		groupPages.remove(player)
	}

	@EventHandler
	fun onInventoryClose(event: InventoryCloseEvent) {
		val player = event.player
		val name = event.view.title
		if (name.startsWith("Permissions GUI")) {
			groupPages.remove(player)
		}
	}

	private fun updateGroupItemLore(itemStack: ItemStack, player: Player) {
		val meta = itemStack.itemMeta ?: return
		val groupName = meta.displayName ?: return
		val user = luckPerms.userManager.getUser(player.uniqueId)
		val nodeToCheck = InheritanceNode.builder(groupName).build()

		if (user != null) {
			if (user.nodes.contains(nodeToCheck)) {
				meta.lore = listOf("§aEnabled")
			} else {
				meta.lore = listOf("§cDisabled")
			}
		}

		itemStack.itemMeta = meta
	}
}