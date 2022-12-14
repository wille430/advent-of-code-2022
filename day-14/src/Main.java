import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.mapLibraryName;
import static java.lang.System.out;

public class Main {
    private static final String FILE_PATH = "./input.txt";
    private static final int GRID_WIDTH = 700;
    private static final int GRID_HEIGHT = 600;
    private static final char AIR_CHAR = '.';
    private static final char ROCK_CHAR = '#';
    private static final char SAND_CHAR = 'o';

    private static final int DROP_SAND_X = 500;

    public static void main(String[] args) {
        Main main = new Main();
        main.part1();
        main.part2();
    }

    public void part1() {
        List<List<Character>> grid = getInput();
        fillWithSand(grid);
        out.printf("%s units of sand\n", countSand(grid));
    }

    public void part2() {
        List<List<Character>> grid = getInput();
        addFloor(grid, getDeepestPoint(grid).y + 2);
        fillWithSand(grid);
        out.printf("%s units of sand\n", countSand(grid));
    }

    public void addFloor(List<List<Character>> grid, int y) {
        for (int x = 0; x < grid.get(0).size(); x++) {
            grid.get(y).set(x, ROCK_CHAR);
        }
    }

    public Point getDeepestPoint(List<List<Character>> grid) {
        for (int y = grid.size() - 1; y >= 0; y--) {
            for (int x = 0; x < grid.get(y).size(); x++) {
                if (getChar(grid, new Point(x, y)) == ROCK_CHAR) {
                    return new Point(x, y);
                }
            }
        }

        throw new IllegalArgumentException("Grid is empty");
    }

    public static void displayGrid(List<List<Character>> grid) {
        for (List<Character> row : grid) {
            for (Character c : row) {
                out.print(c);
                out.print(" ");
            }
            out.println();
        }
    }

    public int countSand(List<List<Character>> grid) {
        int count = 0;
        for (int y = 0; y < grid.size(); y++) {
            for (int x = 0; x < grid.get(y).size(); x++) {
                if (getChar(grid, new Point(x, y)) == SAND_CHAR) {
                    count++;
                }
            }
        }
        return count;
    }

    public List<List<Character>> getInput() {
        File file = new File(FILE_PATH);
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<List<Character>> grid = getGrid();
        while (reader.hasNextLine()) {
            parseLine(grid, reader.nextLine());
        }

        return grid;
    }

    private void fillWithSand(List<List<Character>> grid) {
        boolean didDrop = true;
        while (didDrop) {
            try {
                didDrop = dropSand(grid);
            } catch (RuntimeException e) {
                break;
            }
        }
    }

    private boolean dropSand(List<List<Character>> grid) {
        boolean hasDropped = false;
        Point groundPoint = slideSand(grid, groundedPoint(grid, new Point(DROP_SAND_X, 0)));

        if (groundPoint.x < Integer.MAX_VALUE && groundPoint.y < Integer.MAX_VALUE) {
            grid.get(groundPoint.y).set(groundPoint.x, SAND_CHAR);
            hasDropped = true;
        }

        if (groundPoint.y == 0 && groundPoint.x == DROP_SAND_X) {
            throw new RuntimeException("Source is clogged!");
        }

        return hasDropped;
    }

    private Point slideSand(List<List<Character>> grid, Point point) {
        if (point.x < 1) {
            return new Point(Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else if (point.x >= GRID_WIDTH - 1) {
            return new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        } else if (point.y + 1 >= GRID_HEIGHT) {
            return new Point(point.x, Integer.MAX_VALUE);
        } else if (getChar(grid, new Point(point.x, point.y + 1)) == AIR_CHAR) {
            return slideSand(grid, new Point(point.x, point.y + 1));
        } else if (getChar(grid, new Point(point.x - 1, point.y + 1)) == AIR_CHAR) {
            return slideSand(grid, new Point(point.x - 1, point.y + 1));
        } else if (getChar(grid, new Point(point.x + 1, point.y + 1)) == AIR_CHAR) {
            return slideSand(grid, new Point(point.x + 1, point.y + 1));
        }

        return point;
    }

    private char getChar(List<List<Character>> grid, Point point) {
        return grid.get(point.y).get(point.x);
    }

    private Point groundedPoint(List<List<Character>> grid, Point point) {
        for (int dy = 1; dy < GRID_HEIGHT; dy++) {
            if (point.y + dy >= GRID_HEIGHT) {
                return new Point(point.x, Integer.MAX_VALUE);
            }

            if (grid.get(point.y + dy).get(point.x) != AIR_CHAR) {
                return new Point(point.x, point.y + dy - 1);
            }
        }

        throw new RuntimeException("Could not determine grounded point");
    }

    private void parseLine(List<List<Character>> grid, String line) {
        String[] strPoints = line.split(" -> ");
        List<Point> points = Arrays.stream(strPoints).map(Point::new).toList();

        Point from;
        for (int i = 1; i < points.size(); i++) {
            from = points.get(i - 1);
            Point to = points.get(i);
            Point direction = new Point(to.x - from.x, to.y - from.y);

            Point last = from;
            grid.get(from.y).set(from.x, ROCK_CHAR);
            while (direction.x != 0 || direction.y != 0) {
                int dx = Integer.compare(direction.x, 0);
                int dy = Integer.compare(direction.y, 0);

                direction.x -= dx;
                direction.y -= dy;

                grid.get(last.y + dy).set(last.x + dx, ROCK_CHAR);
                last = new Point(last.x + dx, last.y + dy);
            }
            grid.get(to.y).set(to.x, ROCK_CHAR);
        }
    }

    private List<List<Character>> getGrid() {
        List<List<Character>> grid = new ArrayList<>();
        for (int i = 0; i < GRID_HEIGHT; i++) {
            grid.add(new ArrayList<>());
            for (int j = 0; j < GRID_WIDTH; j++) {
                grid.get(i).add(AIR_CHAR);
            }
        }
        return grid;
    }

    public class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(String str) {
            String[] words = str.split(",");
            this.x = Integer.parseInt(words[0]);
            this.y = Integer.parseInt(words[1]);
        }
    }
}