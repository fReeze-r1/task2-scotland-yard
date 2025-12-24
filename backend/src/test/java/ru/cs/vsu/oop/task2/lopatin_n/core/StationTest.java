package ru.cs.vsu.oop.task2.lopatin_n.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StationTest {
    @Test
    void testStationCreation() {
        Station station = new Station(1);
        assertEquals(1, station.getNumber());
    }

    @Test
    void testAddConnection() {
        Station station1 = new Station(1);
        Station station2 = new Station(2);

        station1.addConnection(TicketType.TAXI, station2);
        assertTrue(station1.getAvailableTransport().contains(TicketType.TAXI));
        assertTrue(station1.canReach(station2, TicketType.TAXI));
    }

    @Test
    void testCanReach() {
        Station station1 = new Station(1);
        Station station2 = new Station(2);
        Station station3 = new Station(3);

        station1.addConnection(TicketType.BUS, station2);
        assertTrue(station1.canReach(station2, TicketType.BUS));
        assertFalse(station1.canReach(station3, TicketType.BUS));
    }

    @Test
    void testStationEquality() {
        Station station1 = new Station(5);
        Station station2 = new Station(5);
        Station station3 = new Station(6);

        assertEquals(station1, station2);
        assertNotEquals(station1, station3);
    }
}

