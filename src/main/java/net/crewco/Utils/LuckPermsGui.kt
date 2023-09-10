package net.crewco.Utils

import net.crewco.RawGensPlugin.RawGensLP.Companion.editing
import net.crewco.RawGensPlugin.RawGensLP.Companion.logo
import net.crewco.RawGensPlugin.RawGensLP.Companion.lpgui
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class LuckPermsGui : Listener {

	private val playersPerPage = 18 // Number of players to display per page
	private val playerPages = mutableMapOf<Player, Int>() // To store the current page for each player

	init {
		// Create the initial pages when the plugin loads
		createPages()
	}

	private fun createPages() {
		val onlinePlayers = Bukkit.getOnlinePlayers()
		for (player in onlinePlayers) {
			playerPages[player] = 0 // Initialize each player's page to 0
		}
	}

	private fun updatePages(player: Player) {
		val currentPage = playerPages[player] ?: 0
		val startIndex = currentPage * playersPerPage
		val onlinePlayers = Bukkit.getOnlinePlayers().toList()
		val endIndex = Math.min(startIndex + playersPerPage, onlinePlayers.size)

		val inventory = Bukkit.createInventory(null, 27, "RawGensPerms GUI - Page ${currentPage + 1}")

		// Fill the inventory with black glass panes for a nice background
		val blackGlass = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
		val glassMeta = blackGlass.itemMeta
		glassMeta.setDisplayName(" ") // Empty display name
		blackGlass.itemMeta = glassMeta

		for (i in 0 until 27) {
			inventory.setItem(i, blackGlass)
		}

		// Add a back arrow
		if (currentPage > 0) {
			val backArrow = ItemStack(Material.ARROW)
			val backMeta = backArrow.itemMeta
			backMeta.setDisplayName("Back")
			backArrow.itemMeta = backMeta
			inventory.setItem(18, backArrow)
		}

		// Add a close button
		val closeButton = ItemStack(Material.BARRIER)
		val closeMeta = closeButton.itemMeta
		closeMeta.setDisplayName("Close")
		closeButton.itemMeta = closeMeta
		inventory.setItem(22, closeButton)

		// Fill the inventory with player heads for the current page
		for (i in startIndex until endIndex) {
			val targetPlayer = onlinePlayers[i]
			val playerHead = createPlayerHead(targetPlayer)
			inventory.setItem(i - startIndex, playerHead)
		}

		// Add a "Next Page" button as the last item if there are more pages
		if (endIndex < onlinePlayers.size) {
			val nextPageButton = ItemStack(Material.ARROW)
			val meta = nextPageButton.itemMeta
			meta.setDisplayName("Next Page")
			nextPageButton.itemMeta = meta
			inventory.setItem(26, nextPageButton)
		}

		// Store the inventory as the player's current page
		player.openInventory(inventory)
	}

	private fun createPlayerHead(targetPlayer: Player): ItemStack {
		val itemStack = ItemStack(Material.PLAYER_HEAD)
		val meta = itemStack.itemMeta as SkullMeta

		// Set the owner of the skull
		meta.owningPlayer = targetPlayer

		// Set the display name if needed
		meta.setDisplayName(targetPlayer.displayName)

		itemStack.itemMeta = meta
		return itemStack
	}

	@EventHandler
	fun onInventoryClick(event: InventoryClickEvent) {
		val player = event.whoClicked as? Player ?: return
		val clickedInventory = event.clickedInventory ?: return

		if (clickedInventory.holder == player.inventory.holder) return // Ignore player's own inventory

		if (event.view.title.startsWith("RawGensPerms GUI")) {
			event.isCancelled = true // Cancel the event to prevent item dragging

			val currentPage = playerPages[player] ?: 0
			val page = event.view.title.substringAfter("Page ").toIntOrNull()

			if (page != null && currentPage == page - 1) {
				val clickedItem = event.currentItem ?: return

				if (clickedItem.type == Material.PLAYER_HEAD) {
					// Handle clicking on player heads here
					val offlinePlayerName = clickedItem.itemMeta.displayName
					val offlinePlayer = Bukkit.getPlayer(offlinePlayerName)

					if (offlinePlayer != null && offlinePlayer.isOnline) {
						if (!editing.containsValue(offlinePlayer)) {
							editing[player.uniqueId] = offlinePlayer
							lpgui.openGUI(player)
						} else {
							player.sendMessage("$logo Someone is already editing this player")
						}
					} else {
						player.sendMessage("$logo The Player is not online")
					}
				}
			}
		}

		// Define currentPage here based on playerPages
		val currentPage = playerPages[player] ?: 0

		// Handle the buttons outside of the if block
		val clickedItem = event.currentItem ?: return

		if (clickedItem.type == Material.ARROW && event.slot == 26) {
			nextPage(player)
		} else if (clickedItem.type == Material.BARRIER && event.slot == 22) {
			// Handle closing the GUI here
			player.closeInventory()
			playerPages.remove(player)
		} else if (clickedItem.type == Material.ARROW && event.slot == 18) {
			// Handle going back a page here
			previousPage(player)
		}
	}

	@EventHandler
	fun onPlayerJoin(event: PlayerJoinEvent) {
		val player = event.player
		playerPages[player] = 0 // Initialize the player's page to 0 when they join
	}

	@EventHandler
	fun onPlayerQuit(event: PlayerQuitEvent) {
		val player = event.player
		playerPages.remove(player) // Remove the player's page when they quit
	}

	// Add a nextPage function to go to the next page
	private fun nextPage(player: Player) {
		val currentPage = playerPages[player] ?: 0
		val nextPageIndex = currentPage + 1

		if (nextPageIndex < (Bukkit.getOnlinePlayers().size + playersPerPage - 1) / playersPerPage) {
			playerPages[player] = nextPageIndex
			updatePages(player)
		}
	}
	private fun previousPage(player: Player) {
		val currentPage = playerPages.getValue(player)
		if (currentPage > 0) {
			val previousPageIndex = currentPage - 1
			playerPages[player] = previousPageIndex
			updatePages(player)
		}else{
			player.sendMessage("Nah")
		}
	}

	// Add an openGui function to open the GUI for a specific player
	fun openGui(player: Player) {
		updatePages(player)
	}
}
