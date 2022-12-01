import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class Main {

    static String inputPath = "./input.txt";

    public static void main(String[] args) {
        Main main = new Main();
        try {
            List<Elf> elves = main.getElves();
            elves.sort(Comparator.comparingInt(x -> x.calories));
            Elf answer1 = elves.get(elves.size() - 1);
            out.printf("The elf that is carrying the most calories is carrying %s cals\n", answer1.calories);

            List<Elf> topElves = elves.subList(elves.size() - 3, elves.size());
            int answer2 = topElves.stream().mapToInt(o -> o.calories).sum();

            out.printf("Top three elves are carrying %s cals\n", answer2);
        } catch (FileNotFoundException e) {
            out.printf("%s is not a valid filepath", inputPath);
        }
    }

    class Elf {
        int calories;

        Elf(int calories) {
            this.calories = calories;
        }

        public void addCalories(int cals) {
            this.calories += cals;
        }
    }

    List<Elf> getElves() throws FileNotFoundException {
        List<Elf> elves = new ArrayList<>();

        File inputFile = new File(inputPath);
        Scanner reader = new Scanner(inputFile);

        elves.add(new Elf(0));
        while (reader.hasNextLine()) {
            String line = reader.nextLine();

            if (line.isBlank()) {
                elves.add(new Elf(0));
            } else {
                elves.get(elves.size() - 1).addCalories(Integer.parseInt(line));
            }
        }

        return elves;
    }
}