package ui.text;

import hangman.HangmanRound;

import java.util.Scanner;

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

    // TODO: Change to allow external API call when Internet available
    private static String chooseWord() {
        int index = Pseudorandom.nextInt(MAX_WORDS_AVAILABLE_LOCALLY);
        return Pseudorandom.SOME_COMMON_ENGLISH_WORDS[index];
    }

    private static void printHangman(int badGuessCount) {
        System.out.println(HANGMAN_DIAGRAMS[badGuessCount
                % HANGMAN_DIAGRAMS.length]);
    }

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
            }
        }
    }
}
