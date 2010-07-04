package ar.edu.itba.cg_final.controller;

import ar.edu.itba.cg_final.RallyGame;

import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.MusicTrackQueue;
import com.jmex.audio.MusicTrackQueue.RepeatType;

public class Audio {

	private MusicTrackQueue queue;
	
	public Audio(){
		this.queue = AudioSystem.getSystem().getMusicQueue();
		queue.setCrossfadeinTime(0);
		queue.setRepeatType(RepeatType.ALL);
	}
	
	public void addAudio(String path){
		AudioTrack aTrack = AudioSystem.getSystem().createAudioTrack(
				RallyGame.class.getClassLoader().getResource(
						path), false);
		
		queue.addTrack(aTrack);
	}
	
	public void play(){
		AudioSystem.getSystem().getMusicQueue().play();
	}
	
	public void update(){
		AudioSystem.getSystem().update();
	}
	
	public void playOnce(String path){
		AudioTrack aTrack = AudioSystem.getSystem().createAudioTrack(
				RallyGame.class.getClassLoader().getResource(
						path), false);
		aTrack.play();
	}
}
