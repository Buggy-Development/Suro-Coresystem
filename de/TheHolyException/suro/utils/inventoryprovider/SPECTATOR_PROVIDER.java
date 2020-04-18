package de.TheHolyException.suro.utils.inventoryprovider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Base64;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.utils.Inventorys;
import de.TheHolyException.suro.utils.ItemManager;
import de.TheHolyException.suro.utils.JSONReader;
import de.TheHolyException.suro.utils.SUROPlayer;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;

public class SPECTATOR_PROVIDER implements InventoryProvider {

	@Override
	public void init(Player player, InventoryContents contents) {
		
		Pagination pageination = contents.pagination();
		
		List<ClickableItem> items = new ArrayList<ClickableItem>();
		Map<UUID, String> usernamecache = CoreSpigot.getInstance().getMySQLManager().getPlayerDataManager().getAllUsersUUIDKey();
		for (SUROPlayer splayer : CoreSpigot.getInstance().getSuroEngine().getAllAlivePlayers()) {
			Player bukkitplayer = Bukkit.getPlayer(splayer.getUniqueId());
			if (bukkitplayer != null && bukkitplayer.isOnline()) {
				ItemStack skull = new ItemManager(getPlayerSkull(bukkitplayer)).setDisplayName(usernamecache.get(splayer.getUniqueId())).build();
				items.add(ClickableItem.of(skull, event -> {
					player.teleport(bukkitplayer);
				}));
			}
		}
		
		ClickableItem[] citems = new ClickableItem[items.size()];
		for (int i = 0; i < items.size(); i++) citems[i] = items.get(i);
		
		pageination.setItems(citems);
		pageination.setItemsPerPage(14);
		pageination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1,4));

		contents.fillBorders(ClickableItem.empty(new ItemManager(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("").build()));
		
		contents.set(3, 0, ClickableItem.of(new ItemManager(Material.ARROW).setDisplayName("§e§l<- §6§lVorherige Seite").build(), event -> Inventorys.spectator.open(player, pageination.next().getPage())));
		contents.set(3, 8, ClickableItem.of(new ItemManager(Material.ARROW).setDisplayName("§6§l Nächste Seite §e§l->").build(), event -> Inventorys.spectator.open(player, pageination.next().getPage())));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
	}
	
	
	public ItemStack getPlayerSkull(Player player) {
		JSONReader headcache = JSONReader.getJSONReader("skull_cache");
        String url = "http://textures.minecraft.net/texture/" + player.getUniqueId().toString();
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (url.isEmpty()) return head;

        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);        
        byte[] data;
        if (headcache.containsKey(player.getUniqueId().toString())) {
        	data = headcache.getString(player.getUniqueId().toString(), null).getBytes();
        } else {
        	data = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        	headcache.set(player.getUniqueId().toString(), new String(data));
        }
        
        gameProfile.getProperties().put("textures", new Property("textures", new String(data)));
        Field field;
        try {
            field = skullMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(skullMeta, gameProfile);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        head.setItemMeta(skullMeta);
        return head;
	}

}
