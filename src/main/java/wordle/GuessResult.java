package wordle;

public class GuessResult {

    public final String guess;
    public final LetterResult[] score;

    public GuessResult(String guess, LetterResult[] score) {
        this.guess = guess;
        this.score = score;
    }

    public static GuessResult parseGuessResult(String result) {
        StringBuilder sb = new StringBuilder();
        LetterResult[] letterResults = new LetterResult[5];
        for (int i = 0; i < 5; i++) {
            char c = result.charAt(i*2);
            int num = Integer.parseInt(result.substring(i*2+1, i*2+2));
            letterResults[i] = num == 2 ? LetterResult.CORRECT : (num == 1 ? LetterResult.WRONG_SPOT : LetterResult.NOT_EXISTANT);
            sb.append(c);
        }
        return new GuessResult(sb.toString(), letterResults);
    }

}
