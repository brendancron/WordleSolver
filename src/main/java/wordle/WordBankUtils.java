package wordle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordBankUtils {

    private static String[] wordBank;

    public static String[] getWordBank() {
        if(wordBank == null) {
            try {
                wordBank = getWordBankHelper();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return wordBank.clone();
    }

    private static String[] getWordBankHelper() throws IOException {
        List<String> wordBank = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("./src/main/resources/wordbank.txt"));
        try {
            String line = br.readLine();
            while (line != null) {
                line = br.readLine();
                if(line != null && line.length() == 5) {
                    wordBank.add(line);
                }
            }
        } finally {
            br.close();
        }
        return wordBank.toArray(new String[0]);
    }

    public static void main(String[] args) {
        try {
            String[] wordBank = getWordBank();
            System.out.println(Arrays.toString(wordBank));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
