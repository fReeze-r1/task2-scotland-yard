package ru.cs.vsu.oop.task2.lopatin_n.components;

import org.junit.jupiter.api.Test;
import ru.cs.vsu.oop.task2.lopatin_n.core.GameBoard;
import ru.cs.vsu.oop.task2.lopatin_n.core.Station;
import ru.cs.vsu.oop.task2.lopatin_n.core.TicketType;
import static org.junit.jupiter.api.Assertions.*;

class MrXTest {
    @Test
    void testMrXCreation() {
        GameBoard board = new GameBoard();
        Station startStation = board.getStation(1);
        MrX mrX = new MrX(startStation, 3);

        assertEquals(4, mrX.getTicketCount(TicketType.TAXI));
        assertEquals(3, mrX.getTicketCount(TicketType.BUS));
        assertEquals(3, mrX.getTicketCount(TicketType.UNDERGROUND));
        assertEquals(2, mrX.getTicketCount(TicketType.DOUBLE_MOVE));
        assertEquals(3, mrX.getTicketCount(TicketType.BLACK));
    }

    @Test
    void testRecordMove() {
        GameBoard board = new GameBoard();
        Station startStation = board.getStation(1);
        MrX mrX = new MrX(startStation, 2);

        mrX.recordMove(5, TicketType.TAXI);
        assertEquals(1, mrX.getMoveCount());
        assertEquals(1, mrX.getMoveHistory().size());
    }

    @Test
    void testShouldReveal() {
        GameBoard board = new GameBoard();
        Station startStation = board.getStation(1);
        MrX mrX = new MrX(startStation, 2);

        assertFalse(mrX.shouldReveal());
        mrX.recordMove(5, TicketType.TAXI);
        mrX.recordMove(6, TicketType.BUS);
        mrX.recordMove(7, TicketType.TAXI);
        assertTrue(mrX.shouldReveal());
    }

    @Test
    void testUseDoubleMove() {
        GameBoard board = new GameBoard();
        Station startStation = board.getStation(1);
        MrX mrX = new MrX(startStation, 2);

        assertTrue(mrX.canUseDoubleMove());
        assertTrue(mrX.useDoubleMove());
        assertTrue(mrX.canUseDoubleMove());
        assertTrue(mrX.useDoubleMove());
        assertFalse(mrX.canUseDoubleMove());
    }
}

