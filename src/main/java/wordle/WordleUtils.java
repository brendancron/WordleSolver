package wordle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class WordleUtils {

    public static String[] filterGuess(String[] available, GuessResult guessResult) {
        List<String> possibilities = new ArrayList<>(Arrays.asList(available));
        HashMap<Character, ArrayList<Tuple<Integer, LetterResult>>> guessData = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            char c = guessResult.guess.charAt(i);
            LetterResult lr = guessResult.score[i];
            if(! guessData.containsKey(c)) {
                guessData.put(c, new ArrayList<>());
            }
            guessData.get(c).add(new Tuple<>(i, lr));
        }
        for (char c : guessData.keySet()) {
            ArrayList<Tuple<Integer, LetterResult>> data = guessData.get(c);
            int numFound = 0;
            boolean zeroSeen = false;
            for (Tuple<Integer, LetterResult> dataItem : data) {
                int pos = dataItem.fst;
                LetterResult lr = dataItem.snd;
                switch(lr) {
                    case CORRECT:
                        numFound++;
                        possibilities = possibilities.stream().filter(word -> word.charAt(pos) == c).collect(Collectors.toList());
                        break;
                    case WRONG_SPOT:
                        if (zeroSeen) {
                            //TODO ERROR HERE
                            possibilities = new ArrayList<>();
                        }
                        numFound++;
                        possibilities = possibilities.stream().filter(word -> word.charAt(pos) != c).collect(Collectors.toList());
                        break;
                    case NOT_EXISTANT:
                        zeroSeen = true;
                        possibilities = possibilities.stream().filter(word -> word.charAt(pos) != c).collect(Collectors.toList());
                        break;
                }
            }
            final int finalNumFound = numFound;
            if (zeroSeen) {
                possibilities = possibilities.stream().filter(word -> count(word, c) == finalNumFound).collect(Collectors.toList());
            } else {
                possibilities = possibilities.stream().filter(word -> count(word, c) >= finalNumFound).collect(Collectors.toList());
            }
        }
        return possibilities.toArray(new String[0]);
    }

    public static String[] filterGuess(String[] available, String guess, LetterResult[] score) {
        return filterGuess(available, new GuessResult(guess, score));
    }

    public static String[] filterGuesses(String[] available, GuessResult[] guessResults) {
        for(GuessResult guessResult : guessResults) {
            available = filterGuess(available, guessResult);
        }
        return available;
    }

    public static int count(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    public static void run(String[] args) {
        try {
            String[] possibilities = WordBankUtils.getWordBank();
            ArrayList<String> resultStrings = new ArrayList<>(Arrays.asList(args));
            GuessResult[] guessResults = resultStrings.stream()
                .map(GuessResult::parseGuessResult)
                .toArray(GuessResult[]::new);
            possibilities = filterGuesses(possibilities, guessResults);
            Tuple<String, Double>[] bestGuesses = AIUtils.getBestGuessList(possibilities, false);
            System.out.println(Arrays.toString(bestGuesses));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
