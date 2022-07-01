package ui.text;

import hangman.HangmanRound;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Scanner;

import randomness.CoinSide;
import randomness.Pseudorandom;

public class Game {

    private static Scanner input;

    private final static int MAX_WORDS_AVAILABLE_LOCALLY
            = Pseudorandom.SOME_COMMON_ENGLISH_WORDS.length;

    public static final String[] HANGMAN_DIAGRAMS
            = {"+---+\n\n    |\n\n    |\n\n    |\n\n   ===",
            "+---+\n\n O  |\n\n    |\n\n    |\n\n   ===",
            "+---+\n\n O  |\n\n |  |\n\n    |\n\n   ===",
            "+---+\n\n O  |\n\n/|  |\n\n    |\n\n   ===",
            "+---+\n\n O  |\n\n/|\\  |\n\n    |\n\n   ===",
            "+---+\n\n O  |\n\n/|\\  |\n\n/   |\n\n   ===",
            "+---+\n\n O  |\n\n/|\\  |\n\n/\\  |\n\n   ==="};

    private static String chooseWordLocally() {
        System.out.println("Choosing word from local list of common words");
        int index = Pseudorandom.nextInt(MAX_WORDS_AVAILABLE_LOCALLY);
        return Pseudorandom.SOME_COMMON_ENGLISH_WORDS[index];
    }

    private static String chooseWord() {
        CoinSide flip = Pseudorandom.flipCoin();
        if (flip.equals(CoinSide.HEADS)) {
            try {
                String word = Pseudorandom.nextWord();
                System.out.println("Choosing word from Random Word API");
                return word;
            } catch (MalformedURLException mfurle) {
                System.err.println("The requested URL is malformed");
                System.err.println("\"" + mfurle.getMessage() + "\"");
            } catch (IOException ioe) {
                System.err.println("Had problem accessing Random Word API");
                System.err.println("\"" + ioe.getMessage() + "\"");
            }
        }
        return chooseWordLocally();
    }

    private static void printHangman(int badGuessCount) {
        System.out.println(HANGMAN_DIAGRAMS[badGuessCount
                % HANGMAN_DIAGRAMS.length]);
    }

    // TODO: Re-implement without looping
    private static void playRound() {
        String mysteryWord = chooseWord();
        HangmanRound round = new HangmanRound(mysteryWord);
        int missedGuessCount = 0;
        StringBuilder missedLetters = new StringBuilder();
        while (round.guessesLeft() > 0 && !round.solved()) {
            printHangman(missedGuessCount);
            System.out.println(round.solvedSoFar());
            System.out.println("Missed letters: " + missedLetters);
            System.out.println("Guess a letter");
            char guess = input.next().toLowerCase().charAt(0);
            System.out.println();
            if (!round.isPresent(guess)) {
                if (missedLetters.toString().indexOf(guess) > -1) {
                    System.out.println("You already guessed that letter.");
                    System.out.println("Guess again: ");
                } else {
                    missedLetters.append(guess);
                    missedLetters.append(' ');
                    missedGuessCount++;
                }
            }
        }
        if (round.solved()) {
            System.out.println("Yes, the secret word is \"" + mysteryWord
                    + "\" You have won!");
        } else {
            System.out.println("Sorry, the word was \"" + mysteryWord + "\"");
            System.out.println("Better luck next time");
        }
    }

    public static void main(String[] args) {
        System.out.println("\nH A N G M A N\n");
        try (Scanner scanner = new Scanner(System.in)) {
            input = scanner;
            boolean keepPlaying = true;
            while (keepPlaying) {
                playRound();
                System.out.println("Do you want to play again? (yes or no)");
                String answer = scanner.next();
                keepPlaying = answer.toLowerCase().startsWith("y");
                System.out.println();
            }
        }
    }
}
