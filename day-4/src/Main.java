import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;

import static java.lang.System.*;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        // main.test();

        long s = main.toGroupsOf2(main.getInput())
                .stream()
                .map(main::intervalsContainsEachOther)
                .filter(b -> b).count();
        out.printf("Number of full overlaps: %s\n", s);

        long s2 = main.toGroupsOf2(main.getInput())
                .stream()
                .map(main::intervalsOverlaps)
                .filter(b -> b).count();
        out.printf("Number of overlaps: %s\n", s2);
    }

    <T> List<List<T>> toGroupsOf2(List<T> list) {
        if (list.size() % 2 != 0) {
            throw new IllegalArgumentException("The size of the input list must be a multiple of 2.");
        }

        List<List<T>> groups = new ArrayList<>();
        for (int i = 0; i < list.size(); i += 2) {
            groups.add(list.subList(i, i + 2));
        }
        return groups;
    }

    boolean intervalsOverlaps(List<BigInteger> list) {
        return list.subList(1, list.size())
                .stream()
                .anyMatch(o -> o.and(list.get(0)).bitCount() != 0);
    }

    boolean intervalsContainsEachOther(List<BigInteger> intervals) {
        return intervals.subList(1, intervals.size())
                .stream()
                .anyMatch(o -> o.and(intervals.get(0)).equals(o)
                        || o.and(intervals.get(0)).equals(intervals.get(0)));
    }

    List<BigInteger> getInput() {
        File file = new File("./input.txt");
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<BigInteger> list = new ArrayList<>();
        while (reader.hasNextLine()) {
            list.addAll(Arrays.stream(reader.nextLine().split(",")).map(this::intervalToBinStr).toList());
        }
        return list;
    }

    BigInteger intervalToBinStr(String str) {
        int lowerEnd = Integer.parseInt(str.split("-")[0]);
        int higherEnd = Integer.parseInt(str.split("-")[1]);

        String binStr = "1".repeat(Math.max(0, (higherEnd - lowerEnd + 1))) +
                "0".repeat(Math.max(0, lowerEnd - 1));

        return new BigInteger(binStr, 2);
    }

    void test() {
        out.println(intervalToBinStr("5-10"));
        out.println(intervalToBinStr("5-10").equals(new BigInteger(String.valueOf(0b1111110000))));

        out.println(intervalToBinStr("1-1").equals(new BigInteger(String.valueOf(0b1))));

        out.println(intervalToBinStr("48-50").bitCount() == 3);

        List<BigInteger> intervals = List.of(new BigInteger(String.valueOf(0b111100L)), new BigInteger(String.valueOf(0b111100L)));
        out.println(intervalsContainsEachOther(intervals));

        intervals = List.of(intervalToBinStr("34-71"), intervalToBinStr("35-71"));
        out.println(intervalsContainsEachOther(intervals));

        intervals = List.of(intervalToBinStr("89-99"), intervalToBinStr("11-89"));
        out.println(!intervalsContainsEachOther(intervals));

        out.println(toGroupsOf2(List.of(1, 2, 3, 4)));

        out.println(toGroupsOf2(getInput()).stream().map(this::intervalsContainsEachOther).toList().subList(0, 4).equals(List.of(true, true, false, false)));

        out.println(toGroupsOf2(getInput()).stream().map(this::intervalsContainsEachOther).toList().subList(0, 6).equals(List.of(true, true, false, false, false, true)));

        out.println(!intervalsOverlaps(List.of(new BigInteger(String.valueOf(0b000111)), new BigInteger(String.valueOf(0b111000)))));
        out.println(intervalsOverlaps(List.of(new BigInteger(String.valueOf(0b001111)), new BigInteger(String.valueOf(0b111000)))));
        out.println(intervalsOverlaps(List.of(new BigInteger(String.valueOf(0b001111000)), new BigInteger(String.valueOf(0b111000)))));
        out.println(!intervalsOverlaps(List.of(new BigInteger(String.valueOf(0b010101)), new BigInteger(String.valueOf(0b101010)))));

        out.println(intervalsOverlaps(List.of(new BigInteger(String.valueOf(0b000000000011100000)), new BigInteger(String.valueOf(0b000000000111110000)))));

        exit(0);
    }
}