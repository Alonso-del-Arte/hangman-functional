package swingaux;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JPanel;

public final class SinglePressKeyboard extends JPanel
        implements ActionListener {

    public static final String QWERTY_LETTER_KEYS
            = "QWERTYUIOP\nASDFGHJKL\nZXCVBNM";

    List<JButton> buttons = new ArrayList<>();

    private final String keyChars;

    private final ActionListener actListen;

    public final KeyListener physKbdListen = new PhysicalKeyboardListener();

    public void reset() {
        for (JButton button : this.buttons) {
            button.setEnabled(true);
        }
    }

    public void actionPerformed(ActionEvent ae) {
        System.out.println(ae.getSource()); // TODO: Delete this line after all tests pass
        String command = ae.getActionCommand();
        ActionEvent event = new ActionEvent(this, ae.getID(), command,
                ae.getWhen(), ae.getModifiers());
        this.actListen.actionPerformed(event);
        List<JButton> matchingButtons = this.buttons.stream()
                .filter(button -> button.getText().equals(command))
                .collect(Collectors.toList());
        for (JButton button : matchingButtons) {
            button.setEnabled(false);
        }
        this.requestFocus();
    }

    private JButton makeButton(char ch) {
        String letter = Character.toString(ch);
        JButton button = new JButton(letter);
        button.addActionListener(this);
        this.buttons.add(button);
        return button;
    }

    private void addButtons(String keys) {
        char[] characters = keys.toCharArray();
        for (char ch : characters) {
            if (ch != '\n') {
                this.add(this.makeButton(ch));
            }
        }
    }

    public SinglePressKeyboard(ActionListener listener) {
        this(QWERTY_LETTER_KEYS, listener);
    }

    public SinglePressKeyboard(String keys, ActionListener listener) {
        super(new GridLayout(3, 12));
        this.addButtons(keys);
        this.actListen = listener;
        this.keyChars = keys.replace("\n", "");
    }

    private class PhysicalKeyboardListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent ke) {
            char ch = ke.getKeyChar();
            if (SinglePressKeyboard.this.keyChars.indexOf(ch) > -1
                    && ke.getModifiers() == 0) {
                ActionEvent event = new ActionEvent(ke.getSource(),
                        ActionEvent.ACTION_PERFORMED,
                        Character.toString(ke.getKeyChar()));
                SinglePressKeyboard.this.actionPerformed(event);
            }
        }

        private PhysicalKeyboardListener() {}

    }

}
