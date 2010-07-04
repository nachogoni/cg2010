package ar.edu.itba.cg_final.settings;


public class GameUserSettings {

	String terrain;
	String raceMode;
	String playerCar;
	String videoSettings;

	private Integer musicVolume = 100;
	private Boolean musicOn = true;
	private Integer sfxVolume = 100;
	private Boolean sfxOn = true;
	private String skybox = GlobalSettings.getInstance().getSkyBoxesNames().get(0);
	
	static private GameUserSettings instance;
	
	private GameUserSettings() {
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

	@Override
	public String toString() {
		return "GameUserSettings [musicOn=" + musicOn + ", musicVolume="
				+ musicVolume + ", sfxOn=" + sfxOn + ", sfxVolume=" + sfxVolume
				+ ", skybox=" + skybox + "]";
	}
	
}