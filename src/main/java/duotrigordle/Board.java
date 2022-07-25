package duotrigordle;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import wordle.GuessResult;
import wordle.LetterResult;
import wordle.WordBankUtils;
import wordle.WordleUtils;

public class Board {

    private final WebElement[][] cells = new WebElement[37][5];

    private String[] possibilities;

    private boolean complete;

    public Board(WebElement board) {
        int row = 0;
        int col = 0;
        for (WebElement cell: board.findElements(By.className("cell"))) {
            cells[row][col] = cell;
            col++;
            if(col > 4) {
                col = 0;
                row++;
            }
        }
        possibilities = WordBankUtils.getWordBank();
    }

    public void updatePossibilities(int num) {
        if(complete) {
            return;
        }
        WebElement[] row = cells[num];
        StringBuilder guessBuilder = new StringBuilder();
        LetterResult[] score = new LetterResult[5];
        for(int i = 0; i < 5; i++) {
            String letter = row[i].getText();
            guessBuilder.append(letter);
            score[i] = getLetterResult(row[i]);
        }
        boolean correctGuess = true;
        for(LetterResult lr : score) {
            if (!lr.equals(LetterResult.CORRECT)) {
                correctGuess = false;
                break;
            }
        }
        if(correctGuess) {
            complete = true;
        }
        String guess = guessBuilder.toString().toLowerCase();
        possibilities = WordleUtils.filterGuess(possibilities, new GuessResult(guess, score));
    }

    public String[] getPossibilities() {
        if(complete) {
            return new String[0];
        }
        return possibilities;
    }

    public static LetterResult getLetterResult(WebElement cell) {
        for(String c : cell.getAttribute("class").split(" ")) {
            if(c.equals("yellow")) {
                return LetterResult.WRONG_SPOT;
            } else if (c.equals("green")) {
                return LetterResult.CORRECT;
            }
        }
        return LetterResult.NOT_EXISTANT;
    }

    public boolean isComplete() {
        return complete;
    }

}
