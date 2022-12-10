import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Rope {

    Map<String, Integer> visitedPositions = new HashMap<>();

    private int tailX = 0;
    private int tailY = 0;

    private int headX = 0;
    private int headY = 0;

    public List<String> getCommands() {
        File file = new File("./input_test.txt");
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<String> list = new ArrayList<>();
        while (reader.hasNextLine()) {
            list.add(reader.nextLine());
        }

        return list;
    }

    public void moveHead(String cmd) {

        char direction = cmd.split(" ")[0].charAt(0);
        int count = Integer.parseInt(cmd.split(" ")[1]);

        moveHead(direction, count);
    }

    public void moveHead(char direction, int count) {
        switch (direction) {
            case 'L' -> moveHead(-1, 0, count);
            case 'U' -> moveHead(0, -1, count);
            case 'R' -> moveHead(1, 0, count);
            case 'D' -> moveHead(0, 1, count);
        }
    }

    public void moveHead(int dx, int dy, int count) {
        for (int i = 0; i < count; i++) {
            moveHead(dx, dy);
        }
    }

    public void moveHead(int dx, int dy) {
        int headX2 = headX + dx;
        int headY2 = headY + dy;
        int newDist = distance(headX2, headY2, tailX, tailY);

        if (newDist >= 2) {
            this.addVisitedPos(new Pos(tailX, tailY));

            // move tail towards head
            this.tailX = this.headX;
            this.tailY = this.headY;

            this.addVisitedPos(new Pos(tailX, tailY));
        }

        this.headX = headX2;
        this.headY = headY2;
    }

    public int distance(int x1, int y1, int x2, int y2) {
        return (int) Math.floor(Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
    }

    public int distance(Pos p1, Pos p2) {
        return distance(p1.x, p1.y, p2.x, p2.y);
    }

    protected void addVisitedPos(Pos pos) {
        this.visitedPositions.put(String.format("%s,%s", pos.x, pos.y), 1);
    }
}
