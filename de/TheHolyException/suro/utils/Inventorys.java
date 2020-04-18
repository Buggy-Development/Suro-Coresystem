package de.TheHolyException.suro.utils;

import de.TheHolyException.suro.utils.inventoryprovider.SPECTATOR_PROVIDER;
import fr.minuskube.inv.SmartInventory;

public class Inventorys {
	
	public static SmartInventory spectator = SmartInventory.builder()
			.id("spectate")
			.size(4, 9)
			.provider(new SPECTATOR_PROVIDER())
			.closeable(true)
			.title("žežlSchauen einen Spieler zu")
			.build();

}
