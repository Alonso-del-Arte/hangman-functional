package swingaux;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextField;

public final class SinglePressKeyboardTestFrame extends JFrame
        implements ActionListener {

    public static final int DEFAULT_COLUMN_WIDTH = 20;

    private final JTextField textField;

    final SinglePressKeyboard keyboard;

    @Override
    public void actionPerformed(ActionEvent ae) {
        String fieldText = this.textField.getText() + ae.getActionCommand();
        this.textField.setText(fieldText);
    }

    public SinglePressKeyboardTestFrame() {
        super("Single Press Keyboard Test Frame");
        this.setLayout(new BorderLayout());
        this.textField = new JTextField(DEFAULT_COLUMN_WIDTH);
        this.textField.setEditable(false);
        this.add(this.textField, BorderLayout.PAGE_START);
        this.keyboard = new SinglePressKeyboard(this);
        this.addKeyListener(this.keyboard.physKbdListen);
        this.add(this.keyboard, BorderLayout.CENTER);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SinglePressKeyboardTestFrame frame = new SinglePressKeyboardTestFrame();
        frame.pack();
        frame.setVisible(true);
        frame.keyboard.setFocusable(true);
        frame.keyboard.requestFocus();
    }

}
