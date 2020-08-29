package fr.maxlego08.shop.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.arcaniax.hdb.api.DatabaseLoadEvent;

public class HeadListener implements Listener {

	private final Runnable runnable;

	/**
	 * @param runnable
	 */
	public HeadListener(Runnable runnable) {
		super();
		this.runnable = runnable;
	}

	@EventHandler
	public void onReady(DatabaseLoadEvent event) {
		runnable.run();
	}

}
