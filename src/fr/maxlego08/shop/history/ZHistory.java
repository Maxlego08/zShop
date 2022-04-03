package fr.maxlego08.shop.history;

import fr.maxlego08.shop.api.history.History;
import fr.maxlego08.shop.api.history.HistoryType;

public class ZHistory implements History {

	private final HistoryType type;
	private final String message;
	private final long date;

	/**
	 * @param type
	 * @param message
	 */
	public ZHistory(HistoryType type, String message) {
		super();
		this.type = type;
		this.message = message;
		this.date = System.currentTimeMillis();
	}

	@Override
	public long getDate() {
		return date;
	}

	@Override
	public HistoryType getType() {
		return type;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
