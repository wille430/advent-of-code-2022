import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.System.out;

public class Main {
    private static final String FILE_PATH = "./input.txt";

    public static void main(String[] args) {
        Main main = new Main();
        List<Ordering> order = main.getInput().stream().map(main::compare).toList();
        out.println(order);
        out.println(main.indicesOfLT(order));
    }

    public int indicesOfLT(List<Ordering> orders) {
        int sum = 0;

        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i) == Ordering.LT) {
                sum += i + 1;
            }
        }

        return sum;
    }

    public List<Pair> getInput() {
        File file = new File(FILE_PATH);
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<Pair> pairs = new ArrayList<>();
        List<String> input = new ArrayList<>();
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (!line.isEmpty()) {
                input.add(line);
            }
        }

        for (int i = 0; i < input.size(); i += 2) {
            pairs.add(new Pair(
                    parseLine(input.get(i)),
                    parseLine(input.get(i + 1))
            ));
        }

        return pairs;
    }

    private Deque<String> parseLine(String line) {
        List<Character> chars = line.chars().mapToObj(c -> (char) c).toList();
        Deque<String> ret = new ArrayDeque<>();
        StringBuilder builder = new StringBuilder();

        for (char c : chars) {
            if (Character.isDigit(c)) {
                builder.append(c);
                continue;
            }


            if (!builder.isEmpty()) {
                ret.add(builder.toString());
                builder.setLength(0);
            }

            if (c != ',') {
                ret.add(Character.toString(c));
            }
        }

        return ret;
    }

    class Pair {
        Deque<String> left;
        Deque<String> right;

        public Pair(Deque<String> left, Deque<String> right) {
            this.left = left;
            this.right = right;
        }

        public void pop() {
            left.pop();
            right.pop();
        }
    }

    enum Ordering {
        LT,
        GT
    }

    public Ordering compare(Pair pair) {
        if (pair.left.isEmpty()) {
            return Ordering.LT;
        }

        if (pair.right.isEmpty()) {
            return Ordering.GT;
        }

        String rChr = pair.right.peek();
        String lChr = pair.left.peek();

        boolean bothIsDigits = isDigit(rChr) && isDigit(lChr);
        if (bothIsDigits) {
            Ordering ret;
            int order = Integer.parseInt(rChr) - Integer.parseInt(lChr);
            if (order < 0) {
                ret = Ordering.GT;
            } else if (order > 0) {
                ret = Ordering.LT;
            } else {
                pair.pop();
                ret = compare(pair);
            }
            return ret;
        }

        if (isOpen(rChr) && isOpen(lChr)) {
            pair.pop();
            return compare(pair);
        }

        if (!isDigit(rChr) && isDigit(lChr)) {
            pair.right.pop();
            return compare(pair);
        }

        if (isDigit(rChr) && !isDigit(lChr)) {
            pair.left.pop();
            return compare(pair);
        }

        if (isClose(rChr) && !isClose(lChr)) {
            pair.right.pop();
            popUntilClose(pair.left);
            return compare(pair);
        }

        if (!isClose(rChr) && isClose(lChr)) {
            pair.left.pop();
            popUntilClose(pair.right);
            return compare(pair);
        }

        pair.pop();
        return compare(pair);
    }

    private void popUntilClose(Deque<String> deck) {
        String top = deck.peek();
        while (!isClose(top)) {
            deck.pop();

            if (deck.isEmpty()) {
                break;
            }

            top = deck.peek();
        }
    }

    private boolean isOpen(String c) {
        return c.charAt(0) == '[';
    }

    private boolean isClose(String c) {
        return c.charAt(0) == ']';
    }

    private boolean isDigit(String str) {
        return str.chars().mapToObj(Character::isDigit).allMatch(b -> b);
    }
}