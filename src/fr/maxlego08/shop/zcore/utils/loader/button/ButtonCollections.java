package fr.maxlego08.shop.zcore.utils.loader.button;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.maxlego08.shop.api.Loader;
import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.exceptions.ButtonsNotFoundException;

public class ButtonCollections implements Loader<List<Button>> {

	@Override
	public List<Button> load(YamlConfiguration configuration, String name, Object... args) throws Exception {

		List<Button> buttons = new ArrayList<Button>();

		if (!configuration.contains("items") || !configuration.isConfigurationSection("items."))
			throw new ButtonsNotFoundException(
					"Impossible to find the list of buttons for the " + name + " inventory!");

		ConfigurationSection section = configuration.getConfigurationSection("items.");

		Loader<Button> loader = new ButtonLoader();
		for (String tmpPath : section.getKeys(false)) {
			Button button = loader.load(configuration, "items." + tmpPath + ".", name);
			buttons.add(button);
		}

		return buttons;
	}

	@Override
	public void save(List<Button> object, YamlConfiguration configuration, String path) {

	}

}
