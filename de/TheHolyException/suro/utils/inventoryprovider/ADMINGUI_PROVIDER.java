package de.TheHolyException.suro.utils.inventoryprovider;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import de.TheHolyException.suro.utils.ItemManager;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

public class ADMINGUI_PROVIDER implements InventoryProvider {

	private SmartInventory events = SmartInventory.builder()
			.id("suroEvents")
			.closeable(true)
			.provider(new EVENTS_PROVIDER())
			.size(3, 9)
			.build();
	
	
	@Override
	public void init(Player player, InventoryContents contents) {
		contents.fillBorders(ClickableItem.empty(new ItemManager(Material.RED_STAINED_GLASS_PANE).setDisplayName("").build()));
		contents.add(ClickableItem.of(new ItemManager(Material.CAKE).setDisplayName("§5Events").addLoreLines("§7Starte Events").build(), event -> {
			events.open(player);
		}));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
	}

}
