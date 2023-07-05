package wallpapercalculator;

import wallpapercalculator.exception.InvalidDatasourcePathException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

final class Calculator {
    private static final String DEFAULT_DELIMETER = "x";

    private static final String DEFAULT_UNIT = "feet";

    private static Calculator instance;
    private final String dataSourcePath;

    private String delimiter = DEFAULT_DELIMETER;

    private String unit = DEFAULT_UNIT;

    private boolean addExtra = true;

    private Double areaToOrder = null;

    private final List<Room> rooms;

    private List<Room> cubicRooms;

    private Set<Room> identicalRooms;

    /**
     * Calculator Calculates required amount of wallpaper
     *
     * @param dataSourcePath Path to data source file
     * @param delimiter      Delimeter of dimensions in file (default: "x")
     * @param addExtra       Adds extra amount if true (default: true)
     * @param unit           Unit of length (default: "feet")
     */
    private Calculator(final String dataSourcePath, final Boolean addExtra, final String unit, final String delimiter) {
        if (dataSourcePath != null && dataSourcePath.length() > 0) {
            this.dataSourcePath = dataSourcePath;
        } else {
            throw new InvalidDatasourcePathException("Invalid datasource path: " + dataSourcePath);
        }
        if (unit != null) {
            this.unit = unit;
        }
        if (addExtra != null) {
            this.addExtra = addExtra;
        }
        if (delimiter != null) {
            this.delimiter = delimiter;
        }

        this.rooms = readRoomsFromDatasource();

        calculate();
    }

    public static Calculator getInstance(final String dataSourcePath, final Boolean addExtra, final String unit, final String delimiter) {
        if (instance == null || !instance.dataSourcePath.equals(dataSourcePath) || instance.addExtra != addExtra
                || !instance.unit.equals(unit) || !instance.delimiter.equals(delimiter)) {
            instance = new Calculator(dataSourcePath, addExtra, unit, delimiter);
        }
        return instance;
    }

    public String getDataSourcePath() {
        return dataSourcePath;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isAddExtra() {
        return addExtra;
    }

    public Double getAreaToOrder() {
        return areaToOrder;
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

    private List<Room> readRoomsFromDatasource() {
        var roomsTmp = new ArrayList<Room>();
        try (FileReader fileReader = new FileReader(dataSourcePath); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            int lineCounter = 1;
            while ((line = bufferedReader.readLine()) != null) {
                var dimensions = Arrays.stream(line.split(this.delimiter))
                        .map(Double::valueOf)
                        .collect(Collectors.toList());
                roomsTmp.add(new Room(lineCounter++, dimensions));
            }
            return roomsTmp;
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidDatasourcePathException(
                    "Error while reading datasorce at path: " + dataSourcePath + "\r\n Error message: " + e.getMessage()
            );
        }
    }

    private void calculate() {
        areaToOrder = calculateWorkingArea(this.rooms, this.addExtra);
        cubicRooms = collectCubicRooms(this.rooms);
        identicalRooms = collectIdenticalRooms(this.rooms);
    }

    public static Set<Room> collectIdenticalRooms(List<Room> rooms) {
        var identicalRoomsTmp = new HashSet<Room>();
        rooms.forEach(room -> {
            if (rooms.stream().filter(room::equals).count() > 1) {
                identicalRoomsTmp.add(rooms.get(rooms.indexOf(room)));
                identicalRoomsTmp.add(room);
            }
        });
        return identicalRoomsTmp;
    }

    public static List<Room> collectCubicRooms(List<Room> rooms) {
        var cubicRoomsTmp = new ArrayList<Room>();
        rooms.forEach(room -> {
            if (room.isCubic()) {
                cubicRoomsTmp.add(room);
            }
        });
        cubicRoomsTmp.sort(Collections.reverseOrder());
        return cubicRoomsTmp;
    }

    public static Double calculateWorkingArea(List<Room> rooms, boolean addExtra) {
        final AtomicReference<Double> areaToOrderTmp = new AtomicReference<>((double) 0);
        rooms.forEach(room -> areaToOrderTmp.updateAndGet(v -> (v + room.getArea(addExtra))));
        return areaToOrderTmp.get();
    }

    public void printResults() {
        if (areaToOrder == null) {
            calculate();
        }
        System.out.println("The required amount of wallpaper: " + areaToOrder + " square " + unit);

        System.out.println("\nCube-shaped rooms in descending order by area:");
        cubicRooms.forEach(room -> System.out.println(room.toString()));

        System.out.println("\nRooms that have pairs with same dimensions:");
        identicalRooms.forEach(room -> System.out.println(room.toString()));
    }

}
