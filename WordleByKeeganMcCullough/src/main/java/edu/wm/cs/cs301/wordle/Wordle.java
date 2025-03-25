package edu.wm.cs.cs301.wordle;

import javax.swing.*;

import edu.wm.cs.cs301.wordle.model.WordleModel;
import edu.wm.cs.cs301.wordle.view.WordleFrame;
import edu.wm.cs.cs301.wordle.model.Statistics;

public class Wordle implements Runnable {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Wordle());

        //Can't use the Cross-Platform Look and Feel on Windows - Needs investigation
        if (!System.getProperty("os.name").contains("Windows")) {
            //Must use cross-platform look and feel so button backgrounds work on Mac
            try {
                System.out.println("OS: " + System.getProperty("os.name"));
                // Uncomment to debug available LAFs on current system
/*
                System.out.println("Cross-Platform name: " + UIManager.getCrossPlatformLookAndFeelClassName());
                System.out.println("System name: " + UIManager.getSystemLookAndFeelClassName());
                UIManager.LookAndFeelInfo[] installed = UIManager.getInstalledLookAndFeels();
                for(UIManager.LookAndFeelInfo info : installed) {
                    System.out.println("INSTALLED: " + info);
                }
*/

                UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
            }
            catch (Exception e) {
                System.out.println("Failed to load Cross-Platform Look and Feel on non-Windows system.");
                System.out.println("GUI may not function correctly");
            }
        }
    }

    @Override
    public void run() {
        Statistics statistics = new Statistics();
        new WordleFrame(new WordleModel("Normal", statistics));
    }
}
