package ar.edu.itba.cg_final.controller;

import java.util.HashMap;

import ar.edu.itba.cg_final.controller.player.Player;
import ar.edu.itba.cg_final.map.CheckPoint;

public class GameController {

	private HashMap<String, Player> players = new HashMap<String, Player>();
	private HashMap<String, CheckPoint> positions = new HashMap<String, CheckPoint>();
	private HashMap<String, CheckPoint> checkPoints = new HashMap<String, CheckPoint>();
	
	public GameController() {
		
	}
	
	public void addPlayer(Player player) {
		if (player != null)
			this.players.put(player.getName(), player);
	}
	
	public Player getPlayer(String name) {
		return players.get(name);
	}
	
	public void addCheckPoint(CheckPoint checkPoint) {
		if (checkPoint != null)
			checkPoints.put(checkPoint.getName(), checkPoint);
	}
	
	public CheckPoint getCheckPoint(String name) {
		return checkPoints.get(name);
	}
	
	public boolean passThrough(Player player, CheckPoint checkPoint) {
		if (player == null || checkPoint == null)
			return false;
		
		String playerName = player.getName();
		String checkPointName = checkPoint.getName();
		
		// If there is no player o checkpoint listed in the game controller
		if (!checkPoints.containsKey(checkPointName) || !players.containsKey(playerName))
			return false;
		
		if (positions.get(playerName).getNext().getName().equals(checkPointName)) {
			// Update positions for that player
			positions.put(playerName, checkPoint);
			// Do something at the checkpoint
			checkPoint.performAction();
			return true;
		}

		return false;
	}
	
	
	
}
