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
import org.bukkit.inventory.meta.SkullMeta

class InventoryClickListener @Inject constructor(private val plugin: RawGensLP) : Listener {
/*
	@EventHandler
	fun onInventoryClick(event: InventoryClickEvent) {
		val player = event.whoClicked as? Player ?: return
		val clickedInventory = event.clickedInventory ?: return

		if (clickedInventory.holder == player.inventory.holder) return // Ignore player's own inventory

		if (event.view.title.startsWith("Example GUI - Page ")) {
			event.isCancelled = true // Cancel the event to prevent item dragging

			val currentPage = playerPages[player] ?: 0
			val page = pages.indexOf(clickedInventory)

			if (page >= 0 && currentPage == page) {
				val clickedItem = event.currentItem ?: return

				if (clickedItem.type == Material.PLAYER_HEAD) {
					// Handle clicking on player heads here
					// You can use the item's metadata to identify the player and perform actions
					player.sendMessage("You clicked on a player head!")
				} else if (clickedItem.type == Material.ARROW) {
					nextPage(player)
				}
			}
		}
	}*/
}
