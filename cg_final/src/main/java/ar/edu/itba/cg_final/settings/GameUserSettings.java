package ar.edu.itba.cg_final.settings;

import ar.edu.itba.cg_final.utils.GraphicsQualityUtils;


public class GameUserSettings {

	String terrain;
	String raceMode;
	String playerCar;
	String videoSettings;

	private Integer musicVolume;
	private Boolean musicOn;
	private Integer sfxVolume;
	private Boolean sfxOn;
	private Boolean highRes;
	private String skybox;
	
	static private GameUserSettings instance;
	
	private GameUserSettings() {
		
		musicOn = GlobalSettings.getInstance().getIntProperty("MUSIC.STATE") == 1;
		sfxOn = GlobalSettings.getInstance().getIntProperty("EFFECT.STATE") == 1;
		
		int mLevel = GlobalSettings.getInstance().getIntProperty("MUSIC.LEVEL");
		musicVolume = ( mLevel > 100) ? 100 : (mLevel < 0 ? 0 : mLevel);
		
		int sLevel = GlobalSettings.getInstance().getIntProperty("EFFECT.LEVEL");
		sfxVolume = ( sLevel > 100) ? 100 : (sLevel < 0 ? 0 : sLevel);
		
		String qName = GlobalSettings.getInstance().getProperty("GAME.GRAPHICS.QUALITY");
		highRes = GraphicsQualityUtils.High.toString().equalsIgnoreCase(qName);
		skybox = GlobalSettings.getInstance().getSkyBoxesNames().get(0);
	}

	static public GameUserSettings getInstance(){
		if( instance == null )
			instance = new GameUserSettings();
		return instance;
	}
	
	public Integer getMusicVolume() {
		return musicVolume;
	}
	
	public void setMusicVolume(Integer musicVolume) {
		this.musicVolume = musicVolume;
	}
	
	public Boolean isMusicOn() {
		return musicOn;
	}
	
	public void setMusicOn(Boolean musicOn) {
		this.musicOn = musicOn;
	}
	
	public Integer getSfxVolume() {
		return sfxVolume;
	}
	
	public void setSfxVolume(Integer sfxVolume) {
		this.sfxVolume = sfxVolume;
	}
	
	public Boolean isSfxOn() {
		return sfxOn;
	}
	
	public void setSfxOn(Boolean sfxOn) {
		this.sfxOn = sfxOn;
	}

	public String getSkybox() {
		return this.skybox;
	}

	public void setSkyBox(String newSkybox) {
		this.skybox = newSkybox;
	}
	
	public void setHighRes(Boolean highRes) {
		this.highRes = highRes;
	}
	
	public Boolean getHighRes() {
		return highRes;
	}

	@Override
	public String toString() {
		return "GameUserSettings [highRes=" + highRes + ", musicOn=" + musicOn
				+ ", musicVolume=" + musicVolume + ", sfxOn=" + sfxOn
				+ ", sfxVolume=" + sfxVolume + ", skybox=" + skybox + "]";
	}

}