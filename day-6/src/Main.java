import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        int pos = main.findFirstNUniqueChars(main.getInput(), 4);
        out.printf("Found start-of-packet at position %s\n", pos);

        int pos2 = main.findFirstNUniqueChars(main.getInput(), 14);
        out.printf("Found start-of-message at position %s\n", pos2);
    }

    String getInput() {
        File file = new File("./input.txt");
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return reader.nextLine();
    }

    int findFirstNUniqueChars(String sequence, int size) {
        for (int i = 0; i < sequence.length() - size; i++) {
            String subseq = sequence.substring(i, i + size);
            if (isUnique(subseq)) {
                return i + size;
            }
        }

        return -1;
    }

    boolean isUnique(String seq) {
        for (int i = 0; i < seq.length(); i++) {
            for (int j = 0; j < seq.length(); j++) {
                if (i == j) {
                    continue;
                }

                if (seq.charAt(i) == seq.charAt(j)) {
                    return false;
                }
            }
        }

        return true;
    }
}