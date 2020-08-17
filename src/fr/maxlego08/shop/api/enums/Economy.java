package fr.maxlego08.shop.api.enums;

public enum Economy {
	

	VAULT, 
	
	PLAYERPOINT, 
	
	TOKENMANAGER,
	
	MYSQLTOKEN, 
	
	CUSTOM, 
	
	ICECORE,
	
	;

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static Economy get(String string) {
		for (Economy e : values())
			if (e.name().equalsIgnoreCase(string))
				return e;
		return Economy.VAULT;
	}

}
