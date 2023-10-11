package task2;

import java.util.Arrays;
import java.util.Scanner;

import static java.util.Collections.reverseOrder;
import static task2.Util.*;

public class WordSorterWithOOP {

    public static void main(String[] args) {
        var sc = new Scanner(System.in);
        while (true) {
            System.out.println("Enter some words consist of uppercase alphabets separated with comma. Enter \"END\" to exit.");
            String line = sc.nextLine();
            if (line.equals("END")) break;
            String[] words = Arrays.stream(line.split(",+")).filter(s -> !s.isEmpty()).toArray(String[]::new);
            if (invalid(words)) continue;
            System.out.println("[INPUT] " + Arrays.toString(words));
            Arrays.sort(words);
            System.out.println("[ASC] " + Arrays.toString(words));
            Arrays.sort(words, reverseOrder());
            System.out.println("[DESC] " + Arrays.toString(words));
        }
        sc.close();
    }

}
