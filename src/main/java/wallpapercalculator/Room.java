package wallpapercalculator;

import wallpapercalculator.exception.InvalidDimensionsException;

import java.util.List;

public class Room implements Comparable<Room> {

    private final int positionInList;

    private final double length;

    private final double width;

    private final double height;

    /**
     * Room
     *
     * @param positionInList The position in the datasource
     * @param length
     * @param width
     * @param height
     */
    public Room(final int positionInList, final double length, final double width, final double height) {
        this.positionInList = positionInList;
        this.length = length;
        this.width = width;
        this.height = height;
    }

    /**
     * Room
     *
     * @param positionInList The position in the datasource
     * @param dimension
     */
    public Room(final int positionInList, final List<Double> dimension) {
        if (dimension.size() != 3) {
            throw new InvalidDimensionsException("Invalid dimension array! Expected length: 3, received:" + dimension.size());
        }
        this.positionInList = positionInList;
        this.length = dimension.get(0);
        this.width = dimension.get(1);
        this.height = dimension.get(2);
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean isCubic() {
        return length == width && width == height;
    }

    public double getArea(boolean addExtra) {
        double areaLengthWidth = length * width;
        double areaWidthHeight = width * height;
        double areaHeightLength = height * length;
        double area = 2 * (areaLengthWidth + areaWidthHeight + areaHeightLength);
        if (addExtra) {
            area += Math.min(areaLengthWidth, Math.min(areaWidthHeight, areaHeightLength));
        }
        return area;
    }

    @Override
    public int compareTo(Room other) {
        return Double.compare(this.getArea(false), other.getArea(false));
    }

    @Override
    public String toString() {
        return positionInList + ". room (length=" + length + ", width=" + width + ", height=" + height + ")";
    }

    /**
     * Rooms are identical if the individual dimensions are the same
     * (length = length, width = width, height = height)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Room other = (Room) obj;
        return length == other.length && width == other.width && height == other.height;
    }

}
