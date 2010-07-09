package ar.edu.itba.cg_final.settings;

import java.io.Serializable;
import java.util.Date;

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
	
	@SuppressWarnings("deprecation")
	public Score(Date aDate, long score){
		super();
		
		this.userID = String.format("%02d/%02d/%04d - %02d:%02d", aDate.getDate(), aDate.getMonth()+1, 
				aDate.getYear()+1900, aDate.getHours(), aDate.getMinutes());
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
	
	public String getScoreAsString(){
		int secs = (int) (score)/1000;
		int milisecs = (int) score%1000;
		int min = secs / 60;
		secs = secs % 60;
		return String.format("%02d:%02d.%03d", min, secs, milisecs);
		
	}

	@Override
	public String toString() {
		return "";
	}

	public int compareTo(Score aScore) {
		return (int) (this.score-aScore.getScore());
	}
	
	
	

}
