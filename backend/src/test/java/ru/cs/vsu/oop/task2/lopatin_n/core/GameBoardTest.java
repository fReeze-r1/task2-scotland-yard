package ru.cs.vsu.oop.task2.lopatin_n.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    @Test
    void testGameBoardCreation() {
        GameBoard board = new GameBoard();
        assertNotNull(board);
    }

    @Test
    void testGetStation() {
        GameBoard board = new GameBoard();
        Station station = board.getStation(1);
        assertNotNull(station);
        assertEquals(1, station.getNumber());
    }

    @Test
    void testGetNonExistentStation() {
        GameBoard board = new GameBoard();
        Station station = board.getStation(999);
        assertNull(station);
    }

    @Test
    void testIsRiverStation() {
        GameBoard board = new GameBoard();
        assertTrue(board.isRiverStation(108));
        assertTrue(board.isRiverStation(115));
        assertTrue(board.isRiverStation(157));
        assertTrue(board.isRiverStation(194));
        assertFalse(board.isRiverStation(1));
    }

    @Test
    void testStationConnections() {
        GameBoard board = new GameBoard();
        Station station1 = board.getStation(1);
        assertNotNull(station1);
        assertFalse(station1.getAvailableTransport().isEmpty());
    }
}

