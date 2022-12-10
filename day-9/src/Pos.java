public class Pos {
    int x;
    int y;

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pos(Pos pos) {
        this.x = pos.x;
        this.y = pos.y;
    }
}
