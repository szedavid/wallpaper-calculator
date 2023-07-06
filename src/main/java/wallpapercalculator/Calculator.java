package wallpapercalculator;

import wallpapercalculator.exception.InvalidDatasourcePathException;
import wallpapercalculator.exception.RoomReadException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

final class Calculator {
    private static final String DEFAULT_DELIMETER = "x";

    private static final String DEFAULT_UNIT = "feet";

    private final String dataSourcePath;

    private String delimiter = DEFAULT_DELIMETER;

    private String unit = DEFAULT_UNIT;

    private boolean addExtra = true;

    private final List<Room> rooms;

    /**
     * Calculator Calculates the required amount of wallpaper, collect identical rooms and rooms with cubic shape
     *
     * @param dataSourcePath Path to data source file
     */
    public Calculator(final String dataSourcePath) {
        this(dataSourcePath, null, null, null);
    }

    /**
     * Calculator Calculates the required amount of wallpaper, collect identical rooms and rooms with cubic shape
     *
     * @param dataSourcePath Path to data source file
     * @param delimiter      Delimiter of dimensions in file (default: "x")
     * @param addExtra       Adds extra amount if true (default: true)
     * @param unit           Unit of length (default: "feet")
     */
    public Calculator(final String dataSourcePath, final Boolean addExtra, final String unit, final String delimiter) {
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
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setAddExtra(boolean addExtra) {
        this.addExtra = addExtra;
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

    public List<Room> getRooms() {
        return rooms;
    }

    public Double getWallpaperArea() {
        return calculateWallpaperArea(this.rooms, this.addExtra);
    }

    public String getWallpaperAreaAsText() {
        return calculateWallpaperArea(this.rooms, this.addExtra) + " square " + unit + "s";
    }

    public List<Room> getCubicRooms() {
        return collectCubicRooms(this.rooms);
    }

    public Set<Room> getIdenticalRooms() {
        return collectIdenticalRooms(this.rooms);
    }

    public List<Room> readRoomsFromDatasource() {
        var roomsTmp = new ArrayList<Room>();
        String line = null;
        try (FileReader fileReader = new FileReader(dataSourcePath); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            int lineCounter = 1;
            while ((line = bufferedReader.readLine()) != null) {
                var dimensions = Arrays.stream(line.split(this.delimiter))
                        .map(Double::valueOf)
                        .collect(Collectors.toList());
                roomsTmp.add(new Room(lineCounter++, dimensions));
            }
            return roomsTmp;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new RoomReadException("Cannot read dimensions in line: " + line + " using delimiter: " + delimiter
                    + "\nMake sure file is correct and use proper delimiter.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidDatasourcePathException(
                    "Error while reading datasorce at path: " + dataSourcePath + "\n Error message: " + e.getMessage()
            );
        }
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

    public static Double calculateWallpaperArea(List<Room> rooms, boolean addExtra) {
        final AtomicReference<Double> wallpaperAreaTmp = new AtomicReference<>((double) 0);
        rooms.forEach(room -> wallpaperAreaTmp.updateAndGet(v -> (v + room.getArea(addExtra))));
        return wallpaperAreaTmp.get();
    }

    public void printResults() {
        System.out.println("\nCube-shaped rooms in descending order by area:");
        collectCubicRooms(this.rooms).forEach(room -> System.out.println(room.toString()));

        System.out.println("\nRooms that have pairs with same dimensions:");
        collectIdenticalRooms(this.rooms).forEach(room -> System.out.println(room.toString()));

        System.out.println("\nThe required amount of wallpaper: " + getWallpaperAreaAsText());
    }

}
