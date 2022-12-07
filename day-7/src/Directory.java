import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Directory {

    String name;
    List<Integer> files = new ArrayList<>();
    Map<String, Directory> dirs = new HashMap<>();

    public Directory(String name) {
        this.name = name;
    }

    public int getSize() {
        return this.files.stream().mapToInt(e -> (int) e).sum()
                + dirs.values().stream().mapToInt(Directory::getSize).sum();
    }
}
