import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        out.println(main.getSignalStrength());
        main.resetRegister();

        CRT crt = new CRT(main.getXRegisterValues());
        crt.draw();
        crt.printDisplay();
    }

    private final static int START_AT_CYCLE = 20;
    private final static int EVERY_X_CYCLES = 40;

    public int getSignalStrength() {
        List<Integer> xRegister = loadInstructions().stream()
                .map(this::getXRegisterValues)
                .flatMap(Collection::stream).toList();

        int sum = 0;
        for (int i = START_AT_CYCLE; i < xRegister.size(); i += EVERY_X_CYCLES) {
            int signalStrength = xRegister.get(i - 2) * i;
            sum += signalStrength;
        }
        return sum;
    }

    public List<String> loadInstructions() {
        File file = new File("./input.txt");
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<String> instructions = new ArrayList<>();
        while (reader.hasNextLine()) {
            instructions.add(reader.nextLine());
        }
        return instructions;
    }

    private int x = 1;

    public List<Integer> getXRegisterValues() {
        return this.loadInstructions().stream().map(this::getXRegisterValues).flatMap(Collection::stream).toList();
    }

    public List<Integer> getXRegisterValues(String instruction) {
        String[] words = instruction.split(" ");
        if (words[0].equals("noop")) {
            return List.of(x);
        } else {
            int op = Integer.parseInt(words[1]);
            List<Integer> list = List.of(x, x + op);
            this.x = x + op;
            return list;
        }
    }

    public void resetRegister() {
        this.x = 1;
    }

}