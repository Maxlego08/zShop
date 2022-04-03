package fr.maxlego08.shop.api.history;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

public interface HistoryFile {

	/**
	 * 
	 * @return {@link UUID}
	 */
	public UUID getUniqueId();
	
	/**
	 * 
	 * @return {@link OfflinePlayer}
	 */
	public OfflinePlayer getPlayer();

	/**
	 * 
	 * @return
	 */
	public Collection<History> getHistories();
	
	/**
	 * 
	 * @param history
	 */
	public void addHistory(History history);
	
}
