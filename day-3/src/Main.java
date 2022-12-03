
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.System.exit;
import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        // main.test();

        int totalPriority = main.getInput().stream().map(main::itemInBoth).mapToInt(main::getPriority).sum();
        out.println("Total priority: " + totalPriority);

        int sumOfGroups = main.groupsOf3(main.getInput()).stream().map(main::commonCharacter).mapToInt(main::getPriority).sum();
        out.println("Total group priority: " + sumOfGroups);
    }

    List<String> getInput() {
        File file = new File("./input.txt");
        Scanner reader;

        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<String> lines = new ArrayList<>();

        while (reader.hasNextLine()) {
            lines.add(reader.nextLine().trim());
        }

        return lines;
    }

    List<List<String>> groupsOf3(List<String> strs) {
        List<List<String>> groups = new ArrayList<>();
        for (int i = 0; i < strs.size(); i += 3) {
            groups.add(strs.subList(i, i + 3));
        }

        return groups;
    }

    char itemInBoth(String backpack) {
        return characterInBothStrings(backpack.substring(0, backpack.length() / 2), backpack.substring(backpack.length() / 2));
    }

    int getPriority(char c) {
        return c - 0x60 + (Character.isUpperCase(c) ? 58 : 0);
    }

    char characterInBothStrings(String s1, String s2) {
        return s1.chars()
                .mapToObj(e -> (char) e)
                .filter(o -> s2.contains(o.toString()))
                .toList().get(0);
    }

    Character commonCharacter(List<String> strings) {
        String baseString = strings.get(0);
        for (char c : baseString.toCharArray()) {
            int count = 1;
            for (String str : strings.subList(1, strings.size())) {
                if (str.contains(Character.toString(c))) {
                    count++;
                }
            }

            if (count >= strings.size()) {
                return c;
            }
        }

        return null;
    }

    void test() {
        // getPriority
        out.println(getPriority('a') == 1);
        out.println(getPriority('z') == 26);

        out.println(getPriority('A') == 27);
        out.println(getPriority('Z') == 52);

        // characterInBothStrings
        out.println(characterInBothStrings("vJrwpWtwJgWr", "hcsFMMfFFhFp") == 'p');

        // commonCharacter
        List<String> strs = new ArrayList<>(List.of(
                "vJrwpWtwJgWrhcsFMMfFFhFp",
                "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL",
                "PmmdzqPrVvPwwTWBwg"));
        out.println(commonCharacter(strs) == 'r');

        out.println(groupsOf3(strs).get(0).equals(strs));

        List<String> group2 = List.of(
                "qhwwQSwStJbHNftS",
                "WlfWSwDftzRltBWVlRDlsmBJPcsZPmcJnmPmFhrn",
                "dLQbQbvGTddTvbjQCbLbhmCrZZPPsshPPPrJZrnF");
        strs.addAll(group2);
        out.println(groupsOf3(strs).get(1).equals(group2));

        exit(0);
    }
}