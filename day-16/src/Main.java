import static java.lang.System.out;

public class Main {
    public static final String FILE_PATH = "./input_test.txt";

    public static void main(String[] args) {
        TunnelNetwork tn = TunnelNetwork.getFromInput();

        out.println("Optimal relieved pressure: " + tn.getOptimalRelievedPressure());
    }
}