package ru.cs.vsu.oop.task2.lopatin_n.components;

import org.junit.jupiter.api.Test;
import ru.cs.vsu.oop.task2.lopatin_n.core.TicketType;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    void testGameCreation() {
        Game game = new Game();
        assertEquals(Game.GameState.SETUP, game.getState());
    }

    @Test
    void testAddDetective() {
        Game game = new Game();
        game.addDetective("Holmes");
        game.addDetective("Watson");
        assertEquals(2, game.getDetectives().size());
    }

    @Test
    void testStartGame() {
        Game game = new Game();
        game.addDetective("Holmes");
        game.startGame();
        assertEquals(Game.GameState.IN_PROGRESS, game.getState());
        assertNotNull(game.getMrX());
        assertNotNull(game.getDetectives().get(0).getCurrentStation());
    }

    @Test
    void testStartGameWithoutDetectives() {
        Game game = new Game();
        assertThrows(IllegalStateException.class, () -> game.startGame());
    }

    @Test
    void testMakeMrXMove() {
        Game game = new Game();
        game.addDetective("Holmes");
        game.startGame();

        int startStation = game.getMrX().getCurrentStation().getNumber();
        int targetStation = findConnectedStation(game, startStation, TicketType.TAXI);

        if (targetStation > 0) {
            Move move = new Move(targetStation, TicketType.TAXI);
            assertTrue(game.makeMrXMove(move));
            assertEquals(targetStation, game.getMrX().getCurrentStation().getNumber());
        }
    }

    @Test
    void testMakeDetectiveMove() {
        Game game = new Game();
        game.addDetective("Holmes");
        game.startGame();

        Detective detective = game.getDetectives().get(0);
        int startStation = detective.getCurrentStation().getNumber();
        int targetStation = findConnectedStation(game, startStation, TicketType.TAXI);

        if (targetStation > 0) {
            Move move = new Move(targetStation, TicketType.TAXI);
            assertTrue(game.makeDetectiveMove(detective, move));
            assertEquals(targetStation, detective.getCurrentStation().getNumber());
        }
    }

    @Test
    void testDetectivesCannotOccupySameStation() {
        Game game = new Game();
        game.addDetective("Holmes");
        game.addDetective("Watson");
        game.startGame();

        Detective detective1 = game.getDetectives().get(0);
        Detective detective2 = game.getDetectives().get(1);

        int station1 = detective1.getCurrentStation().getNumber();
        int station2 = detective2.getCurrentStation().getNumber();

        if (station1 == station2) {
            int targetStation = findConnectedStation(game, station1, TicketType.TAXI);
            if (targetStation > 0) {
                Move move = new Move(targetStation, TicketType.TAXI);
                assertTrue(game.makeDetectiveMove(detective1, move));
                assertFalse(game.makeDetectiveMove(detective2, move));
            }
        }
    }

    private int findConnectedStation(Game game, int stationNumber, TicketType transport) {
        var station = game.getBoard().getStation(stationNumber);
        if (station != null) {
            var connected = station.getConnectedStations(transport);
            if (!connected.isEmpty()) {
                return connected.iterator().next().getNumber();
            }
        }
        return -1;
    }
}

