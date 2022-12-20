import java.util.ArrayList;
import java.util.List;

public class FallingPiece {
    private int x;
    private int y;
    private int width;
    private int height;
    private List<String> shape;

    public FallingPiece(int x, int y, List<String> shape) {
        this.x = x;
        this.y = y;
        this.shape = shape;
        this.height = shape.size();
        this.width = shape.get(0).length();
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<String> getShape() {
        return shape;
    }
}
