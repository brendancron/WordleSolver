package duotrigordle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import wordle.AIUtils;
import wordle.Tuple;

public class Duotrigordle {

    public static final int PRINT_THRESHOLD = 100;

    private final ChromeDriver driver;

    private final Board[] boards;

    private int guessNum = 0;

    public Duotrigordle(String[] startingGuesses) {
        driver = new ChromeDriver();
        driver.get("https://duotrigordle.com/");
        boards = new Board[32];
        int i = 0;
        for (WebElement board : driver.findElements(By.className("board"))) {
            boards[i] = new Board(board);
            i++;
        }
        for(String guess : startingGuesses) {
            makeGuess(guess);
        }
    }

    public void makeGuess(String guess) {
        Actions guessAction = new Actions(driver);
        guessAction.sendKeys(guess);
        guessAction.perform();
        //TODO some if valid check here
        Actions enterAction = new Actions(driver);
        enterAction.sendKeys(Keys.ENTER);
        enterAction.perform();
        for(Board board: boards) {
            board.updatePossibilities(guessNum);
        }
        guessNum++;
    }

    public boolean isComplete() {
        boolean complete = true;
        for (Board b : boards) {
            if(!b.isComplete()) {
                complete = false;
                break;
            }
        }
        return complete;
    }

    public void solve() {
        while(!isComplete()) {
            nextGuess();
        }
    }

    public void nextGuess() {
        int min = Integer.MAX_VALUE;
        String[] choices = new String[0];
        Set<String> words = new HashSet<>();
        for(Board b: boards) {
            String[] possibilities = b.getPossibilities();
            words.addAll(Arrays.asList(possibilities));
            if (possibilities.length > 0 && possibilities.length < min) {
                min = possibilities.length;
                choices = possibilities;
            }
        }
        if(choices.length == 1) {
            System.out.println(Arrays.toString(choices));
            makeGuess(choices[0]);
            return;
        }
        String[] wordArray = words.toArray(new String[0]);
        Tuple<String, Double>[] bestGuesses = AIUtils.getBestGuessList(choices, wordArray, false);
        System.out.println(Arrays.toString(bestGuesses));
        makeGuess(bestGuesses[0].fst);
    }

    public static void run(String[] args) {
        Duotrigordle duotrigordle = new Duotrigordle(args);
        duotrigordle.solve();
    }

}
