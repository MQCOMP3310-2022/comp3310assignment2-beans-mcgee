package wordle;
import javax.swing.*;
import java.awt.*;

public class resultPopup {
    public static void popup(JFrame frame) {

        JDialog dialog = new JDialog(frame, "Yo", Dialog.ModalityType.TOOLKIT_MODAL);

        dialog.setLayout(new FlowLayout());

        dialog.setBounds(500, 300, 400, 300);


        JLabel label = new JLabel("Press close button");

        dialog.add(label);
        dialog.setVisible(true);
    }
}
