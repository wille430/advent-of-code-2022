import java.util.*;

import static java.lang.System.out;

public class Main {


    public static void main(String[] args) {
        Rope rope = new TenKnotRope(2);
        rope.getCommands().forEach(rope::moveHead);

        answerMessage(rope.visitedPositions.size());

        rope = new TenKnotRope(10);
        // rope.getCommands().forEach(rope::moveHead);
        rope.moveHead("R 5");
        rope.moveHead("U 4");
        answerMessage(rope.visitedPositions.size());
    }

    public static void answerMessage(int answer) {
        out.printf("Tail visited %s unique positions\n", answer);
    }
}