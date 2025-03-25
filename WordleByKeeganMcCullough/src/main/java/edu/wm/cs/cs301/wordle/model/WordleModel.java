package edu.wm.cs.cs301.wordle.model;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import edu.wm.cs.cs301.wordle.controller.ReadWordsRunnable;

public class WordleModel {

    private char[] currentWord, guess;

    private final int columnCount, maximumRows;
    private int currentColumn, currentRow;

    private List<String> wordList;

    private final Random random;

    private final Statistics statistics;

    private final String currentDifficulty;

    private WordleResponse[][] wordleGrid;

    public WordleModel(String currentDifficulty, Statistics statistics) {
        // added if statement to correctly initialize columns and rows based on mode
        this.currentDifficulty = currentDifficulty;
        if(this.currentDifficulty.equals("Pro")) {
            this.columnCount = 6;
            this.maximumRows = 7;
        }
        else{
            this.columnCount = 5;
            this.maximumRows = 6;
        }
        this.currentColumn = -1;
        this.currentRow = 0;
        this.random = new Random();
        createWordList();

        this.wordleGrid = initializeWordleGrid();
        this.guess = new char[columnCount];
        if (statistics != null){
            this.statistics = statistics;
        } else{
            this.statistics = new Statistics();
        }
    }

    private void createWordList() {
        ReadWordsRunnable runnable = new ReadWordsRunnable(this);
        Thread wordListThread = new Thread(runnable);
        wordListThread.start();

        try {
            wordListThread.join();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public void initialize() {
        this.wordleGrid = initializeWordleGrid();
        this.currentColumn = -1;
        this.currentRow = 0;
        generateCurrentWord();
        this.guess = new char[columnCount];
    }

    public void generateCurrentWord() {
        String word = wordList.get(getRandomIndex());
        this.currentWord = word.toUpperCase().toCharArray();

        System.out.println("DEBUG: Current word set to " + word);
    }

    public String getCurrentWord() {
        return new String(this.currentWord);
    }

    private int getRandomIndex() {
        int size = wordList.size();
        return random.nextInt(size);
    }

    private WordleResponse[][] initializeWordleGrid() {
        WordleResponse[][] wordleGrid = new WordleResponse[maximumRows][columnCount];

        for (int row = 0; row < wordleGrid.length; row++) {
            for (int column = 0; column < wordleGrid[row].length; column++) {
                wordleGrid[row][column] = null;
            }
        }

        return wordleGrid;
    }

    public void setWordList(List<String> wordList) {
        this.wordList = wordList;
    }

    public List<String> getWordList(){return wordList;}

    public void setCurrentColumn(char c) {
        currentColumn++;
        currentColumn = Math.min(currentColumn, (columnCount - 1));
        guess[currentColumn] = c;
        wordleGrid[currentRow][currentColumn] = new WordleResponse(c,
                Color.WHITE, Color.BLACK);
    }

    public void backspace() {
        if (this.currentColumn > -1) { //only backspace if there's room
            wordleGrid[currentRow][currentColumn] = null;
            guess[currentColumn] = ' ';
            this.currentColumn--;
            this.currentColumn = Math.max(currentColumn, -1);
        }
    }

    public WordleResponse[] getCurrentRow() {
        return wordleGrid[getCurrentRowNumber()];
    }

    public int getCurrentRowNumber() {
        return currentRow - 1;
    }

    // new function to return the submitted guess
    public char[] getGuess(){return guess;}

    public boolean setCurrentRow() {
        for (int column = 0; column < guess.length; column++) {
            Color backgroundColor = AppColors.GRAY;
            Color foregroundColor = Color.WHITE;
            if (guess[column] == currentWord[column]) {
                backgroundColor = AppColors.GREEN;
            } else if (contains(currentWord, guess, column)) {
                backgroundColor = AppColors.YELLOW;
            }

            wordleGrid[currentRow][column] = new WordleResponse(guess[column], backgroundColor, foregroundColor);
        }

        currentColumn = -1;
        currentRow++;
        guess = new char[columnCount];

        return currentRow < maximumRows;
    }

    public String getCurrentDifficulty(){return currentDifficulty;}

    private boolean contains(char[] currentWord, char[] guess, int column) {
        for (int index = 0; index < currentWord.length; index++) {
            if (index != column && guess[column] == currentWord[index]) {
                return true;
            }
        }

        return false;
    }

    public WordleResponse[][] getWordleGrid() {
        return wordleGrid;
    }

    public int getMaximumRows() {
        return maximumRows;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }

    public int getTotalWordCount() {
        return wordList.size();
    }

    public Statistics getStatistics() {
        return statistics;
    }

}
