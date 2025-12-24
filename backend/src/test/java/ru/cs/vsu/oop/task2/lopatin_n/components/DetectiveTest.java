package ru.cs.vsu.oop.task2.lopatin_n.components;

import org.junit.jupiter.api.Test;
import ru.cs.vsu.oop.task2.lopatin_n.core.GameBoard;
import ru.cs.vsu.oop.task2.lopatin_n.core.Station;
import ru.cs.vsu.oop.task2.lopatin_n.core.TicketType;
import static org.junit.jupiter.api.Assertions.*;

class DetectiveTest {
    @Test
    void testDetectiveCreation() {
        GameBoard board = new GameBoard();
        Station startStation = board.getStation(1);
        Detective detective = new Detective("Holmes", startStation);

        assertEquals(10, detective.getTicketCount(TicketType.TAXI));
        assertEquals(8, detective.getTicketCount(TicketType.BUS));
        assertEquals(4, detective.getTicketCount(TicketType.UNDERGROUND));
    }

    @Test
    void testDetectiveCannotUseBlackTicket() {
        GameBoard board = new GameBoard();
        Station station1 = board.getStation(1);
        Station station2 = board.getStation(2);
        station1.addConnection(TicketType.BLACK, station2);

        Detective detective = new Detective("Holmes", station1);
        assertFalse(detective.canMove(station2, TicketType.BLACK));
    }

    @Test
    void testDetectiveCannotUseDoubleMove() {
        GameBoard board = new GameBoard();
        Station station1 = board.getStation(1);
        Station station2 = board.getStation(2);
        station1.addConnection(TicketType.DOUBLE_MOVE, station2);

        Detective detective = new Detective("Holmes", station1);
        assertFalse(detective.canMove(station2, TicketType.DOUBLE_MOVE));
    }
}

