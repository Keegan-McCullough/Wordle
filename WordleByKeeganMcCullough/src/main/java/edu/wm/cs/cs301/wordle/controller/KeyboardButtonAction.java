package edu.wm.cs.cs301.wordle.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import java.util.List;

import edu.wm.cs.cs301.wordle.model.AppColors;
import edu.wm.cs.cs301.wordle.model.WordleModel;
import edu.wm.cs.cs301.wordle.model.WordleResponse;
import edu.wm.cs.cs301.wordle.view.StatisticsDialog;
import edu.wm.cs.cs301.wordle.view.WordleFrame;

public class KeyboardButtonAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	private final WordleFrame view;
	
	private final WordleModel model;

	public KeyboardButtonAction(WordleFrame view, WordleModel model) {
		this.view = view;
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton button = (JButton) event.getSource();
		String text = button.getActionCommand();
		switch (text) {
		case "Enter":
			// added to process the submitted word
			char[] currentGuess = model.getGuess();
			String submittedWord = "";
			for (char letter : currentGuess) {
				submittedWord += Character.toString(letter).toLowerCase();
			}
			// gets the word list to see if the word is in there
			List<String> wordList = model.getWordList();
			boolean isWord=false;
			for (String word : wordList) {
				if (word.equals(submittedWord)) {
					isWord = true;
					break;
				}
			}
			// if in word list and correct size the word is submitted and the row is recolored
			if (model.getCurrentColumn() >= (model.getColumnCount() - 1) && isWord) {
				int greenCount = 0;
				boolean moreRows = model.setCurrentRow();
				WordleResponse[] currentRow = model.getCurrentRow();
				for (WordleResponse wordleResponse : currentRow) {
					view.setColor(Character.toString(wordleResponse.getChar()),
							wordleResponse.getBackgroundColor(),
							wordleResponse.getForegroundColor());
					if (wordleResponse.getBackgroundColor().equals(AppColors.GREEN)) {
						greenCount++;
					}
				}

				if (greenCount >= model.getColumnCount()) {
					view.repaintWordleGridPanel();
					int currentRowNumber = model.getCurrentRowNumber();
					if (model.getCurrentDifficulty().equals("Normal")){
						model.getStatistics().incrementTotalNormalGamesPlayed();
						model.getStatistics().addNormalWordsGuessed(currentRowNumber);
					} else if (model.getCurrentDifficulty().equals("Pro")){
						model.getStatistics().incrementTotalProGamesPlayed();
						model.getStatistics().addProWordsGuessed(currentRowNumber);
					}
					int currentStreak = model.getStatistics().getCurrentStreak();
					model.getStatistics().setCurrentStreak(++currentStreak);
					// sets game result to true for better ui
					model.getStatistics().setGameResult(true);
					new StatisticsDialog(view, model);

				} else if (!moreRows) {
					view.repaintWordleGridPanel();
					if (model.getCurrentDifficulty().equals("Normal")){
						model.getStatistics().incrementTotalNormalGamesPlayed();
					} else if (model.getCurrentDifficulty().equals("Pro")){
						model.getStatistics().incrementTotalProGamesPlayed();
					}
					model.getStatistics().setCurrentStreak(0);
					// sets game result to false indicating failure to guess correctly
					model.getStatistics().setGameResult(false);
					new StatisticsDialog(view, model);

				} else {

					view.repaintWordleGridPanel();
				}
			}
			break;
		case "Backspace":
			model.backspace();
			view.repaintWordleGridPanel();
			break;
		default:
			model.setCurrentColumn(text.charAt(0));
			view.repaintWordleGridPanel();
			break;
		}
		
	}

}
