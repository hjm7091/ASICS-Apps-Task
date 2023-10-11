package task2;

public class Util {

    public static boolean invalid(final String[] words) {
        if (words.length == 0) {
            System.out.println("Please enter some words.");
            return true;
        }
        for (String word : words) {
            if (word.isEmpty()) {
                System.out.println("The input word can't be empty.");
                return true;
            }
            if (!word.matches("[A-Z]+")) {
                System.out.println("The input word should consist of uppercase alphabets.");
                return true;
            }
        }
        return false;
    }

}
