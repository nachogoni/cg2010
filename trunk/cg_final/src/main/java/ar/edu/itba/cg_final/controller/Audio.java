package ar.edu.itba.cg_final.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ar.edu.itba.cg_final.RallyGame;
import ar.edu.itba.cg_final.settings.GameUserSettings;

import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.MusicTrackQueue;
import com.jmex.audio.MusicTrackQueue.RepeatType;

public class Audio {

	private static Audio instance;
	private MusicTrackQueue queue;
	
	public enum soundsEffects {
		HIT_SOUND, CHECKPOINT, ENGINE, ACEL 
	};
	
	private Map<soundsEffects, AudioTrack> map = new HashMap<soundsEffects, AudioTrack>();
    
	public void pauseAll() {
		queue.stop();
		for (Iterator<AudioTrack> iterator = map.values().iterator(); iterator.hasNext();) {
			AudioTrack sound = iterator.next();
			if (!sound.isStopped()) {
				sound.stop();
			}
		} 
	}
	public void unpauseAll() {
		queue.play();
		for (Iterator<AudioTrack> iterator = map.values().iterator(); iterator.hasNext();) {
			AudioTrack sound = iterator.next();
			if (!sound.isPlaying() && sound.isActive()) {
				sound.play();
			}
		} 		
	}
	
	private Audio(){
		queue = AudioSystem.getSystem().getMusicQueue();
		queue.setCrossfadeinTime(0);
		queue.setRepeatType(RepeatType.ALL);
	}
	
	static public Audio getInstance(){
		if ( instance == null )
			instance = new Audio();
		return instance;
	}
	
	public void addSong(String path){
		AudioTrack aTrack = AudioSystem.getSystem().createAudioTrack(
							RallyGame.class.getClassLoader().getResource(path), false);
		aTrack.setMaxVolume(GameUserSettings.getInstance().getMusicVolume()/100.f);
		aTrack.setEnabled(GameUserSettings.getInstance().isMusicOn());
		queue.addTrack(aTrack);
	}
	
	public void playList() {
		AudioSystem.getSystem().getMusicQueue().play();
	}
	
	public void cleanup() {
		AudioSystem.getSystem().cleanup();
	}
	
	public void update(){
		AudioSystem.getSystem().update();
	}
	
	public void playSound(soundsEffects sound) {
		map.get(sound).play();
	}
	
	public void addSound(String property, soundsEffects effect, boolean loop, GameUserSettings gus) {
		AudioTrack audioTrack = AudioSystem.getSystem().createAudioTrack(
								RallyGame.class.getClassLoader().getResource(property), false);
		audioTrack.setLooping(loop);
		audioTrack.setMaxVolume(gus.getSfxVolume()/100.f);
		audioTrack.setEnabled(gus.isSfxOn());
		map.put(effect, audioTrack);
	}
	
	public void addSound(String property, soundsEffects effect, GameUserSettings gus) {
		addSound(property, effect, false, gus);
	}
	
	public void stopSound(soundsEffects sound) {
		map.get(sound).stop();
	}
	
	public void setSoundsStatusAndVolume(){
		for(AudioTrack at:map.values()){
			if ( map.get(at) != null){
				map.get(at).setMaxVolume(GameUserSettings.getInstance().getSfxVolume()/100.f);
				map.get(at).setEnabled(GameUserSettings.getInstance().isSfxOn());
			}
		}
	}

	public void setMusicStatusAndVolume(){
		for(AudioTrack at:AudioSystem.getSystem().getMusicQueue().getTrackList()){
			if ( map.get(at) != null){
				at.setMaxVolume(GameUserSettings.getInstance().getMusicVolume()/100.f);
				at.setEnabled(GameUserSettings.getInstance().isMusicOn());
			}
		}
	}
}
