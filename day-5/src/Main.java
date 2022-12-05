import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        Input input = main.getInput();
        Input input2 = new Input(input);

        while (!input.instructions.isEmpty()) {
            input.moveWith9000();
        }
        out.println("Answer 1: " + getOutputMsg(input));

        while (!input2.instructions.isEmpty()) {
            input2.moveWith9001();
        }

        out.println("Answer 2: " + getOutputMsg(input2));
    }

    static String getOutputMsg(Input input) {
        return String.join("", input.stacks.stream()
                .map(o -> o.isEmpty() ? ""
                        : Character.toString(o.peek().charAt(1))
                )
                .toList());
    }

    Input getInput() {
        File file = new File("./input.txt");
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Input input = new Input(9);

        getStacks(reader, input);
        getInstruction(reader, input);

        return input;
    }

    void getStacks(Scanner reader, Input input) {
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.charAt(0) != '[') {
                break;
            }

            Deque<String> row = new ArrayDeque<>(groupsOf(line, 3));

            for (Deque<String> stack : input.stacks) {
                if (!row.isEmpty() && !row.peek().isBlank()) {
                    stack.add(row.pop());
                } else {
                    row.pop();
                }
            }
        }
    }

    void getInstruction(Scanner reader, Input input) {
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.isBlank()) {
                continue;
            }

            StringBuilder builder = new StringBuilder();
            String[] words = line.split(" ");
            for (int i = 0; i < words.length; i++) {
                if (i % 2 != 0) {
                    builder.append(words[i]);
                    builder.append(",");
                }
            }
            builder.deleteCharAt(builder.length() - 1); // Remove last comma

            input.instructions.add(builder.toString());
        }
    }

    List<String> groupsOf(String str, int size) {
        List<String> groups = new ArrayList<>();

        for (int i = 0; i < str.length(); i += size + 1) {
            groups.add(str.substring(i, i + size));
        }

        return groups;
    }

    public static class Input {
        List<Deque<String>> stacks;
        Deque<String> instructions;

        Input(int stackSize) {
            stacks = new ArrayList<>();
            for (int i = 0; i < stackSize; i++) {
                stacks.add(new ArrayDeque<>());
            }
            instructions = new ArrayDeque<>();
        }

        Input(Input input) {
            this.instructions = new ArrayDeque<>(input.instructions);
            this.stacks = new ArrayList<>();
            for (Deque<String> stack : input.stacks) {
                this.stacks.add(new ArrayDeque<>(stack));
            }
        }

        private int[] parseInstruction(String instruction) {
            String[] parts = instruction.split(",");
            int count = Integer.parseInt(parts[0]);
            int from = Integer.parseInt(parts[1]);
            int to = Integer.parseInt(parts[2]);

            return new int[]{count, from, to};
        }

        Input moveWith9000() {
            int[] arr = this.parseInstruction(this.instructions.pop());
            int count = arr[0], from = arr[1], to = arr[2];

            for (int i = 0; i < count; i++) {
                stacks.get(to - 1).push(stacks.get(from - 1).pop());
            }

            return this;
        }

        Input moveWith9001() {
            int[] arr = this.parseInstruction(this.instructions.pop());
            int count = arr[0], from = arr[1], to = arr[2];

            Deque<String> subDeque = new ArrayDeque<>();
            for (int i = 0; i < count; i++) {
                subDeque.push(stacks.get(from - 1).pop());
            }

            for (String str : subDeque) {
                stacks.get(to - 1).push(str);
            }

            return this;
        }
    }
}