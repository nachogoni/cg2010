package ar.edu.itba.cg_final.controller;

import ar.edu.itba.cg_final.RallyGame;

import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.MusicTrackQueue;
import com.jmex.audio.MusicTrackQueue.RepeatType;

public class Audio {

	private MusicTrackQueue queue;
	
	private AudioTrack hitSound;
	
	private AudioTrack checkpointSound;
	
	public Audio(){
		this.queue = AudioSystem.getSystem().getMusicQueue();
		this.queue.setCrossfadeinTime(0);
		this.queue.setRepeatType(RepeatType.ALL);
	}
	
	public void addAudio(String path){
		AudioTrack aTrack = AudioSystem.getSystem().createAudioTrack(
				RallyGame.class.getClassLoader().getResource(
						path), false);
		
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
	
	public void playHit(){
		hitSound.play();
	}

	public void setHitSound(String property) {
		hitSound = AudioSystem.getSystem().createAudioTrack(
				RallyGame.class.getClassLoader().getResource(
						property), false);
	}

	public void playCheckpoint() {
		checkpointSound.play();
	}
	
	public void setCheckpointSound(String property) {
		checkpointSound = AudioSystem.getSystem().createAudioTrack(
				RallyGame.class.getClassLoader().getResource(
						property), false);
		checkpointSound.play();
	}
	
	
}
