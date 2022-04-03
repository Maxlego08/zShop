package fr.maxlego08.shop.zcore.utils;

import java.util.Optional;

import com.songoda.epicheads.EpicHeads;
import com.songoda.epicheads.head.Head;
import com.songoda.epicheads.head.HeadManager;

public class Heads {

	private final HeadManager manager = EpicHeads.getInstance().getHeadManager();

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Optional<Head> getHead(int id) {
		return manager.getHeads().stream().filter(head -> {
			return head.getId() == id;
		}).findAny();
	}

}
