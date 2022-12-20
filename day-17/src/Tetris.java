import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class Tetris {
    private static final String FILE_PATH = "./input.txt";

    public static List<List<String>> shapes = List.of(
            List.of("####"),
            List.of(".#.", "###", ".#."),
            List.of("..#", "..#", "###"),
            List.of("#", "#", "#", "#"),
            List.of("##", "##")
    );

    public static final char EMPTY_CHAR = '.';
    public static final char ROCK_CHAR = '#';

    public final int gridHeight;
    public final int gridWidth;

    private FallingPiece fp;
    private String pattern;
    private int shapeIndex;
    private List<List<Character>> grid;
    private boolean hasPushed = false;
    public long rockCount = 0;
    public int heightOffset = 0;

    public Tetris(int width) {
        this.gridHeight = 100;
        this.gridWidth = width;

        shapeIndex = 0;
        pattern = getInput();
        grid = getGrid();
    }

    public int getTowerHeight() {
        return gridHeight - getShallowestRock() + heightOffset;
    }

    public void gameLoop() {
        // this.displayGrid();
        this.spawnPiece();

        if (!hasPushed) {
            this.pushPiece();
            hasPushed = true;
        } else {
            this.moveDownPiece();
            hasPushed = false;
        }
    }

    private void displayGrid() {
        List<List<Character>> grid = mergePiece(this.grid);

        out.print("\033[H\033[2J");
        out.flush();

        for (List<Character> cs : grid) {
            for (Character c : cs) {
                out.print(c);
                out.print(" ");
            }
            out.println();
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void spawnPiece() {
        if (this.fp != null) {
            return;
        }

        int minY = getShallowestRock();

        final int SPAWN_Y_OFFSET = -2;
        final int SPAWN_X_OFFSET = 2;

        var shape = shapes.get(shapeIndex);

        int spawnY = minY + SPAWN_Y_OFFSET - shape.size() - 1;
        int spawnX = SPAWN_X_OFFSET;

        this.fp = new FallingPiece(spawnX, spawnY, shape);
        shapeIndex++;
        shapeIndex %= shapes.size();
    }

    private void pushPiece() {
        char moveDir = pattern.charAt(0);
        pattern = pattern.substring(1) + moveDir;

        int lastX = fp.getX();
        int lastY = fp.getY();

        switch (moveDir) {
            case '>' -> this.fp.move(1, 0);
            case '<' -> this.fp.move(-1, 0);
            default -> throw new RuntimeException("Invalid jet pattern");
        }

        int maxX = grid.get(0).size() - fp.getWidth();
        fp.setX(Math.max(0, Math.min(fp.getX(), maxX)));

        if (pieceOverlaps()) {
            fp.moveTo(lastX, lastY);
        }
    }

    private void moveDownPiece() {
        fp.move(0, 1);
        if (fp.getHeight() + fp.getY() > grid.size()
                || pieceOverlaps()) {
            fp.move(0, -1);
            mergePiece();
        }
    }

    private List<List<Character>> mergePiece(List<List<Character>> grid) {
        List<List<Character>> newGrid = new ArrayList<>();

        for (List<Character> characters : grid) {
            newGrid.add(new ArrayList<>(characters));
        }

        if (fp == null) {
            return newGrid;
        }

        for (int dy = 0; dy < fp.getHeight(); dy++) {
            int y = fp.getY() + dy;
            for (int dx = 0; dx < fp.getWidth(); dx++) {
                int x = fp.getX() + dx;
                char c = fp.getShape().get(dy).charAt(dx);

                if (c != EMPTY_CHAR) {
                    newGrid.get(y).set(x, c);
                }
            }
        }

        return newGrid;
    }

    private void mergePiece() {
        this.grid = mergePiece(this.grid);
        this.fp = null;
        this.rockCount++;

        int filledIndex = indexOfFilledRow();
        int hOffset = gridHeight - filledIndex - 1;

        grid = grid.subList(0, filledIndex + 1);
        resizeGrid();
        heightOffset += hOffset;
    }

    private void resizeGrid() {
        resizeGrid(gridHeight);
    }

    private void resizeGrid(int height) {
        while (grid.size() < height) {
            grid.add(0, new ArrayList<>());

            for (int x = 0; x < gridWidth; x++) {
                grid.get(0).add(EMPTY_CHAR);
            }
        }
    }

    private int indexOfFilledRow() {
        List<Integer> depths = new ArrayList<>();
        for (int x = 0; x < gridWidth; x++) {
            depths.add(Math.min(getShallowestRock(x), gridHeight - 1));
        }

        return Collections.max(depths);
    }

    private boolean pieceOverlaps() {
        for (int dy = 0; dy < fp.getHeight(); dy++) {
            int y = fp.getY() + dy;
            for (int dx = 0; dx < fp.getWidth(); dx++) {
                int x = fp.getX() + dx;

                if (grid.get(y).get(x) != EMPTY_CHAR && fp.getShape().get(dy).charAt(dx) != EMPTY_CHAR) {
                    return true;
                }
            }
        }

        return false;
    }

    // find min y where a rock char exists
    private int getShallowestRock() {
        for (int y = 0; y < grid.size(); y++) {
            if (grid.get(y).contains(ROCK_CHAR)) {
                return y;
            }
        }

        return grid.size();
    }

    private int getShallowestRock(int x) {
        for (int y = 0; y < grid.size(); y++) {
            if (grid.get(y).get(x).equals(ROCK_CHAR)) {
                return y;
            }
        }

        return grid.size();
    }

    private List<List<Character>> getGrid() {
        List<List<Character>> grid = new ArrayList<>();
        for (int y = 0; y < gridHeight; y++) {
            grid.add(new ArrayList<>());
            for (int x = 0; x < gridWidth; x++) {
                grid.get(y).add(EMPTY_CHAR);
            }
        }

        return grid;
    }

    public String getInput() {
        File file = new File(FILE_PATH);
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        StringBuilder builder = new StringBuilder();
        while (reader.hasNextLine()) {
            builder.append(reader.nextLine());
        }

        return builder.toString();
    }
}
