import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.System.exit;
import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        // main.test();

        List<List<Integer>> grid = main.getInput();
        int count = main.countVisibleTress(grid);
        out.printf("%s trees are visible\n", count);

        int highestScore = main.highestScenicScore(grid);
        out.printf("%s is the highest scenic score\n", highestScore);
    }

    public int highestScenicScore(List<List<Integer>> grid) {
        List<List<Integer>> scores = new ArrayList<>();
        for (int y = 0; y < grid.size(); y++) {
            if (scores.size() == y) {
                scores.add(new ArrayList<>());
            }

            for (int x = 0; x < grid.get(y).size(); x++) {
                scores.get(y).add(x, getScenicScore(x, y, grid));
            }
        }

        return Collections.max(scores.stream().flatMap(Collection::stream).toList());
    }

    public int getScenicScore(int x, int y, List<List<Integer>> grid) {
        return this.getViews(x, y, grid).stream().mapToInt(this::getViewDistance).reduce(1, (s1, s2) -> s1 * s2);
    }

    public int getViewDistance(List<Integer> view) {
        Integer base = view.get(0);
        int distance = view.subList(1, view.size()).stream().map(e -> e < base).toList().indexOf(false);
        return distance == -1 ? view.size() - 1 : distance + 1;
    }

    public int countVisibleTress(List<List<Integer>> grid) {
        int count = 0;
        for (int y = 0; y < grid.size(); y++) {
            for (int x = 0; x < grid.get(y).size(); x++) {
                if (isVisible(x, y, grid)) {
                    count++;
                }
            }
        }
        return count;
    }

    public List<List<Integer>> getInput() {
        File file = new File("./input.txt");
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<List<Integer>> grid = new ArrayList<>();
        while (reader.hasNextLine()) {
            grid.add(Arrays.stream(reader.nextLine().split("")).map(Integer::parseInt).toList());
        }

        return grid;
    }

    public List<List<Integer>> getViews(int x, int y, List<List<Integer>> grid) {
        List<Integer> vertical = grid.stream().map(o -> o.get(x)).toList();
        List<Integer> horizontal = grid.get(y);

        List<Integer> lh = new ArrayList<>(horizontal.subList(0, x + 1));
        List<Integer> rh = horizontal.subList(x, horizontal.size());

        List<Integer> lv = new ArrayList<>(vertical.subList(0, y + 1));
        List<Integer> rv = vertical.subList(y, vertical.size());

        Collections.reverse(lv);
        Collections.reverse(lh);

        return List.of(
                lh,
                rh,
                lv,
                rv
        );
    }

    public boolean isVisible(int x, int y, List<List<Integer>> grid) {
        return getViews(x, y, grid).stream().anyMatch(this::firstIsMax);
    }

    private boolean firstIsMax(List<Integer> list) {
        Integer ele = list.get(0);
        return list.subList(1, list.size()).stream().noneMatch(o -> o >= ele);
    }

    void test() {
        List<List<Integer>> grid = List.of(
                List.of(3, 0, 3, 7, 3),
                List.of(2, 5, 5, 1, 2),
                List.of(6, 5, 3, 3, 2),
                List.of(3, 3, 5, 4, 9),
                List.of(3, 5, 3, 9, 0)
        );
        out.println(isVisible(1, 1, grid));
        out.println(isVisible(2, 1, grid));
        out.println(!isVisible(3, 1, grid));
        out.println(!isVisible(2, 2, grid));
        out.println(!isVisible(1, 3, grid));
        out.println(!isVisible(3, 3, grid));

        out.println(isVisible(0, 3, grid));

        out.println(getViewDistance(List.of(5, 1, 2)) == 2);
        out.println(getViewDistance(List.of(5, 3)) == 1);
        out.println(getViewDistance(List.of(5, 3, 5, 3)) == 2);

        out.println(getScenicScore(2, 1, grid) == 4);
        out.println(getScenicScore(2, 3, grid) == 8);

        exit(0);
    }
}