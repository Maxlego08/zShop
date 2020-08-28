package fr.maxlego08.shop.listener.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.maxlego08.shop.ZShop;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class CitizenListener implements Listener {

	private final ZShop shop;

	public CitizenListener(ZShop shop) {
		super();
		this.shop = shop;
	}

	@EventHandler
	public void onClick(NPCRightClickEvent event){
		shop.getShop().openShopWithCitizen(event.getClicker(), event.getNPC());
	}
	
	@EventHandler
	public void onClick(NPCLeftClickEvent event){
		shop.getShop().openShopWithCitizen(event.getClicker(), event.getNPC());
	}
	
}
