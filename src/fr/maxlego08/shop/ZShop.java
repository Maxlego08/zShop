package fr.maxlego08.shop;

import fr.maxlego08.shop.command.CommandManager;
import fr.maxlego08.shop.inventory.InventoryManager;
import fr.maxlego08.shop.listener.AdapterListener;
import fr.maxlego08.shop.save.Config;
import fr.maxlego08.shop.scoreboard.ScoreBoardManager;
import fr.maxlego08.shop.zcore.ZPlugin;
import fr.maxlego08.shop.zcore.utils.builder.CooldownBuilder;

public class ZShop extends ZPlugin {

	@Override
	public void onEnable() {

		preEnable();

		commandManager = new CommandManager(this);

		if (!isEnabled())
			return;
		inventoryManager = InventoryManager.getInstance();

		scoreboardManager = new ScoreBoardManager(1000);
		
		/* Add Listener */

		addListener(new AdapterListener(this));
		addListener(inventoryManager);

		/* Add Saver */
		addSave(Config.getInstance());
		addSave(new CooldownBuilder());

		getSavers().forEach(saver -> saver.load(getPersist()));

		postEnable();
	}

	@Override
	public void onDisable() {

		preDisable();

		getSavers().forEach(saver -> saver.save(getPersist()));

		postDisable();

	}

}
