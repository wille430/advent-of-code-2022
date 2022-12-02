import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();

        try {
            int score = main.getInput().stream().mapToInt(main::calculateScore).sum();
            out.printf("Part 1: Total score: %s\n", score);

            score = main.getInput().stream()
                    .map(main::choosePlay)
                    .mapToInt(main::calculateScore).sum();

            out.printf("Part 2: Total score: %s", score);
        } catch (FileNotFoundException e) {
            out.println("Could not find input file");
        }
    }

    int[] choosePlay(int[] plays) {
        int p1 = plays[0];
        int p2 = plays[1];

        if (p2 == 1) { // Lose
            p2 = p1 == 1 ? 3 : p1 - 1;
        } else if (p2 == 2) { // Draw
            p2 = p1;
        } else { // Win
            p2 = Math.max((p1 + 1) % 4, 1);
        }
        plays[1] = p2;

        return plays;
    }

    int calculateScore(int[] plays) {
        int p1 = plays[0];
        int p2 = plays[1];

        if (p1 == p2) {
            return p2 + 3; // draw
        } else if (p1 - p2 == -1 || p1 - p2 == 2) {
            return p2 + 6; // win
        } else {
            return p2; // loss
        }
    }

    List<int[]> getInput() throws FileNotFoundException {
        File file = new File("./guide.txt");
        Scanner reader = new Scanner(file);

        List<int[]> guide = new ArrayList<>();

        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            int p1 = charToInt(line.charAt(0));
            int p2 = charToInt(line.charAt(2));
            guide.add(new int[]{p1, p2});
        }

        return guide;
    }

    int charToInt(char ch) {
        return switch (ch) {
            case 'A', 'X' -> 1;
            case 'B', 'Y' -> 2;
            case 'C', 'Z' -> 3;
            default -> 0;
        };
    }
}