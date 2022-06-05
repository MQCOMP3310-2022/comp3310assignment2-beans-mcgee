
package wordle;

import java.awt.Graphics;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
// import java.util.Scanner;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;

import javax.swing.JOptionPane;


public class Board {
    static Grid grid;
    static SQLiteConnectionManager wordleDatabaseConnection;
    static int secretWordIndex;
    static int numberOfWords;
    static Random randomNumber;
    

    public Board(){
        randomNumber = new Random();
        wordleDatabaseConnection = new SQLiteConnectionManager("words.db");
        int setupStage = 0;

        wordleDatabaseConnection.createNewDatabase("words.db");
        if (wordleDatabaseConnection.checkIfConnectionDefined())
        {
            System.out.println("Wordle created and connected.");
            if(wordleDatabaseConnection.createWordleTables())
            {
                System.out.println("Wordle structures in place.");
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
                   //System.out.println(line);
                   wordleDatabaseConnection.addValidWord(i,line);
                   i++;
                }
                numberOfWords = i;
                setupStage = 2;
            }catch(IOException e)
            {
                System.out.println(e.getMessage());
            }

        }
        else{
            System.out.println("Not able to Launch. Sorry!");
        }



        grid = new Grid(6,4, wordleDatabaseConnection);
        
        randomNumber = new Random();
        secretWordIndex  = randomNumber.nextInt(numberOfWords); //N words in databse = numberOfWords - 1

        String theWord = wordleDatabaseConnection.getWordAtIndex(secretWordIndex);
        grid.setWord(theWord);
    }

    public void resetBoard(){
        grid.reset();
    }

    void paint(Graphics g){
        grid.paint(g);
    }    

    public void keyPressed(KeyEvent e){
        System.out.println("Key Pressed! " + e.getKeyCode());

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            grid.keyPressedEnter();
            System.out.println("Enter Key");
        }
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            grid.keyPressedBackspace();
            System.out.println("Backspace Key");
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            
            int confirmChoice = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset? Streak count will be reset", "Confirmation dialog",  JOptionPane.OK_CANCEL_OPTION);
            if (confirmChoice == JOptionPane.OK_OPTION) { // Confirmation before user wants to reset as it will reset streak
                grid.keyPressedEscape();
                
                secretWordIndex = randomNumber.nextInt(numberOfWords);
                String theWord = wordleDatabaseConnection.getWordAtIndex(secretWordIndex);
                grid.setWord(theWord);

                resultPopup.resetStreak();;
            }

            System.out.println("Escape Key");
        }
        if(e.getKeyCode()>= KeyEvent.VK_A && e.getKeyCode() <= KeyEvent.VK_Z){
            grid.keyPressedLetter(e.getKeyChar());
            System.out.println("Character Key");
        }

    }

    public static void resetGame() {
        grid.keyPressedEscape();
            
        randomNumber = new Random();
        secretWordIndex  = randomNumber.nextInt(numberOfWords); //N words in databse = numberOfWords - 1
        
            String theWord = wordleDatabaseConnection.getWordAtIndex(secretWordIndex);
            grid.setWord(theWord);
    }
}
