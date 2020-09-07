package fr.maxlego08.shop.api.history;

import java.util.Optional;
import java.util.UUID;

public interface HistoryManager {

	/**
	 * Get file is present
	 * @param uuid
	 * @return {@link Optional}
	 */
	public Optional<HistoryFile> getFile(UUID uuid);
	
	/**
	 * Get or create file
	 * @param uuid
	 * @return {@link Optional}
	 */
	public HistoryFile getOrCreate(UUID uuid);
	
	/**
	 * Load or create file
	 * @param uuid
	 * @return {@link HistoryFile}
	 */
	public HistoryFile loadOrCreate(UUID uuid);
	
	/**
	 * 
	 * @param uuid
	 * @return true if file is load
	 */
	public boolean isLoad(UUID uuid);
	
	/**
	 * 
	 */
	public void asyncSave();
	
	/**
	 * 
	 * @param uuid
	 */
	public void asyncSave(UUID uuid);
	
	/**
	 * 
	 * @param uuid
	 */
	public void save(UUID uuid);
	
	/**
	 * Add async value
	 * @param uuid
	 * @param history
	 */
	public void asyncValue(UUID uuid, History history);
	
}
