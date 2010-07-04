package ar.edu.itba.cg_final.settings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scores implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6875504180449349560L;
	
	private List<Score> scores;

	private static Scores instance;
	
	private static final String path = "high.scores";

	private Scores() {
		super();
		this.scores = new ArrayList<Score>();
	}
	
	static public Scores getInstance(){
		if ( instance == null ){
			instance = load();
			if ( instance == null )
				instance = new Scores();
		}
		return instance;
	}
	
	private static Scores load(){
		try {
			ObjectInputStream highscores = new ObjectInputStream(new FileInputStream(path));
			Scores readObject = (Scores) highscores.readObject();
			return readObject;
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
		}
		return null;
	}
	
	private static void save(){
		try {
			ObjectOutputStream highscores = new ObjectOutputStream(new FileOutputStream(path));
			highscores.writeObject(instance);
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		
	}
	
	protected void finalize() {
		save();
	}
		
	public void sort(){
		Collections.sort(scores);
	}
	
	public void addScore(Score aScore){
		this.scores.add(aScore);
		this.sort();
		this.scores = new ArrayList<Score>(this.scores.subList(0, this.scores.size() < 10 ? this.scores.size():10));
		save();
	}

	@Override
	public String toString() {
		return "Scores [scores=" + scores + "]";
	}
	
	public Score getScore(int position){
		return this.scores.get(position);
	}
	
	public List<Score> getScores(){
		return this.scores;
	}
	
}
