import java.util.ArrayList;
import java.util.List;

public class TenKnotRope extends Rope {

    List<Pos> segments = new ArrayList<>();

    private final int len;

    public TenKnotRope(int len) {
        segments.add(new Pos(0, 0));
        this.len = len;
        this.addVisitedPos(new Pos(0, 0));
    }

    @Override
    public void moveHead(int dx, int dy) {
        Pos head = segments.get(0);
        Pos prevPos = new Pos(head);
        Pos lastPos = segments.get(segments.size() - 1);

        // move head
        head.x += dx;
        head.y += dy;

        if (segments.size() < len) {
            segments.add(new Pos(0, 0));
        }

        for (int i = 1; i < segments.size(); i++) {
            Pos tail = segments.get(i);
            int dist = distance(tail, segments.get(i - 1));
            if (dist >= 2) {
                Pos tmp = new Pos(tail);
                tail.x = prevPos.x;
                tail.y = prevPos.y;
                prevPos = tmp;

                if (i == len - 1) {
                    this.addVisitedPos(tail);
                }
            }
        }
    }
}
