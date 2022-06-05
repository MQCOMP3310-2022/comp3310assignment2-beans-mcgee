
package wordle;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.time.Duration;
import java.time.Instant;
import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.IOException;





public final class App extends JFrame {

    static {
 
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("resources/logging.properties"));
        } catch (SecurityException | IOException e1) {
            e1.printStackTrace();
        }
    }
  
    static final Logger logger = Logger.getLogger(App.class.getName());

    class WordleGame extends JPanel implements KeyListener{
        Board board;
        boolean stageBuilt = false;

        public WordleGame(){
            setPreferredSize(new Dimension(330, 490));
            this.addKeyListener(this);
            board = new Board();
            stageBuilt = true;
            this.setFocusable(true);
            pack();
            this.requestFocus();
        }

        @Override
        public void paint(Graphics g) {
          if (stageBuilt && isVisible()) {
            board.paint(g);
          }
        }

        @Override
        public void keyPressed (KeyEvent e) {}    
        
        @Override
        public void keyReleased (KeyEvent e) {
            Board.keyPressed(e);
        }    

        @Override
        public void keyTyped (KeyEvent e) {}  
        

    }

    public static void main(String[] args) throws Exception {
        App window = new App();
        window.run();
    }

    public App() {
        this.setTitle("Wordle By Beans Mcgee");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        WordleGame canvas = new WordleGame();
        this.setContentPane(canvas);
        this.pack(); 
        this.setLocationRelativeTo(null); // center of screen
        this.setVisible(true);

    }




    public void run() {
        while (true) {
            Instant startTime = Instant.now();
            this.repaint();
            Instant endTime = Instant.now();
            long howLong = Duration.between(startTime, endTime).toMillis();
            try {
                Thread.sleep(20L - howLong);
            } catch (InterruptedException e) {
                logger.log(Level.WARNING, "thread was interrupted, but who cares?");
            } catch (IllegalArgumentException e) {
                logger.log(Level.WARNING, "application can't keep up with framerate");
            }
        }
    }


}
