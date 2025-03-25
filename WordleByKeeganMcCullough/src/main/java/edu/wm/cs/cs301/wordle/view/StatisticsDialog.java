package edu.wm.cs.cs301.wordle.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.Serial;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import edu.wm.cs.cs301.wordle.model.WordleModel;

public class StatisticsDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private final ExitAction exitAction;
	
	private final NextAction nextAction;
	
	private final WordleFrame view;
	
	private final WordleModel model;

	public StatisticsDialog(WordleFrame view, WordleModel model) {
		super(view.getFrame(), "Statistics", true);
		this.view = view;
		this.model = model;
		this.exitAction = new ExitAction();
		this.nextAction = new NextAction();

		add(createMainPanel(), BorderLayout.NORTH);
		add(createButtonPanel(), BorderLayout.SOUTH);
		
		pack();
		setLocationRelativeTo(view.getFrame());
		
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private JPanel createMainPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		panel.add(createTopPanel(), BorderLayout.NORTH);
		panel.add(createBottomPanel(), BorderLayout.SOUTH);
		
		return panel;
	}
	
	private JPanel createTopPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		panel.add(createTitlePanel(), BorderLayout.NORTH);
		panel.add(createSummaryPanel(), BorderLayout.SOUTH);
		
		return panel;
	}
	
	private JPanel createTitlePanel() {
		// Changed this Jpanel to a grid layout so the messages are stacked on top of one another
		JPanel panel = new JPanel(new GridLayout(2, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

		// Calls new closing message if user failed
		if (!model.getStatistics().getGameResult()) {
			panel.add(createClosingMessage());
		}
		// new function to allow for closing message to be above statistics
		panel.add(createTitleMessage());

		return panel;
	}

	// Outputs the closing message with the word
	private JPanel createClosingMessage() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		JLabel label1 = new JLabel("You failed to guess the secret word "+ model.getCurrentWord() +" in the allocated "+ model.getMaximumRows() +" guesses");
		label1.setFont(AppFonts.getTextFont());
		panel.add(label1);

		return panel;
	}

	// adds statistic in the correct place
	private JPanel createTitleMessage() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

		JLabel label = new JLabel("Statistics");
		label.setFont(AppFonts.getTitleFont());
		panel.add(label);

		return panel;
	}

	private JPanel createBottomPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		panel.add(createSubtitlePanel(), BorderLayout.NORTH);
		panel.add(new DistributionPanel(view, model), BorderLayout.SOUTH);
		
		return panel;
	}
	
	private JPanel createSubtitlePanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		JLabel label = new JLabel("Guess Distribution");
		label.setFont(AppFonts.getTitleFont());
		panel.add(label);
		
		return panel;
	}
	
	private JPanel createSummaryPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		int totalNormalGamesPlayed = model.getStatistics().getTotalNormalGamesPlayed();
		int totalProGamesPlayed = model.getStatistics().getTotalProGamesPlayed();
		int totalGamesPlayed = totalNormalGamesPlayed + totalProGamesPlayed;
		int currentStreak = model.getStatistics().getCurrentStreak();
		int longestStreak = model.getStatistics().getLongestStreak();
		List<Integer> wordsGuessed = model.getStatistics().getWordsGuessed("Normal");
		List<Integer> proWordsGuessed = model.getStatistics().getWordsGuessed("Pro");
		int normalPercent = (wordsGuessed.size() * 1000 + 5) / (totalNormalGamesPlayed * 10);
		int proPercent = (proWordsGuessed.size() * 1000 + 5) / (totalProGamesPlayed * 10);

		panel.add(createStatisticsPanel(totalGamesPlayed, "Played", ""));
		panel.add(createStatisticsPanel(normalPercent, "Normal Win %", ""));
		panel.add(createStatisticsPanel(proPercent, "Pro Win %", ""));
		panel.add(createStatisticsPanel(currentStreak, "Current", "Streak"));
		panel.add(createStatisticsPanel(longestStreak, "Longest", "Streak"));
		
		return panel;
	}
	
	private JPanel createStatisticsPanel(int value, String line1, String line2) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		Font textFont = AppFonts.getTextFont();
		
		JLabel valueLabel = new JLabel(String.format("%,d", value));
		valueLabel.setFont(AppFonts.getTitleFont());
		valueLabel.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(valueLabel);
		
		JLabel label = new JLabel(line1);
		label.setFont(textFont);
		label.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(label);
		
		label = new JLabel(line2);
		label.setFont(textFont);
		label.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(label);
		
		return panel;
	}
	
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		InputMap inputMap = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitAction");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "nextAction");
		ActionMap actionMap = panel.getActionMap();
		actionMap.put("nextAction", nextAction);
		actionMap.put("exitAction", exitAction);
		
		JButton nextButton = new JButton("Next Word");
		nextButton.addActionListener(nextAction);
		panel.add(nextButton);
		
		JButton exitButton = new JButton("Exit Wordle");
		exitButton.addActionListener(exitAction);
		panel.add(exitButton);
		
		Dimension nextDimension = nextButton.getPreferredSize();
		Dimension exitDimension = exitButton.getPreferredSize();
		int maxWidth = Math.max(nextDimension.width, exitDimension.width) + 10;
		nextButton.setPreferredSize(new Dimension(maxWidth, nextDimension.height));
		exitButton.setPreferredSize(new Dimension(maxWidth, exitDimension.height));
		
		return panel;
	}
	
	private class ExitAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent event) {
			dispose();
			view.shutdown();
		}
		
	}
	
	private class NextAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent event) {
			dispose();
			model.initialize();
			view.repaintWordleGridPanel();
			view.resetDefaultColors();
		}
		
	}
}
