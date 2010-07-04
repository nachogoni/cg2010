package ar.edu.itba.cg_final.settings;

import java.io.Serializable;

public class Score implements Serializable, Comparable<Score> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2295623734097373090L;
	private String userID;
	private int score;
	
	public Score(String ID, int score) {
		super();
		this.userID = ID;
		this.score = score;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "Score [userID=" + userID + ", score=" + score + "]";
	}

	public int compareTo(Score aScore) {
		return aScore.getScore()-this.score;
	}
	
	

}
