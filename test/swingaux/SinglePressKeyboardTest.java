package swingaux;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import randomness.Pseudorandom;

class SinglePressKeyboardTest implements ActionListener {

    private static final String QWERTY = "QWERTYUIOP\nASDFGHJKL\nZXCVBNM";

    private static final String DVORAK = "PYFGCRL\nAOEUIDHTNS\nQJKXBMWVZ";

    private ActionEvent mostRecentEvent = null;

    @Override
    public void actionPerformed(ActionEvent ae) {
        this.mostRecentEvent = ae;
    }

    @Test
    void testPickLetter() {
        char letterChoice = (char) ('A' + Pseudorandom.nextInt(26));
        String letter = Character.toString(letterChoice);
        SinglePressKeyboard keyboard = new SinglePressKeyboard(letter,this);
        JButton button = keyboard.buttons.get(0);
        button.doClick();
        String nonNullMsg = "Most recent event should not be null";
        assert this.mostRecentEvent != null : nonNullMsg;
        String msg = "Keyboard with button for letter '" + letterChoice
                + "' should be source of most recent event";
        assertEquals(keyboard, this.mostRecentEvent.getSource(), msg);
        assertEquals(letter, this.mostRecentEvent.getActionCommand());
    }

    @Test
    void testLetterOnscreenButtonsRespondToPhysicalKeyboardListener() {
        SinglePressKeyboard keyboard = new SinglePressKeyboard(QWERTY, this);
        JFrame frame = new JFrame("For test purposes only");
        for (JButton button : keyboard.buttons) {
            String expected = button.getText();
            char ch = expected.charAt(0);
            KeyEvent event = new KeyEvent(frame, KeyEvent.KEY_PRESSED,
                    System.currentTimeMillis(), 0, ch, ch);
            keyboard.physKbdListen.keyPressed(event);
            assert this.mostRecentEvent != null
                    : "Most recent event should not be null at this point";
            String actual = this.mostRecentEvent.getActionCommand();
            String msg = "Pressing '" + expected
                    + "' key on physical keyboard should relay that key";
            assertEquals(expected, actual, msg);
        }
    }

    @Test
    void testKeyListenerIgnoresLettersWhenModifiersPresent() {
        SinglePressKeyboard keyboard = new SinglePressKeyboard(DVORAK, this);
        JFrame frame = new JFrame("For test purposes only");
        String msgEnd = " keystroke should be ignored";
        this.mostRecentEvent = null;
        for (JButton button : keyboard.buttons) {
            String buttonText = button.getText();
            char ch = buttonText.charAt(0);
            KeyEvent ctrlEvent = new KeyEvent(frame, KeyEvent.KEY_PRESSED,
                    System.currentTimeMillis(), KeyEvent.CTRL_DOWN_MASK, ch,
                    ch);
            keyboard.physKbdListen.keyPressed(ctrlEvent);
            String ctrlMsg = "Ctrl-" + ch + msgEnd;
            assert this.mostRecentEvent == null : ctrlMsg;
            KeyEvent altEvent = new KeyEvent(frame, KeyEvent.KEY_PRESSED,
                    System.currentTimeMillis(), KeyEvent.ALT_DOWN_MASK, ch, ch);
            keyboard.physKbdListen.keyPressed(altEvent);
            String altMsg = "Alt-" + ch + msgEnd;
            assert this.mostRecentEvent == null : altMsg;
        }
    }

    @Test
    void testEachButtonDisabledAfterSinglePress() {
        SinglePressKeyboard keyboard = new SinglePressKeyboard(QWERTY, this);
        for (JButton button : keyboard.buttons) {
            String buttonText = button.getText();
            String enabledMsg = "Button for " + buttonText
                    + " should be enabled";
            assert button.isEnabled() : enabledMsg;
            button.doClick();
            String msg = "After single press, button for " + buttonText
                    + " should be disabled";
            assert !button.isEnabled() : msg;
        }
    }

    @Test
    void testReset() {
        System.out.println("reset");
        SinglePressKeyboard keyboard = new SinglePressKeyboard(DVORAK, this);
        for (JButton buttonToDisable : keyboard.buttons) {
            buttonToDisable.setEnabled(false);
        }
        keyboard.reset();
        for (JButton button : keyboard.buttons) {
            String msg = "Button for " + button.getText()
                    + " should be enabled after reset";
            assert button.isEnabled() : msg;
        }
    }

}
