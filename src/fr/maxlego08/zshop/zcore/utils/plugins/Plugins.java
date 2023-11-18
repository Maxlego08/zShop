package fr.maxlego08.zshop.zcore.utils.plugins;

public enum Plugins {
	
	VAULT("Vault"),
	ESSENTIALS("Essentials"),
	HEADDATABASE("HeadDatabase"), 
	PLACEHOLDER("PlaceholderAPI"),
	CITIZENS("Citizens"),
	TRANSLATIONAPI("TranslationAPI"),
	ZTRANSLATOR("zTranslator"),
	PLAYERPOINT("PlayerPoints"),
	VOTINGPLUGIN("VotingPlugin"),
	TOKENMANAGER("TokenManager"),
	SHOPGUIPLUS("ShopGUIPlus"),
	COINSENGINE("CoinsEngine"),
	ZMENU("zMenu"),

	;

	private final String name;

	Plugins(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
