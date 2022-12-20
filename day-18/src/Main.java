import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class Main {
    private static final String FILE_PATH = "./input.txt";

    private static final int GRID_HEIGHT = 50;
    private static final int GRID_WIDTH = 50;
    private static final int GRID_DEPTH = 50;

    private static final char EMPTY = '.';
    private static final char LAVA = '#';

    public static void main(String[] args) {
        part1();
    }

    public int exposedSides = 0;

    List<List<List<Character>>> space;

    public static void part1() {
        Main main = new Main();

        int count = main.getExposedSides();
        out.printf("Exposed sides: %s\n", count);
    }

    public Main() {
        this.space = getInput();
    }

    public int getExposedSides() {
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                for (int z = 0; z < GRID_DEPTH; z++) {

                    if (space.get(y).get(x).get(z) == LAVA) {
                        exposedSides += exposedSidesCount(x, y, z);
                    }
                }
            }
        }

        return exposedSides;
    }

    public int exposedSidesCount(int x0, int y0, int z0) {
        int count = 0;

        for (int dy = -1; dy < 2; dy++) {
            for (int dx = -1; dx < 2; dx++) {
                for (int dz = -1; dz < 2; dz++) {
                    if (Math.abs(dx) + Math.abs(dy) + Math.abs(dz) != 1) {
                        continue;
                    }

                    int x = x0 + dx;
                    int y = y0 + dy;
                    int z = z0 + dz;

                    if (!isValidPosition(x, y, z) || space.get(y).get(x).get(z) == EMPTY) {
                        count++;
                    }

                }
            }
        }

        return count;
    }

    public boolean isValidPosition(int x, int y, int z) {
        return x >= 0 && x < GRID_WIDTH
                && y >= 0 && y < GRID_HEIGHT
                && z >= 0 && z < GRID_DEPTH;
    }

    public List<List<List<Character>>> getInput() {
        File file = new File(FILE_PATH);
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<List<List<Character>>> space = get3DSpace();
        while (reader.hasNextLine()) {
            String[] words = reader.nextLine().split(",");
            List<Integer> coords = Arrays.stream(words).map(Integer::parseInt).toList();
            space.get(coords.get(1)).get(coords.get(0)).set(coords.get(2), LAVA);
        }

        return space;
    }

    public List<List<List<Character>>> get3DSpace() {
        List<List<List<Character>>> space = new ArrayList<>();

        for (int y = 0; y < GRID_HEIGHT; y++) {
            space.add(new ArrayList<>());
            for (int x = 0; x < GRID_WIDTH; x++) {
                space.get(y).add(new ArrayList<>());
                for (int z = 0; z < GRID_DEPTH; z++) {
                    space.get(y).get(x).add(EMPTY);
                }
            }
        }

        return space;
    }
}