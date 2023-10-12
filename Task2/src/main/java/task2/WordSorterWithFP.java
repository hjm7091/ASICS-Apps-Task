package task2;

import java.util.Arrays;
import java.util.Scanner;

import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;
import static task2.Util.haveInvalid;

public class WordSorterWithFP {

    public static void main(String[] args) {
        var sc = new Scanner(System.in);
        while (true) {
            System.out.println("Enter some words consist of uppercase alphabets separated with comma. Enter \"END\" to exit.");
            String line = sc.nextLine();
            if (line.equals("END")) break;
            String[] words = Arrays.stream(line.split(",+")).filter(s -> !s.isEmpty()).toArray(String[]::new);
            if (haveInvalid(words)) continue;
            System.out.println("[INPUT] " + Arrays.toString(words));
            System.out.println("[ASC] " + Arrays.stream(words).sorted().collect(toList()));
            System.out.println("[DESC] " + Arrays.stream(words).sorted(reverseOrder()).collect(toList()));
        }
        sc.close();
    }

}
