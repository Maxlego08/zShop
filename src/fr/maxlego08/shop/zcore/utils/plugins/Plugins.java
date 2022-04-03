package fr.maxlego08.shop.zcore.utils.plugins;

public enum Plugins {

	VAULT("Vault"),
	ESSENTIALS("Essentials"),
	HEADDATABASE("HeadDatabase"), 
	ZTRANSLATOR("zTranslator"),
	
	;

	private final String name;

	private Plugins(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
