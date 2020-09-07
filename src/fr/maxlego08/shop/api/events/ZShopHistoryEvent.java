package fr.maxlego08.shop.api.events;

import java.util.UUID;

import fr.maxlego08.shop.api.history.History;
import fr.maxlego08.shop.zcore.utils.event.CancelledShopEvent;

public class ZShopHistoryEvent extends CancelledShopEvent {

	private final UUID uuid;
	private History history;

	/**
	 * @param uuid
	 * @param history
	 */
	public ZShopHistoryEvent(UUID uuid, History history) {
		super();
		this.uuid = uuid;
		this.history = history;
	}

	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * @return the history
	 */
	public History getHistory() {
		return history;
	}

	/**
	 * @param history
	 *            the history to set
	 */
	public void setHistory(History history) {
		this.history = history;
	}

	}
