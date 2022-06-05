package wordle;
import javax.swing.*;

public class resultPopup {
    static int streakCounter = 0;
    static JFrame frame = new JFrame();

    public static void win(String word) {
        streakCounter++;
        JOptionPane.showMessageDialog(frame, "<html><center>Congratulations! The word was <b>"+word+"</b> <br> Streak count:" + Integer.toString(streakCounter));
        Board.resetGame();

    }

    public static void lose(String word) {
        streakCounter = 0;

        JOptionPane.showMessageDialog(frame, "<html><center>Sorry, the word was "+ word);
        Board.resetGame();
    }

    public static void resetStreak() {
        streakCounter = 0;
    }
    


}
