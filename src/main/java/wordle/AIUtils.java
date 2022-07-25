package wordle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class AIUtils {

    public static double calculateEntropy(String guess, String[] possibilities) {
        double total = possibilities.length;
        double entropy = 0.0;
        for(LetterResult a : LetterResult.values()) {
            for(LetterResult b : LetterResult.values()) {
                for(LetterResult c : LetterResult.values()) {
                    for(LetterResult d : LetterResult.values()) {
                        for(LetterResult e : LetterResult.values()) {
                            LetterResult[] score = {a,b,c,d,e};
                            GuessResult guessResult = new GuessResult(guess, score);
                            double num = WordleUtils.filterGuess(possibilities, guessResult).length;
                            if(num > 0) {
                                double probability = num/total;
                                double information = Math.log(1/probability) / Math.log(2); // Log base 2 (1/prob)
                                entropy += probability * information;
                            }
                        }
                    }
                }
            }
        }
        return entropy;
    }

    public static Tuple<String, Double>[] getBestGuessList(String[] possibilities, boolean doPrint) {
        return getBestGuessList(possibilities, possibilities, doPrint);
    }

    public static Tuple<String, Double>[] getBestGuessList(String[] possibilities, String[] wordList, boolean doPrint) {
        String[] choices = possibilities.clone();
        int total = choices.length;
        Tuple<String, Double>[] choiceData = new Tuple[total];
        for (int i = 0; i < total; i++) {
            String choice = choices[i];
            double entropy = calculateEntropy(choice, wordList);
            if(doPrint) {
                System.out.println("(" + i + "/" + total + ") " + choice + " total entropy: " + entropy);
            }
            choiceData[i] = new Tuple<>(choice, entropy);
        }
        return sortTuplesHigh(choiceData);
    }

    public static Tuple[] getExpectedValueList(String[] possibilities) {
        int total = possibilities.length;
        AtomicInteger count = new AtomicInteger();
        Tuple[] t = new ArrayList<>(Arrays.asList(possibilities)).stream().map(word -> {
            count.getAndIncrement();
            //System.out.println("Calculating word: " + word + "(" + count + "/" + total + ")");
            return new Tuple<>(word, getExpectedValue(word, possibilities));
        }).toArray(Tuple[]::new);
        return sortTuplesLow(t);
    }

    private static Tuple<String, Double>[] sortTuplesHigh(Tuple<String, Double>[] choiceData) {
        Arrays.sort(choiceData, (a,b) -> {
            if(a.snd instanceof Comparable && b.snd instanceof Comparable) {
                Comparable as = (Comparable) a.snd;
                Comparable bs = (Comparable) b.snd;
                return bs.compareTo(as);
            } else {
                throw new IllegalArgumentException("Must be comparable");
            }
        });
        return choiceData;
    }

    private static Tuple<String, Double>[] sortTuplesLow(Tuple<String, Double>[] choiceData) {
        Arrays.sort(choiceData, (a,b) -> {
            if(a.snd instanceof Comparable && b.snd instanceof Comparable) {
                Comparable as = (Comparable) a.snd;
                Comparable bs = (Comparable) b.snd;
                return as.compareTo(bs);
            } else {
                throw new IllegalArgumentException("Must be comparable");
            }
        });
        return choiceData;
    }

    public static double getExpectedValue(String[] possibilities) {
        double expectedMin = -1.0;
        for (String guess: possibilities) {
            double e_guess = getExpectedValue(guess, possibilities);
            if (expectedMin < 0 || e_guess < expectedMin) {
                expectedMin = e_guess;
            }
        }
        return expectedMin;
    }

    public static double getExpectedValue(String guess, String[] possibilities) {
        double total = (double) possibilities.length;
        double expectedValue = 0.0;
        for (LetterResult lr0 : LetterResult.values()) {
            for (LetterResult lr1 : LetterResult.values()) {
                for (LetterResult lr2 : LetterResult.values()) {
                    for(LetterResult lr3 : LetterResult.values()) {
                        for(LetterResult lr4 : LetterResult.values()) {
                            LetterResult[] score = {lr0,lr1,lr2,lr3,lr4};
                            String[] remainders = WordleUtils.filterGuess(possibilities, guess, score);
                            double num = (double) remainders.length;
                            // In wordle the answer must be present in the result. It makes no sense to have no output set
                            if (num > 0) {
                                double probability = num/total;
                                if (remainders[0].equals(guess)) {
                                    //This means you got it correct!!!!
                                    //increase the expected value by 1 * probability
                                    expectedValue += probability;
                                } else {
                                    //All other scenarios
                                    //increase the expected value by probability * (1+E(others))
                                    expectedValue += probability * (1.0 + getExpectedValue(remainders));
                                }
                            }
                        }
                    }
                }
            }
        }
        return expectedValue;
    }

    public static void logSimulation() {
        BufferedWriter bw = null;
        try {
            //Specify the file name and path here
            File file = new File("./src/main/resources/log.txt");

            /* This logic will make sure that the file
             * gets created if it is not present at the
             * specified location*/
            file.createNewFile();

            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);

            //START THE SIMULATION HERE
            String[] wordbank = WordBankUtils.getWordBank();
            for(int  i = 0; i < 301; i++) {
                long startTime = System.currentTimeMillis();
                String[] currentRange = Arrays.copyOfRange(wordbank, 0, i);
                getExpectedValueList(currentRange);
                long elapsed = System.currentTimeMillis() - startTime;
                System.out.println("Num: " + i + ", Time: " + elapsed);
                bw.write(i + " " + elapsed + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(bw != null) {
                    bw.close();
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void run(String[] args) {
        try {
            String[] wordbank = WordBankUtils.getWordBank();
            String[] possibilities = WordleUtils.filterGuess(wordbank, GuessResult.parseGuessResult("r0e0a0c0t0"));
            Tuple<String, Double>[] bestGuesses = getBestGuessList(possibilities, true);
            System.out.println(Arrays.toString(bestGuesses));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
