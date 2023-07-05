package wallpapercalculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    @Test
    void calculateWithoutExtra() {
        var rooms = new ArrayList<Room>();
        rooms.add(new Room(1, 2, 3, 4));
        assertEquals(52, Calculator.calculateWorkingArea(rooms, false));
    }

    @Test
    void calculateWithExtra() {
        var rooms = new ArrayList<Room>();
        rooms.add(new Room(1, 2, 3, 4));
        assertEquals(58, Calculator.calculateWorkingArea(rooms, true)); // 52 + (2*3)
    }

    @Test
    void identicalRooms() {
        var identicalRooms = new ArrayList<Room>();
        identicalRooms.add(new Room(1, 2, 3, 4));
        identicalRooms.add(new Room(2, 2, 3, 4));
        identicalRooms.add(new Room(3, 2, 3, 4));

        var nonIdenticalRooms = new ArrayList<Room>();
        nonIdenticalRooms.add(new Room(4, 3, 2, 4));    // similar but not identical
        nonIdenticalRooms.add(new Room(5, 1, 5, 6));    // completely different


        var rooms = new ArrayList<>(identicalRooms);
        rooms.addAll(nonIdenticalRooms);

        Assertions.assertTrue(Calculator.collectIdenticalRooms(rooms).containsAll(identicalRooms));
        nonIdenticalRooms.forEach(room -> Assertions.assertFalse(Calculator.collectIdenticalRooms(rooms).contains(room)));
    }

    @Test
    void cubicRooms() {
        var cubicRooms = new ArrayList<Room>();
        cubicRooms.add(new Room(1, 2, 2, 2));
        cubicRooms.add(new Room(2, 100, 100, 100));

        var nonCubicRooms = new ArrayList<Room>();
        nonCubicRooms.add(new Room(3, 1, 2, 3));
        nonCubicRooms.add(new Room(4, 1, 1, 2));
        nonCubicRooms.add(new Room(4, 2, 1, 1));
        nonCubicRooms.add(new Room(4, 1, 2, 1));
        
        var rooms = new ArrayList<>(cubicRooms);
        rooms.addAll(nonCubicRooms);

        Assertions.assertTrue(Calculator.collectCubicRooms(rooms).containsAll(cubicRooms));
        nonCubicRooms.forEach(room -> Assertions.assertFalse(Calculator.collectIdenticalRooms(rooms).contains(room)));
    }
}