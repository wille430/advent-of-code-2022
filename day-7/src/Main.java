import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.lang.System.exit;
import static java.lang.System.out;

public class Main {
    public static final int MAX_DIR_SIZE = 100000;
    public static final int TOTAL_DISK_SPACE = 70000000;
    public static final int UNUSED_SPACE = 30000000;

    public static final Path ROOT_DIR_PATH = Path.of(Pattern.quote(File.separator));
    private Directory rootDir = new Directory("/");
    private Path currentDir = ROOT_DIR_PATH;

    public static void main(String[] args) {
        Main main = new Main();
        // main.test();

        main.buildFS();
        out.printf("Total size of directories: %s\n", main.getAnswer1());

        out.printf("Total size of directory to delete: %s\n", main.getAnswer2());
    }

    public int getAnswer1() {
        return getTotalSize().stream()
                .mapToInt(o -> (int) o)
                .filter(o -> o < MAX_DIR_SIZE)
                .sum();
    }

    public int getAnswer2() {
        int spaceToFreeUp = UNUSED_SPACE - (TOTAL_DISK_SPACE - rootDir.getSize());
        return getTotalSize().stream()
                .sorted()
                .filter(e -> e > spaceToFreeUp)
                .toList()
                .get(0);
    }

    public List<Integer> getTotalSize() {
        return getTotalSize(rootDir);
    }

    public List<Integer> getTotalSize(Directory parentDir) {
        List<Integer> dirSizes = new ArrayList<>(List.of(parentDir.getSize()));
        for (Directory dir : parentDir.dirs.values()) {
            dirSizes.addAll(getTotalSize(dir));
        }

        return dirSizes;
    }

    private void buildFS() {
        getInput().forEach(this::parseLine);
    }

    List<String> getInput() {
        File file = new File("./input.txt");
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<String> lines = new ArrayList<>();
        while (reader.hasNextLine()) {
            lines.add(reader.nextLine());
        }

        return lines;
    }

    void parseLine(String line) {
        List<String> words = Arrays.stream(line.split(" ")).toList();
        switch (words.get(0)) {
            case "$" -> parseCmd(words.subList(1, words.size()));
            case "dir" -> addDir(words.get(1));
            default -> addFile(words.get(0));
        }
    }

    private void parseCmd(List<String> args) {
        if ("cd".equals(args.get(0))) {
            cd(args.get(1));
        }
    }

    public void addFile(String str) {
        addFile(Integer.parseInt(str));
    }

    public void addFile(int filesize) {
        getCurrentDir().files.add(filesize);
    }

    public void addDir(String dirname) {
        getCurrentDir().dirs.put(dirname, new Directory(dirname));
    }

    public Directory getCurrentDir() {
        Directory dir = rootDir;
        List<String> subDirs = Arrays.stream(currentDir.toString().split("/")).filter(Predicate.not(String::isEmpty)).toList();
        for (String dirname : subDirs) {
            if (!dir.dirs.containsKey(dirname)) {
                dir.dirs.put(dirname, new Directory(dirname));
            }
            dir = dir.dirs.get(dirname);
        }

        return dir;
    }

    public void cd(String path) {
        switch (path) {
            case ".." -> goBack();
            case "/" -> currentDir = Path.of("/");
            default -> currentDir = currentDir.resolve(path);
        }
    }

    void goBack() {
        currentDir = currentDir.getParent();
    }

    void test() {
        this.currentDir = Path.of("/a");
        goBack();
        out.println(this.currentDir.equals("/"));

        this.currentDir = Path.of("/a/b");
        goBack();
        out.println(this.currentDir.equals("/a"));

        this.currentDir = Path.of("/");
        cd("jmtrrrp");
        out.println(this.currentDir.equals("/jmtrrrp"));

        cd("fmgsql");
        out.println(this.currentDir.equals("/jmtrrrp/fmgsql"));

        exit(0);
    }
}