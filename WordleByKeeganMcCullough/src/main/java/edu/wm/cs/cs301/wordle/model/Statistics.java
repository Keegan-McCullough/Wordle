package edu.wm.cs.cs301.wordle.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Statistics {
	
	private int currentStreak, longestStreak, totalNormalGamesPlayed, totalProGamesPlayed;

	// new value to hold the result of the game in output the correct end screen
	private boolean gameResult;
	
	private List<Integer> normalWordsGuessed, proWordsGuessed;
	
	private String path, log;
	
	public Statistics() {
		this.normalWordsGuessed = new ArrayList<>();
		this.proWordsGuessed = new ArrayList<>();
		String fileSeparator = System.getProperty("file.separator");
		this.path = System.getProperty("user.home") + fileSeparator + "Wordle";
		this.log = fileSeparator + "statistics.log";
		System.out.println("DEBUG USER HOME: " + path + log);
		readStatistics();
	}

	private void readStatistics() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + log));
			this.currentStreak = Integer.valueOf(br.readLine().trim());
			this.longestStreak = Integer.valueOf(br.readLine().trim());
			this.totalNormalGamesPlayed = Integer.valueOf(br.readLine().trim());
			this.totalProGamesPlayed = Integer.valueOf(br.readLine().trim());
			int totalNormalWordsGuessed = Integer.valueOf(br.readLine().trim());
			
			for (int index = 0; index < totalNormalWordsGuessed; index++) {
				normalWordsGuessed.add(Integer.valueOf(br.readLine().trim()));
			}

			int totalProWordsGuessed = Integer.valueOf(br.readLine().trim());
			for (int index = 0; index < totalProWordsGuessed; index++) {
				proWordsGuessed.add(Integer.valueOf(br.readLine().trim()));
			}

			br.close();
		} catch (FileNotFoundException e) {
			this.currentStreak = 0;
			this.longestStreak = 0;
			this.totalNormalGamesPlayed = 0;
			this.totalProGamesPlayed = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeStatistics() {
		try {
			File file = new File(path);
			file.mkdir();
			file = new File(path + log);
			file.createNewFile();

			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(Integer.toString(currentStreak));
			bw.write(System.lineSeparator());
			bw.write(Integer.toString(longestStreak));
			bw.write(System.lineSeparator());
			bw.write(Integer.toString(totalNormalGamesPlayed));
			bw.write(System.lineSeparator());
			bw.write(Integer.toString(totalProGamesPlayed));
			bw.write(System.lineSeparator());
			bw.write(Integer.toString(normalWordsGuessed.size()));
			bw.write(System.lineSeparator());
			
			for (Integer value : normalWordsGuessed) {
				bw.write(Integer.toString(value));
				bw.write(System.lineSeparator());
			}
			bw.write(Integer.toString(proWordsGuessed.size()));
			bw.write(System.lineSeparator());

			for (Integer value : proWordsGuessed) {
				bw.write(Integer.toString(value));
				bw.write(System.lineSeparator());
			}
			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// new function to get the game result
	public boolean getGameResult(){return gameResult;}

	public int getCurrentStreak() {
		return currentStreak;
	}

	// new function to set the game result
	public void setGameResult(boolean gameResult){
		this.gameResult = gameResult;
	}

	public void setCurrentStreak(int currentStreak) {
		this.currentStreak = currentStreak;
		if (currentStreak > longestStreak) {
			this.longestStreak = currentStreak;
		}
	}

	public int getLongestStreak() {
		return longestStreak;
	}

	public int getTotalNormalGamesPlayed() {
		return totalNormalGamesPlayed;
	}

	public int getTotalProGamesPlayed() {
		return totalProGamesPlayed;
	}

	public void incrementTotalNormalGamesPlayed() {
		this.totalNormalGamesPlayed++;
	}

	public void incrementTotalProGamesPlayed(){this.totalProGamesPlayed++;}

	public List<Integer> getWordsGuessed(String difficulty) {
		// help split up the word guessed part of the statistics
		if (difficulty.equals("Pro")){
			return proWordsGuessed;
		}
		return normalWordsGuessed;
	}

	// two sepertae counters for each word guessed scenario
	public void addNormalWordsGuessed(int wordCount) {
		this.normalWordsGuessed.add(wordCount);
	}

	public void addProWordsGuessed(int wordCount) {
		this.proWordsGuessed.add(wordCount);
	}

}
