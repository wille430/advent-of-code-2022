
import static java.lang.System.out;

public class Main {

    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        Tetris t = new Tetris(7);
        while (t.rockCount < 2022) {
            t.gameLoop();
        }

        int height = t.getTowerHeight();
        out.printf("The tower is %s rocks high\n", height);
    }

    public static void part2() {
        Tetris t = new Tetris(7);
        int i = 0;
        while (t.rockCount < 1_000_000_000_000L) {
            t.gameLoop();

            if (i % 5_000_000 == 0) {
                out.printf("Iteration %s. Rocks: %s\n", i, t.rockCount);
            }

            i++;
        }

        int height = t.getTowerHeight();
        out.printf("The tower is %s rocks high\n", height);
    }
}