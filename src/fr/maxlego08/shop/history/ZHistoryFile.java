package fr.maxlego08.shop.history;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import fr.maxlego08.shop.api.history.History;
import fr.maxlego08.shop.api.history.HistoryFile;

public class ZHistoryFile implements HistoryFile {

	private final UUID uuid;
	private final List<ZHistory> histories = new ArrayList<ZHistory>();

	/**
	 * @param uuid
	 */
	public ZHistoryFile(UUID uuid) {
		super();
		this.uuid = uuid;
	}

	@Override
	public UUID getUniqueId() {
		return uuid;
	}

	@Override
	public OfflinePlayer getPlayer() {
		return Bukkit.getOfflinePlayer(uuid);
	}

	@Override
	public Collection<History> getHistories() {
		return Collections.unmodifiableCollection(histories);
	}

	@Override
	public void addHistory(History history) {
		this.histories.add((ZHistory) history);
	}

}
