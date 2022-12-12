import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;
import static java.lang.System.out;

public class Main {
    private static final String INPUT_FILE_PATH = "./input_test.txt";

    public static void main(String[] args) {
        Main main = new Main();
        // main.test();
        main.displayGrid(main.getPathGrid(main.getGrid()));
    }

    public NPath nextStep(List<List<Character>> grid) {
        int x = 0;
        int y = 0;

        return nextStep(x, y, getPathGrid(grid));
    }

    public NPath nextStep(int x, int y, List<List<Character>> pathGrid) {
        char availableMoves = pathGrid.get(y).get(x);
        NPath path = new NPath();
        path.height = pathGrid.get(y).get(x);

        for (int i = 0; i < 4; i++) {
            boolean canMove = availableMoves % 2 == 1;
            availableMoves = (char) (availableMoves >> 1);
            int dx = 0, dy = 0;

            switch (i) {
                case 0 -> dy++;
                case 1 -> dx++;
                case 2 -> dy--;
                case 3 -> dx--;
            }

            if (canMove) {

            }
        }
    }

    public void displayGrid(List<List<Character>> grid) {
        for (List<Character> row : grid) {
            out.println(row.stream().map(c ->
                    String.format("%4s", Integer.toBinaryString(c)).replace(' ', '0') + " "
            ).reduce((acc, e) -> acc + e).get());
        }
    }

    public List<List<Character>> getPathGrid(List<List<Character>> grid) {
        List<List<Character>> pathGrid = new ArrayList<>();
        for (int y = 0; y < grid.size(); y++) {
            pathGrid.add(new ArrayList<>());
            for (int x = 0; x < grid.get(y).size(); x++) {
                pathGrid.get(y).add(getAvailablePositions(x, y, grid));
            }
        }
        return pathGrid;
    }

    public char getAvailablePositions(int x, int y, List<List<Character>> grid) {
        List<Pos> positions = List.of(
                new Pos(x - 1, y),
                new Pos(x, y - 1),
                new Pos(x + 1, y),
                new Pos(x, y + 1)
        );
        char availablePositions = 0b0000;

        for (int i = 0; i < positions.size(); i++) {
            Pos pos = positions.get(i);
            availablePositions = (char) (availablePositions << 1);
            if (!outOfBounds(pos.x, pos.y, grid)
                    && getHeightDiff(grid.get(pos.y).get(pos.x), grid.get(y).get(x)) <= 1
            ) {
                availablePositions++;
            }
        }

        return availablePositions;
    }

    public boolean outOfBounds(int x, int y, List<List<Character>> grid) {
        return x < 0
                || y < 0
                || grid.size() <= y
                || grid.get(0).size() <= x;
    }

    public int getHeightDiff(char a, char b) {
        return Math.abs(a - b);
    }

    public List<List<Character>> getGrid() {
        File file = new File(INPUT_FILE_PATH);
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<List<Character>> grid = new ArrayList<>();
        while (reader.hasNextLine()) {
            grid.add(reader.nextLine().chars().mapToObj(c -> (char) c).toList());
        }

        return grid;
    }

    public void test() {

        out.println(getHeightDiff('a', 'b') == 1);
        out.println(getHeightDiff('b', 'a') == 1);
        out.println(getHeightDiff('b', 'b') == 0);
        out.println(getHeightDiff('a', 'c') > 1);

        List<List<Character>> grid = getGrid();
        out.println(getAvailablePositions(0, 0, grid) == 0b0011);

        exit(0);
    }

    class Pos {
        int x;
        int y;

        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class NPath {
        char height;
        NPath left;
        NPath up;
        NPath right;
        NPath down;
    }
}