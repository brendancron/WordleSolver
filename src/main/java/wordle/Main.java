package wordle;

import java.util.Arrays;

import duotrigordle.Duotrigordle;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            return;
        }
        String command = args[0];
        String[] arguments = Arrays.copyOfRange(args,1, args.length);
        if (command.equals("wordle")) {
            WordleUtils.run(arguments);
        } else if(command.equals("ai")) {
            AIUtils.run(arguments);
        } else if(command.equals("duotrigordle")) {
            Duotrigordle.run(arguments);
        }
    }

}
