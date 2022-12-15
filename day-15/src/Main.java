import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;

public class Main {
    private static final String FILE_PATH = "./input.txt";

    public static void main(String[] args) {
        Main main = new Main();
        main.part1();
        main.part2();
    }

    private final List<SignalCoverage> coverage;
    private int minX;
    private int maxX;

    public Main() {
        coverage = getInput();
    }

    public void part1() {
        int y = 2000000;
        out.printf("%s positions where a beacon cannot be present at y=%s\n", rowCoverage(y), y);
    }


    public void part2() {
        out.printf("Tuning frequency: %s\n", getFrequency(getDistressBeacon()));
    }

    public Point getDistressBeacon() {
        for (SignalCoverage sc : coverage) {
            List<Point> borderPoints = getBorder(sc)
                    .stream().filter(p ->
                            p.x >= 0
                                    && p.x <= 4000000
                                    && p.y >= 0
                                    && p.y <= 4000000
                    )
                    .toList();

            for (Point p : borderPoints) {
                if (canContainDistressBeacon(p)) {
                    return p;
                }
            }
        }

        throw new RuntimeException("Distress beacon not found");
    }

    private List<Point> getBorder(SignalCoverage sc) {
        Point left = new Point(sc.center.x - sc.radius - 1, sc.center.y);
        Point right = new Point(sc.center.x + sc.radius + 1, sc.center.y);
        Point top = new Point(sc.center.x, sc.center.y - sc.radius - 1);
        Point bottom = new Point(sc.center.x, sc.center.y + sc.radius + 1);

        List<Point> ps = new ArrayList<>();
        Point last = left;
        while (!last.equals(top)) {
            last = new Point(last);
            last.x++;
            last.y--;
            ps.add(last);
        }

        last = right;
        while (!last.equals(top)) {
            last = new Point(last);
            last.x--;
            last.y--;
            ps.add(last);
        }

        last = left;
        while (!last.equals(bottom)) {
            last = new Point(last);
            last.x++;
            last.y++;
            ps.add(last);
        }


        last = right;
        while (!last.equals(bottom)) {
            last = new Point(last);
            last.x--;
            last.y++;
            ps.add(last);
        }

        return ps;
    }

    private long getFrequency(Point p) {
        return p.x * 4000000L + p.y;
    }

    private int rowCoverage(int y) {
        int count = 0;
        for (int x = minX; x < maxX; x++) {
            if (pointIsInCoverage(new Point(x, y))) {
                count++;
            }
        }

        return count;
    }

    private boolean pointIsInCoverage(Point p) {
        return coverage.stream()
                .anyMatch(o -> o.overlaps(p)
                        && !o.beacon.equals(p));
    }

    private boolean canContainDistressBeacon(Point p) {
        return coverage.stream()
                .noneMatch(o -> o.overlaps(p));
    }

    private List<SignalCoverage> getInput() {
        File file = new File(FILE_PATH);
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<SignalCoverage> coverages = new ArrayList<>();
        while (reader.hasNextLine()) {
            SignalCoverage sc = parseLine(reader.nextLine());
            coverages.add(sc);

            if (minX > sc.center.x - sc.radius) {
                minX = sc.center.x - sc.radius;
            }

            if (maxX < sc.center.x + sc.radius) {
                maxX = sc.center.x + sc.radius;
            }
        }

        return coverages;
    }

    private SignalCoverage parseLine(String line) {
        Pattern pattern = Pattern.compile("(?<=[xy]=)(-)?\\d+");
        Matcher matcher = pattern.matcher(line);
        List<Integer> allMatches = new ArrayList<>();

        while (matcher.find()) {
            allMatches.add(Integer.parseInt(matcher.group()));
        }

        if (allMatches.size() == 4) {
            int x1, y1, x2, y2;

            x1 = allMatches.get(0);
            y1 = allMatches.get(1);
            x2 = allMatches.get(2);
            y2 = allMatches.get(3);

            return new SignalCoverage(new Point(x1, y1), new Point(x2, y2));
        } else {
            throw new IllegalArgumentException("Invalid input");
        }
    }

    public void test() {
        SignalCoverage r = this.coverage.stream().filter(o -> o.center.x == 2
                && o.center.y == 18).findFirst().get();

        out.println(r.overlaps(new Point(-2, 15)));
        out.println(!r.overlaps(new Point(-3, 14)));

        out.println(!pointIsInCoverage(new Point(-4, 10)));
        out.println(!pointIsInCoverage(new Point(-3, 10)));
        out.println(!pointIsInCoverage(new Point(25, 10)));
        out.println(!pointIsInCoverage(new Point(26, 10)));
    }

    public class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(Point p) {
            this(p.x, p.y);
        }

        public int manhattanDistance(Point point) {
            return Math.abs(x - point.x) + Math.abs(y - point.y);
        }

        public boolean equals(Point p) {
            return p.x == x && p.y == y;
        }
    }

    public class SignalCoverage {
        Point center;
        int radius;
        Point beacon;

        public SignalCoverage(Point center, int radius) {
            this.center = center;
            this.radius = radius;
        }

        public SignalCoverage(Point sensor, Point closestBeacon) {
            this(sensor, sensor.manhattanDistance(closestBeacon));
            this.beacon = closestBeacon;
        }

        public boolean overlaps(Point p) {
            return center.manhattanDistance(p) <= radius;
        }
    }
}