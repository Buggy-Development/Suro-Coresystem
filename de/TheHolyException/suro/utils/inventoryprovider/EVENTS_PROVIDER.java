package de.TheHolyException.suro.utils.inventoryprovider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.TheHolyException.suro.utils.ItemManager;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

public class EVENTS_PROVIDER implements InventoryProvider {

	private HashMap<Player, Integer> lg_selection = new HashMap<Player, Integer>();
	
	@Override
	public void init(Player player, InventoryContents contents) {
		contents.fillBorders(ClickableItem.empty(new ItemManager(Material.PURPLE_STAINED_GLASS_PANE).setDisplayName("").build()));
		contents.add(ClickableItem.of(new ItemManager(Material.FEATHER).setDisplayName("§e§lLow Gravity").addLoreLines("Verringere Die Gravitation der Spieler"
				,"§e§l*§6Stufe §bI §7(§e1:2,5§7)§e§l*"
				,"§7§l*§6Stufe §bII §7(§e1:3,5§7)§7§l*"
				,"§7§l*§6Stufe §bIII §7(§e1:5§7)§7§l*"
				,"§7§l*§6Stufe §bIV §7(§e1:7§7)§7§l*").build(), event -> {
			if (!lg_selection.containsKey(player)) lg_selection.put(player, 1);
			int selection = lg_selection.get(player);
			if (event.getClick().equals(ClickType.SHIFT_LEFT)) {
				selection = (selection >= 4 ? 0 : selection+1);
				setLoreSelection(event.getCurrentItem(), selection);
			} else if (event.getClick().equals(ClickType.SHIFT_RIGHT)) {
				selection = (selection <= 1 ? 0 : selection-1);
				setLoreSelection(event.getCurrentItem(), selection);
			} else if (event.getClick().equals(ClickType.DOUBLE_CLICK)) {
			}
			
			
			
		}));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		
	}
	
	
	private void setLoreSelection(ItemStack item, int selection) {
		ItemMeta meta = item.getItemMeta();
		List<String> endlore = new ArrayList<String>();
		int line_cnt = 1;
		for (String line : meta.getLore()) {
			if (line.contains("*")) {
				if (line_cnt == selection) {
					line = line.replace("§7§l*", "§e§l*");
				} else if (line.contains("§e§l*")) line = line.replace("§e§l*", "§7§l*");
				line_cnt++;
			}
			endlore.add(line);
		}
		
		meta.setLore(endlore);
		item.setItemMeta(meta);
	}
	
	

}
