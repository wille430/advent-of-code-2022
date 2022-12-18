import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TunnelNetwork {

    private Map<Valve, List<Valve>> connections;


    private static final Pattern valveNamePattern = Pattern.compile("(?<=Valve )\\w+");
    private static final Pattern flowRatePattern = Pattern.compile("(?<=flow rate=)\\d+");
    private static final Pattern tunnelPattern = Pattern.compile("(?<=(lead(s)? to valve(s)? )).*");

    private int minutesRemaining = 30;
    private Valve currentValve;
    private int relievedPressure;

    public TunnelNetwork(Map<Valve, List<Valve>> connections, Valve startingValve) {
        this.connections = connections;
        this.currentValve = startingValve;
    }

    public int getOptimalRelievedPressure() {
        while (minutesRemaining >= 1) {
            openCurrentValve();
            moveToNextValve();
        }
        return relievedPressure;
    }

    private void openCurrentValve() {
        if (currentValve.isOpen) {
            return;
        }

        if (currentValve.flowRate <= 0) {
            return;
        }

        openValve(currentValve);
    }

    private void openValve(Valve valve) {
        valve.isOpen = true;
        relievedPressure += valve.flowRate * (minutesRemaining - 1);
    }

    private void moveToNextValve() {
        Valve best = getBestRoute();
        minutesRemaining--;
        currentValve = best;
    }

    private Valve getBestRoute() {
        List<Valve> connectedValves = connections.get(currentValve);
        List<Integer> pressures = connectedValves.stream().map(o -> o.isOpen ? 0 : o.flowRate * (minutesRemaining - 2)).toList();
        OptionalInt maxPressure = pressures.stream().mapToInt(o -> o).max();
        if (maxPressure.isEmpty()) {
            throw new RuntimeException("Could not find optimal route");
        }

        return connectedValves.get(pressures.indexOf(maxPressure.getAsInt()));
    }

    public static TunnelNetwork getFromInput() {
        Map<Valve, List<String>> connections = new HashMap<>();

        File file = new File(Main.FILE_PATH);
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            Matcher m1 = valveNamePattern.matcher(line);
            Matcher m2 = flowRatePattern.matcher(line);
            Matcher m3 = tunnelPattern.matcher(line);

            String valveName, flowRate;
            valveName = flowRate = null;
            String[] tunnels = null;

            if (m1.find()) {
                valveName = m1.group();
            }
            if (m2.find()) {
                flowRate = m2.group();
            }
            if (m3.find()) {
                tunnels = m3.group().split(", ");
            }
            if (valveName == null || tunnels == null || flowRate == null) {
                throw new IllegalArgumentException("Could not parse " + Main.FILE_PATH);
            }

            Valve valve = new Valve(valveName, Integer.parseInt(flowRate));

            connections.put(valve, Arrays.asList(tunnels));
        }

        Map<Valve, List<Valve>> connections2 = new HashMap<>();
        for (Valve valve : connections.keySet()) {
            List<String> valveNames = connections.get(valve);
            List<Valve> connectedValves = connections.keySet()
                    .stream().filter(o -> valveNames.contains(o.name))
                    .toList();

            connections2.put(valve, connectedValves);
        }

        return new TunnelNetwork(connections2,
                connections2.keySet().stream()
                        .filter(o -> o.name.equals("AA"))
                        .findFirst().get()
        );
    }
}
