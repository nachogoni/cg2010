package ar.edu.itba.cg.tpe2.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FileParser {

	private FileReader aFile;
	private BufferedReader aBReader;
	private List<String> aList;
	private int index;
	
	public FileParser(String filename) throws FileNotFoundException{
		this.aFile = new FileReader(filename);
		this.aBReader = new BufferedReader(this.aFile);
		this.aList = new ArrayList<String>();
		this.index = 0;
	}
	
	public void close() throws IOException{
		if(this.aFile != null){
			this.aFile.close();
			this.aBReader = null;
			this.aList.clear();
			this.index = 0;
		}
	}
	
	private boolean getLine() throws IOException{
		String aLine;
		if(this.aFile == null){
			return false;
		}
	
		do{
			aLine = this.aBReader.readLine();
			
			if(aLine == null){
				return false;
			}
		} while(aLine.startsWith("##") || aLine.startsWith("%"));
		
		
		StringTokenizer aST = new StringTokenizer(aLine);
		
		if(aST == null){
			return false;
		}
		
		while (aST.countTokens() > 0){
			String aString = aST.nextToken();
			if(aString.length()>0)
				this.aList.add(aString);
		}

		return true;
		
	}

	public String getNextToken() throws IOException {
		String token;
		if(this.aFile == null){
			return null;
		}
		if(this.aList.size() <= this.index){
			// We've reached the last token, we have to add more.
			do{
				if(this.getLine() == false){
					return null;
				}
			}while(this.aList.size() <= this.index);
			
		}
		String aRet = this.aList.get(this.index);
		this.index++;
		if(aRet.equals("/*")){
			do {
				token = this.getNextToken();
					
                if (token == null)
                    return null;
            } while (!token.equals("*/"));
			aRet = this.getNextToken();
		}
		
		//System.out.println(aRet);
		return aRet;
	}
	
	public String peekNextToken() throws IOException {
		String token;
		Integer index = this.index;
		if(this.aFile == null){
			return null;
		}
		if(this.aList.size() <= this.index){
			// We've reached the last token, we have to add more.
			do{
				if(this.getLine() == false){
					return null;
				}
			}while(this.aList.size() <= this.index);
			
		}
		String aRet = this.aList.get(index);
		index++;
		if(aRet.equals("/*")){
			do {
                token = this.aList.get(index);
                this.index++;
                if (token == null)
                    return null;
            } while (!token.equals("*/"));
			aRet = this.aList.get(index);
			index++;
		}
		
		return aRet;
	}
	
}
