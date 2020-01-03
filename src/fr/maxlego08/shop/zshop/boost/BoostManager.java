package fr.maxlego08.shop.zshop.boost;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.event.events.ShopBoostEndEvent;
import fr.maxlego08.shop.event.events.ShopBoostStartEvent;
import fr.maxlego08.shop.save.Config;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.utils.MaterialData;
import fr.maxlego08.shop.zcore.utils.ZUtils;
import fr.maxlego08.shop.zcore.utils.builder.TimerBuilder;
import fr.maxlego08.shop.zcore.utils.storage.Persist;
import fr.maxlego08.shop.zshop.factories.Boost;

public class BoostManager extends ZUtils implements Boost {

	private static Map<String, BoostItem> boosts = new HashMap<String, BoostItem>();

	@Override
	public Map<String, BoostItem> getBoosts() {
		return boosts;
	}

	@Override
	public void boost(CommandSender player, MaterialData material, BoostType type, double modifier, long ms) {
		ItemStack itemStack = material.toItemStack();

		// On verifie si un boost est déjà présent sur l'itemstack
		if (boosts.containsKey(itemStack)) {
			message(player, Lang.boostError);
			return;
		}

		BoostItem boostItem = new BoostItem(itemStack, (System.currentTimeMillis() + (ms * 1000)), type, modifier);
		boosts.put(encode(itemStack), boostItem);

		if (Config.shopBoostStartEvent) {
			ShopBoostStartEvent event = new ShopBoostStartEvent(boostItem);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled())
				return;
			boostItem = event.getBoostItem();
		}

		if (Config.broadcastMessageWhenBoostIsCreate) {
			Bukkit.broadcastMessage(Lang.prefix + " "
					+ Lang.boostBroadcast.replace("%item%", getItemName(itemStack))
							.replace("%modifier%", format(modifier))
							.replace("%boosttype%", type.equals(BoostType.BUY) ? Lang.boostBuy : Lang.boostSell)
							.replace("%timer%", TimerBuilder.getStringTime(ms)));
		}

		message(player, Lang.boostSuccess.replace("%item%", getItemName(itemStack)));

	}

	@Override
	public void updateBoost() {
		Iterator<Entry<String, BoostItem>> iterator = boosts.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, BoostItem> entry = iterator.next();
			if (entry.getValue().reset()) {
				if (Config.shopBoostStartEvent) {
					ShopBoostEndEvent event = new ShopBoostEndEvent(entry.getValue());
					Bukkit.getPluginManager().callEvent(event);
					if (!event.isCancelled())
						iterator.remove();
				} else
					iterator.remove();
			}
		}
	}

	@Override
	public boolean isBoost(ItemStack itemStack) {
		updateBoost();
		return boosts.containsKey(encode(itemStack));
	}

	@Override
	public BoostItem getBoost(ItemStack itemStack) {
		return boosts.get(encode(itemStack));
	}

	@Override
	public void save(Persist persist) {
		persist.save(this, "boosts");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(this, BoostManager.class, "boosts");
	}

	@Override
	public void stopBoost(CommandSender sender, MaterialData data) {

		ItemStack itemStack = data.toItemStack();

		// On verifie si un boost est déjà présent sur l'itemstack
		if (!boosts.containsKey(itemStack)) {
			message(sender, Lang.boostErrorFound.replace("%item%", getItemName(itemStack)));
			return;
		}

		getBoost(itemStack).end();
		updateBoost();

		message(sender, Lang.boostStopSuccess.replace("%item%", getItemName(itemStack)));

	}

	@Override
	public void show(CommandSender sender) {
		if (boosts.size() == 0)
			message(sender, Lang.boostEmpty);
		else
			boosts.values().forEach(d -> {
				message(sender,
						Lang.boostShow.replace("%item%", getItemName(d.getItemStack()))
								.replace("%modifier%", format(d.getModifier()))
								.replace("%type%",
										d.getBoostType().equals(BoostType.BUY) ? Lang.boostBuy : Lang.boostSell)
								.replace("%timer%", TimerBuilder
										.getStringTime(Math.abs(d.getEnding() - System.currentTimeMillis()) / 1000)));
			});
	}

}
