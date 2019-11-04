package fr.maxlego08.template.save;

import fr.maxlego08.template.zcore.utils.storage.Persist;
import fr.maxlego08.template.zcore.utils.storage.Saveable;

public class Config implements Saveable {

	public static String version = "0.0.0.1";
	
	private static transient Config i = new Config();
	
	@Override
	public void save(Persist persist) {
		persist.save(i, "config");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, Config.class, "config");
	}

}
