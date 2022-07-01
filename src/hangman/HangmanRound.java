package hangman;

import java.util.Arrays;

public class HangmanRound {

    public static final int DEFAULT_NUMBER_OF_MAX_GUESSES = 10;

    private final int totalChances;

    private final String mysteryWord;

    private boolean hasBeenSolved = false;

    private String uncovered;

    private int usedChances = 0;

    public String solvedSoFar() {
        return this.uncovered;
    }

    // TODO: Re-implement without looping
    public boolean isPresent(char letter) {
        if (this.usedChances == this.totalChances) {
            String excMsg = this.totalChances + " chances already used up";
            throw new IllegalStateException(excMsg);
        }
        boolean found = false;
        char[] letters = this.uncovered.toCharArray();
        for (int i = 0; i < this.mysteryWord.length(); i++) {
            if (this.mysteryWord.charAt(i) == letter) {
                found = true;
                letters[i] = letter;
            }
        }
        this.uncovered = new String(letters);
        this.hasBeenSolved = this.uncovered.equals(this.mysteryWord);
        this.usedChances++;
        return found;
    }

    public int guessesLeft() {
        return this.totalChances - this.usedChances;
    }

    public boolean solved() {
        return this.hasBeenSolved;
    }

    public HangmanRound(String word) {
        this(word, DEFAULT_NUMBER_OF_MAX_GUESSES);
    }

    public HangmanRound(String word, int numberOfGuesses) {
        if (word == null) {
            String excMsg = "Mystery word may be \"null\" but not null";
            throw new NullPointerException(excMsg);
        }
        if (word.isEmpty()) {
            String excMsg = "Mystery word must not be empty";
            throw new IllegalArgumentException(excMsg);
        }
        this.mysteryWord = word;
        this.totalChances = numberOfGuesses;
        char[] letters = this.mysteryWord.toCharArray();
        Arrays.fill(letters, '_');
        this.uncovered = new String(letters);
    }

}
