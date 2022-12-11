import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();


        long monkeyBusiness = main.getMonkeyBusiness(20, false);
        out.printf("Monkey business: %s\n", monkeyBusiness);

        long monkeyBusiness2 = main.getMonkeyBusiness(10000, true);
        out.printf("Monkey business: %s\n", monkeyBusiness2);
    }

    public interface WorryRelief {
        public long operation(long a);
    }

    public long getMonkeyBusiness(int rounds, boolean shouldWorry) {
        List<Monkey> monkeys = getMonkeys();

        WorryRelief worryRelief = (item) -> item / 3;
        if (shouldWorry) {
            worryRelief = (item) -> item % monkeys.stream().map(o -> o.divisibleTest).reduce(1, (l1, l2) -> l1 * l2);
        }

        for (int i = 0; i < rounds; i++) {
            runRound(monkeys, worryRelief);
        }

        Stream<Long> top2Monkeys = monkeys.stream()
                .map(o -> o.inspectionCount)
                .sorted((s1, s2) -> Long.compare(s2, s1))
                .limit(2);
        return top2Monkeys.reduce(1L, (s1, s2) -> s1 * s2);
    }

    public List<Monkey> getMonkeys() {
        File file = new File("./input.txt");
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<String> lines = new ArrayList<>();
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (!line.isBlank()) {
                lines.add(line);
            }
        }

        List<Monkey> monkeys = new ArrayList<>();
        for (int i = 0; i < lines.size(); i += 6) {

            Deque<Long> items = new ArrayDeque<>();
            for (String item : Arrays.stream(lines.get(i + 1).split(" ")).filter(o -> !o.isEmpty()).skip(2).toList()) {
                items.add(Long.parseLong(item.trim().replace(",", "")));
            }

            Monkey monkey = new Monkey(
                    items,
                    lines.get(i + 2).substring(13),
                    Integer.parseInt(lines.get(i + 3).split(" ")[lines.get(i + 3).split(" ").length - 1]),
                    Integer.parseInt(lines.get(i + 4).split(" ")[lines.get(i + 4).split(" ").length - 1]),
                    Integer.parseInt(lines.get(i + 5).split(" ")[lines.get(i + 5).split(" ").length - 1])
            );
            monkeys.add(monkey);
        }

        return monkeys;
    }

    public long evaluateExpression(String expr, long operand) {
        final int OPERATOR_INDEX = 3;

        String[] words = expr.split(" ");
        long a, b;
        a = operand;
        b = parseLong(words[OPERATOR_INDEX + 1], operand);

        return switch (words[OPERATOR_INDEX]) {
            case "*" -> a * b;
            case "+" -> a + b;
            default -> operand;
        };
    }

    public void runRound(List<Monkey> monkeys, WorryRelief worryRelief) {
        for (Monkey monkey : monkeys) {
            int size = monkey.items.size();
            for (int i = 0; i < size; i++) {
                inspect(monkey, monkeys, worryRelief);
            }
        }
    }

    public void inspect(Monkey monkey, List<Monkey> monkeys, WorryRelief worryRelief) {
        monkey.inspectionCount++;
        Long item = monkey.items.pop();
        item = evaluateExpression(monkey.operation, item);
        item = worryRelief.operation(item);

        Monkey throwToMonkey;
        if (item % monkey.divisibleTest == 0) {
            throwToMonkey = monkeys.get(monkey.testTrueThrowTo);
        } else {
            throwToMonkey = monkeys.get(monkey.testFalseThrowTo);
        }

        throwToMonkey.items.add(item);
    }

    private long parseLong(String num, long old) {
        return switch (num) {
            case "old" -> old;
            default -> Integer.parseInt(num);
        };
    }

    class Monkey {
        Deque<Long> items;
        String operation;
        int divisibleTest;
        int testTrueThrowTo;
        int testFalseThrowTo;

        long inspectionCount = 0;

        public Monkey(
                Deque<Long> items,
                String operation,
                int divisibleTest,
                int testTrueThrowTo,
                int testFalseThrowTo
        ) {
            this.items = items;
            this.operation = operation;
            this.divisibleTest = divisibleTest;
            this.testTrueThrowTo = testTrueThrowTo;
            this.testFalseThrowTo = testFalseThrowTo;
        }
    }
}