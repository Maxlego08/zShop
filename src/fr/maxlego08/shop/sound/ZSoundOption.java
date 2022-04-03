package fr.maxlego08.shop.sound;

import org.bukkit.entity.Entity;

import fr.maxlego08.shop.api.sound.SoundOption;
import fr.maxlego08.shop.zcore.utils.XSound;

public class ZSoundOption implements SoundOption {

	private final XSound sound;
	private final float pitch;
	private final float volume;

	/**
	 * @param sound
	 * @param pitch
	 * @param volume
	 */
	public ZSoundOption(XSound sound, float pitch, float volume) {
		super();
		this.sound = sound;
		this.pitch = pitch;
		this.volume = volume;
	}

	/**
	 * @return the sound
	 */
	public XSound getSound() {
		return sound;
	}

	/**
	 * @return the pitch
	 */
	public float getPitch() {
		return pitch;
	}

	/**
	 * @return the volume
	 */
	public float getVolume() {
		return volume;
	}

	@Override
	public void play(Entity entity) {
		if (sound != null)
			sound.play(entity, volume, pitch);
	}

}
