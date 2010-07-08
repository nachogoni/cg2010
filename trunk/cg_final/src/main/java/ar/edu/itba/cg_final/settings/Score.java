package ar.edu.itba.cg_final.settings;

import java.io.Serializable;

public class Score implements Serializable, Comparable<Score> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2295623734097373090L;
	private String userID;
	private long score;
	
	public Score(String ID, long score) {
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

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "Score [userID=" + userID + ", score=" + score + "]";
	}

	public int compareTo(Score aScore) {
		return (int) (this.score-aScore.getScore());
	}
	
	

}
