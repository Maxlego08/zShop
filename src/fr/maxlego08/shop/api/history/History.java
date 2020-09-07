package fr.maxlego08.shop.api.history;

public interface History {

	public long getDate();
	
	public HistoryType getType();
	
	public String getMessage();
	
}
