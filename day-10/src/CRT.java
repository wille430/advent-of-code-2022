import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.System.out;

public class CRT {

    private final static char LIT_PIXEL = '#';
    private final static char DARK_PIXEL = '.';

    private List<Integer> xRegister;

    private List<Character> display;
    private static final int HEIGHT = 6;
    private static final int WIDTH = 40;
    private static final int SPRITE_WIDTH = 3;

    public CRT(List<Integer> xRegister) {
        this.xRegister = xRegister;
        this.display = new ArrayList<>(Collections.nCopies(HEIGHT * WIDTH, DARK_PIXEL));
    }

    public void draw() {
        int pc = 0;
        boolean hasExecuted = false;
        for (int i = 0; i < HEIGHT * WIDTH; i++) {
            List<Character> row = new ArrayList<>(Collections.nCopies(WIDTH, DARK_PIXEL));
            drawSprite(xRegister.get(pc), SPRITE_WIDTH, row);
            int width = i % WIDTH;

            if (hasExecuted) {
                pc = i;
            } else {
                hasExecuted = true;
            }
            this.display.set(i, row.get(width));
        }
    }

    public void drawSprite(int x, int width, List<Character> row) {
        int leftX = x - (SPRITE_WIDTH / 2);

        for (int i = 0; i < width; i++) {
            int ri = i + leftX;
            if (ri >= 0 && ri < WIDTH) {
                row.set(ri, LIT_PIXEL);
            }
        }
    }

    public void printDisplay() {
        for (int i = 0; i < HEIGHT * WIDTH; i++) {
            int width = i % WIDTH;

            out.print(this.display.get(i));

            if (width == WIDTH - 1) {
                out.println();
            }
        }
    }
}
