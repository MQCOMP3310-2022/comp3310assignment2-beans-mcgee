
package wordle;

import java.awt.Graphics;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.io.FileInputStream;

import javax.swing.JOptionPane;


public class Board {

    static {
 
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("resources/logging.properties"));
        } catch (SecurityException | IOException e1) {
            e1.printStackTrace();
        }
    }

    static final Logger logger = Logger.getLogger(Board.class.getName());

    static Grid grid;
    static SQLiteConnectionManager wordleDatabaseConnection;
    static int secretWordIndex;
    static int numberOfWords;

    public Board(){
        wordleDatabaseConnection = new SQLiteConnectionManager("words.db");
        int setupStage = 0;

        wordleDatabaseConnection.createNewDatabase("words.db");
        if (wordleDatabaseConnection.checkIfConnectionDefined())
        {
            logger.log(Level.INFO, "Wordle created and connected.");
            if(wordleDatabaseConnection.createWordleTables())
            {
                logger.log(Level.INFO, "Wordle structures in place.");
                setupStage = 1;
            }
        }

        if(setupStage == 1)
        {
            //let's add some words to valid 4 letter words from the data.txt file

            try (BufferedReader br = new BufferedReader(new FileReader("resources/data.txt"))) {
                String line;
                int i = 1;
                while ((line = br.readLine()) != null) {
                   wordleDatabaseConnection.addValidWord(i,line.toUpperCase()); // Making everything CAPITALIZED
                   i++;                                                         // Visually Looks better that way
                }
                numberOfWords = i;
                setupStage = 2;
            }catch(IOException e)
            {
                logger.log(Level.INFO, e.getMessage());
            }

        }
        else{
            logger.log(Level.SEVERE, "Not able to Launch. Sorry!");
        }

        grid = new Grid(6,4, wordleDatabaseConnection);
        
        Random rand = new Random();
        secretWordIndex  = rand.nextInt(numberOfWords); //N words in databse = numberOfWords - 1

        String theWord = wordleDatabaseConnection.getWordAtIndex(secretWordIndex);
        grid.setWord(theWord);
    }

    public void resetBoard(){
        grid.reset();
    }

    void paint(Graphics g){
        grid.paint(g);
    }    

    public static void keyPressed(KeyEvent e){
        logger.log(Level.INFO, "Key Pressed! " + e.getKeyCode());

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            grid.keyPressedEnter();
            logger.log(Level.INFO, "Enter Key");
        }
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            grid.keyPressedBackspace();
            logger.log(Level.INFO, "Backspace Key");
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            
            int confirmChoice = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset? Streak count will be reset", "Confirmation dialog",  JOptionPane.OK_CANCEL_OPTION);
            if (confirmChoice == JOptionPane.OK_OPTION) { // Confirmation before user wants to reset as it will reset streak
                grid.keyPressedEscape();
                Random rand = new Random();
                secretWordIndex = (rand.nextInt(numberOfWords)) % numberOfWords;
                String theWord = wordleDatabaseConnection.getWordAtIndex(secretWordIndex);
                grid.setWord(theWord);

                resultPopup.resetStreak();;
            }

           logger.log(Level.INFO, "Escape Key");
        }

        if((e.getKeyChar() >= 'A' && e.getKeyChar() <= 'Z') || (e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z' )){ // Alphabetical letter filter
            grid.keyPressedLetter(Character.toUpperCase(e.getKeyChar()));
            logger.log(Level.INFO, "Character Key");
        }

    }

    public static void resetGame() {
        grid.keyPressedEscape();
            
            Random rand = new Random();
            secretWordIndex  = rand.nextInt(numberOfWords);
            String theWord = wordleDatabaseConnection.getWordAtIndex(secretWordIndex);
            grid.setWord(theWord);
    }
}
