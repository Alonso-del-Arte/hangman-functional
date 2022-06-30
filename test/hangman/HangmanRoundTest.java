package hangman;

import java.util.Random;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HangmanRoundTest {

    private static final Random RANDOM = new Random();

    @Test
    void testSolvedSoFarAtBeginning() {
        HangmanRound round = new HangmanRound("example");
        String expected = "_______";
        String actual = round.solvedSoFar();
        assertEquals(expected, actual);
    }

    @Test
    void testSolvedSoFarAtBeginningOtherWord() {
        HangmanRound round = new HangmanRound("accessibility");
        String expected = "_____________";
        String actual = round.solvedSoFar();
        assertEquals(expected, actual);
    }

    @Test
    void testIsPresent() {
        System.out.println("isPresent");
        String word = "binoculars";
        HangmanRound round = new HangmanRound(word);
        char[] letters = word.toCharArray();
        for (char letter : letters) {
            String msg = "Letter '" + letter + "' should be in \"" + word
                    + "\"";
            boolean opResult = round.isPresent(letter);
            assert opResult : msg;
        }
    }

    @Test
    void testIsNotPresent() {
        String word = "cab";
        HangmanRound round = new HangmanRound(word, 26);
        for (char letter = 'd'; letter < 123; letter++) {
            String msg = "Letter '" + letter + "' should NOT be in \"" + word
                    + "\"";
            boolean opResult = round.isPresent(letter);
            assert !opResult : msg;
        }
    }

    @Test
    void testIsPresentQueriesUpdateSolvedSoFar() {
        String word = "flamingos";
        HangmanRound round = new HangmanRound(word);
        char[] letters = word.toCharArray();
        String previousUncovered = "";
        for (char letter : letters) {
            String currUncovered = round.solvedSoFar();
            System.out.println(currUncovered);
            String progressMsg = "There should be progress from "
                    + previousUncovered + " to " + currUncovered;
            assert !currUncovered.equals(previousUncovered) : progressMsg;
            previousUncovered = currUncovered;
            boolean opResult = round.isPresent(letter);
            String msg = "Letter '" + letter + "' should be present in \""
                    + word + "\"";
            assert opResult : msg;
        }
    }

    /**
     * Test of the solvedSoFar function. The test word ought to be a word in
     * which the first letter occurs again at least once later in the word.
     */
    @Test
    void testSolvedSoFar() {
        System.out.println("solvedSoFar");
        String word = "azaleas";
        HangmanRound round = new HangmanRound(word);
        char[] letters = word.toCharArray();
        char firstLetter = letters[0];
        String msg = "Letter '" + firstLetter + "' should be in word \"" + word
                + "\"";
        boolean opResult = round.isPresent(firstLetter);
        assert opResult : msg;
        for (int i = 1; i < letters.length; i++) {
            if (letters[i] != firstLetter) letters[i] = '_';
        }
        String expected = new String(letters);
        String actual = round.solvedSoFar();
        assertEquals(expected, actual);
    }

    @Test
    void testNotSolvedYet() {
        HangmanRound round = new HangmanRound("example");
        String msg = "Round should not start out solved";
        assert !round.solved() : msg;
    }

    @Test
    void testSolved() {
        System.out.println("solved");
        String word = "walkers";
        HangmanRound round = new HangmanRound(word, word.length());
        char[] letters = word.toCharArray();
        for (char letter : letters) {
            String notYetSolvedMsg = "With letters still missing in \""
                    + round.solvedSoFar()
                    + "\", puzzle should not yet be solved";
            assert !round.solved() : notYetSolvedMsg;
            String letterMsg = "Word \"" + word + "\" should have letter '" + letter
                    + "'";
            boolean opResult = round.isPresent(letter);
            assert opResult : letterMsg;
        }
        String msg = "After querying each letter, puzzle should be solved";
        assert round.solved() : msg;
    }

    @Test
    void testNumberOfGuessesLeftAtBeginning() {
        int expected = RANDOM.nextInt(10) + 2;
        HangmanRound round = new HangmanRound("example", expected);
        int actual = round.guessesLeft();
        assertEquals(expected, actual);
    }

    @Test
    void testGuessesLeft() {
        System.out.println("guessesLeft");
        int chances = RANDOM.nextInt(10) + 2;
        HangmanRound round = new HangmanRound("example", chances);
        for (int expected = chances; expected > 0; expected--) {
            int actual = round.guessesLeft();
            assertEquals(expected, actual);
            round.isPresent('a');
        }
    }

    @Test
    void testNumberOfGuessesEnforced() {
        int chances = RANDOM.nextInt(10) + 2;
        HangmanRound round = new HangmanRound("example", chances);
        for (int guessesLeft = chances; guessesLeft > 0; guessesLeft--) {
            round.isPresent('a');
        }
        String msg = "One more chance than allowed should cause exception";
        Throwable t = assertThrows(IllegalStateException.class, () -> {
            round.isPresent('z');
            System.out.println("Should not have been able to use "
                    + (chances + 1) + " chances when only " + chances
                    + " chances were given");
        }, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Message should not be null";
        System.out.println("\"" + excMsg + "\"");
    }

    @Test
    void testConstructorRejectsNullString() {
        Throwable t = assertThrows(NullPointerException.class, () -> {
            HangmanRound badRound = new HangmanRound(null);
            System.out.println("Should not have been able to create " + badRound
                    + " with null String as the mystery word");
        }, "Constructor should reject null");
        String excMsg = t.getMessage();
        assert excMsg != null : "Message should not be null";
        System.out.println("\"" + excMsg + "\"");
    }

    @Test
    void testConstructorRejectsEmptyString() {
        Throwable t = assertThrows(IllegalArgumentException.class, () -> {
            HangmanRound badRound = new HangmanRound("");
            System.out.println("Should not have been able to create " + badRound
                    + " with \"\" as the mystery word");
        }, "Constructor should reject \"\"");
        String excMsg = t.getMessage();
        assert excMsg != null : "Message should not be null";
        System.out.println("\"" + excMsg + "\"");
    }

}
