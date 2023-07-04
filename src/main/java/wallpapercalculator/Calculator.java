package wallpapercalculator;

import wallpapercalculator.exception.InvalidDatasourceException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

final class Calculator {

    private final static String DEFAULT_DELIMETER = "x";

    private final static String DEFAULT_UNIT = "feet";

    private final String dataSource;

    private String delimiter = DEFAULT_DELIMETER;

    private String unit = DEFAULT_UNIT;

    private boolean addExtra = true;

    private Double areaToOrder = null;

    private ArrayList<Room> rooms;

    private ArrayList<Room> cubicRooms;

    private Set<Room> identicalRooms;

    /**
     * Calculator Calculates required amount of wallpaper
     *
     * @param dataSource Path to data source file
     * @param delimiter  Delimeter of dimensions in file (default: "x")
     * @param addExtra   Adds extra amount if true (default: true)
     * @param unit       Unit of length (default: "feet")
     */
    public Calculator(final String dataSource, final String delimiter, final Boolean addExtra, final String unit) {
        if (dataSource != null && dataSource.length() > 0) {
            this.dataSource = dataSource;
        } else {
            throw new InvalidDatasourceException("Invalid datasource path: " + dataSource);
        }
        if (delimiter != null) {
            this.delimiter = delimiter;
        }
        if (unit != null) {
            this.unit = unit;
        }
        if (addExtra != null) {
            this.addExtra = addExtra;
        }

        calculate();
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Room> getCubicRooms() {
        return cubicRooms;
    }

    public Set<Room> getIdenticalRooms() {
        return identicalRooms;
    }

    public void calculate() {
        rooms = new ArrayList<>();
        cubicRooms = new ArrayList<>();
        identicalRooms = new HashSet<>();
        try (FileReader fileReader = new FileReader(dataSource); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            int lineCounter = 1;
            while ((line = bufferedReader.readLine()) != null) {
                var dimensions = Arrays.stream(line.split(this.delimiter))
                        .map(Double::valueOf)
                        .collect(Collectors.toList());
                var room = new Room(lineCounter++, dimensions);
                if (rooms.contains(room)) {
                    identicalRooms.add(rooms.get(rooms.indexOf(room)));
                    identicalRooms.add(room);
                }
                rooms.add(room);
            }

            final AtomicReference<Double> workingArea = new AtomicReference<>((double) 0);
            rooms.forEach(room -> {
                workingArea.updateAndGet(v -> (v + room.getArea(this.addExtra)));
                if (room.isCubic()) {
                    cubicRooms.add(room);
                }
            });
            cubicRooms.sort(Collections.reverseOrder());
            areaToOrder = workingArea.get();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printResults() {
        if (areaToOrder == null) {
            calculate();
        }
        System.out.println("Wallpaper to order: " + areaToOrder + " square " + unit);

        System.out.println("\nCubic shaped rooms descending by area:");
        cubicRooms.forEach(room -> System.out.println(room.toString()));

        System.out.println("\nRooms that have pairs with same dimensions:");
        identicalRooms.forEach(room -> System.out.println(room.toString()));
    }

}
