package fr.maxlego08.shop.zshop.boost;

public enum BoostType {

	SELL("de vente"), BUY("d'achat");

	String name;

	private BoostType(String name) {
		this.name = name;
	}

	public String toName() {
		return name;
	}

}
