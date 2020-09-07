package fr.maxlego08.shop.api.history;

import java.util.UUID;

public interface History {

	public UUID getPlayer();
	
	public long getDate();
	
	public String getMessage();
	
}
