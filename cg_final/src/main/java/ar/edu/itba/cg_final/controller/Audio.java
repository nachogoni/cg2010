package ar.edu.itba.cg_final.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ar.edu.itba.cg_final.RallyGame;
import ar.edu.itba.cg_final.settings.*;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.MusicTrackQueue;
import com.jmex.audio.MusicTrackQueue.RepeatType;

public class Audio {

	private MusicTrackQueue queue;
	private GameUserSettings gus = GameUserSettings.getInstance();

	
	public enum soundsEffects {
		HIT_SOUND, CHECKPOINT, ENGINE, ACEL 
	};
	
	private Map<soundsEffects, AudioTrack> map = new HashMap<soundsEffects, AudioTrack>();
    
	public void pauseAll() {
		for (Iterator<AudioTrack> iterator = map.values().iterator(); iterator.hasNext();) {
			AudioTrack sound = iterator.next();
			if (!sound.isStopped()) {
				sound.stop();
			}
		} 
	}
	public void unpauseAll() {
		for (Iterator<AudioTrack> iterator = map.values().iterator(); iterator.hasNext();) {
			AudioTrack sound = iterator.next();
			if (!sound.isPlaying() && sound.isActive()) {
				sound.play();
			}
		} 		
	}
	
	public Audio() {
		queue = AudioSystem.getSystem().getMusicQueue();
		queue.setCrossfadeinTime(0);
		queue.setRepeatType(RepeatType.ALL);
	}
	
	public void addSong(String path) {
		AudioTrack aTrack = AudioSystem.getSystem().createAudioTrack(
				RallyGame.class.getClassLoader().getResource(
						path), false);
		aTrack.setVolume(gus.getMusicVolume());
		queue.addTrack(aTrack);
	}
	
	public void playList() {
		AudioSystem.getSystem().getMusicQueue().play();
	}
	
	public void cleanup() {
		AudioSystem.getSystem().cleanup();
	}
	
	public void update() {
		AudioSystem.getSystem().update();
	}
	
	public void playSound(soundsEffects sound) {
		map.get(sound).play();
	}
	
	public void addSound(String property, soundsEffects effect, boolean loop) {
		AudioTrack audioTrack = AudioSystem.getSystem().createAudioTrack(
				RallyGame.class.getClassLoader().getResource(
						property), false);
		audioTrack.setVolume(gus.getSfxVolume());
		audioTrack.setLooping(loop);
		map.put(effect, audioTrack);
	}
	
	public void addSound(String property, soundsEffects effect) {
		addSound(property, effect, false);
	}
	
	public void stopSound(soundsEffects sound) {
		map.get(sound).stop();
	}
	
}
