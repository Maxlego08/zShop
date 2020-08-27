package fr.maxlego08.shop.api;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.maxlego08.shop.api.exceptions.ButtonsNotFoundException;

public interface Loader<T> {

	/**
	 * Load object from yml
	 * 
	 * @param configuration
	 * @param path
	 * @return
	 * @throws ButtonsNotFoundException
	 */
	T load(YamlConfiguration configuration, String path, Object... args) throws Exception;

	/**
	 * Save object to yml
	 * 
	 * @param object
	 * @param configuration
	 * @param path
	 */
	void save(T object, YamlConfiguration configuration, String path);

}
