import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();
    private static final Path PATH = Path.of("src", "text.txt");
    private static List<String> LISTS_LETTERS = new ArrayList<>();
    private static List<String> CORRECT_LETTERS = new ArrayList<>();
    private static List<String> WRONG_LETTERS = new ArrayList<>();
    private static Boolean gameOver = false;
    private static String word = null;

    public static void main(String[] args) {
        startGame();
    }

    private static void startGame() {
        do {
            System.out.println("[N]ew game or [E]xit");
            String status = SCANNER.nextLine().toUpperCase();
            if (status.equals("N")) {
                newGame();
                gameOver = false;
            } else if (status.equals("E")) {
                return;
            }
        } while (true);

    }

    private static void newGame() {
        getRandomWordFromFile();

        System.out.println(word.replaceAll("[A-Za-z]", "*"));
        do {
            inputLetterFromConsole();
            printWord();
            if (gameOver) {
                gameOver();
                return;
            }
        } while (true);
    }

    private static boolean checkLetter() {
        String letter = LISTS_LETTERS.get(LISTS_LETTERS.size() - 1);
        if (word.replaceAll(letter, "").length() != word.length()) {
            CORRECT_LETTERS.add(letter);
            return true;
        } else {
            WRONG_LETTERS.add(letter);
            return false;
        }
    }


    private static void printWord() {
        if (checkLetter()) {
            String good = "[^" + String.join("", CORRECT_LETTERS) +
                          "]";
            System.out.println(word.replaceAll(good, "*"));
            if (word.length() == CORRECT_LETTERS.size())
                gameOver = true;
        } else {
            int count = 5 - WRONG_LETTERS.size();
            System.out.println("Wrong letter");
            System.out.println("You have " + count + " mistakes left");
            if (count == 0) {
                System.out.println("You lost. The correct word was: " + word);
                gameOver = true;
            }
        }
        if (WRONG_LETTERS.size() != 0) {
            System.out.print("List of wrong letters: ");
            System.out.println(String.join(", ", WRONG_LETTERS));
        }
    }

    private static void gameOver() {
        CORRECT_LETTERS = new ArrayList<>();
        LISTS_LETTERS = new ArrayList<>();
        WRONG_LETTERS = new ArrayList<>();
    }

    private static void inputLetterFromConsole() {
        System.out.println();
        System.out.println("Type one letter into the console");
        do {
            String letter = SCANNER.nextLine().toUpperCase().replaceAll("[0-9]", "").replaceAll(" ", "");
            if (letter.length() > 1 || letter.equals("")) {
                System.out.println("Type on one letter");
            } else if (LISTS_LETTERS.contains(letter)) {
                System.out.println("This letter has already been");
            } else {
                LISTS_LETTERS.add(letter);
                return;
            }
        } while (true);
    }

    private static void getRandomWordFromFile() {
        try {
            word = Files.readAllLines(PATH)
                    .get(RANDOM.nextInt(40)).toUpperCase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}