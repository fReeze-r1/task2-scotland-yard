package ru.cs.vsu.oop.task2.lopatin_n.components;

import org.junit.jupiter.api.Test;
import ru.cs.vsu.oop.task2.lopatin_n.core.Station;
import ru.cs.vsu.oop.task2.lopatin_n.core.Ticket;
import ru.cs.vsu.oop.task2.lopatin_n.core.TicketType;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void testPlayerCreation() {
        Station station = new Station(1);
        Detective detective = new Detective("Test", station);
        assertEquals("Test", detective.getName());
        assertEquals(station, detective.getCurrentStation());
    }

    @Test
    void testAddTicket() {
        Station station = new Station(1);
        Detective detective = new Detective("Test", station);
        int initialCount = detective.getTicketCount(TicketType.TAXI);
        detective.addTicket(TicketType.TAXI, 5);
        assertEquals(initialCount + 5, detective.getTicketCount(TicketType.TAXI));
    }

    @Test
    void testUseTicket() {
        Station station = new Station(1);
        Detective detective = new Detective("Test", station);
        int initialCount = detective.getTicketCount(TicketType.BUS);
        assertTrue(detective.useTicket(TicketType.BUS));
        assertEquals(initialCount - 1, detective.getTicketCount(TicketType.BUS));
    }

    @Test
    void testUseTicketWhenNoneAvailable() {
        Station station = new Station(1);
        Detective detective = new Detective("Test", station);
        while (detective.hasTicket(TicketType.UNDERGROUND)) {
            detective.useTicket(TicketType.UNDERGROUND);
        }
        assertFalse(detective.useTicket(TicketType.UNDERGROUND));
    }

    @Test
    void testSetCurrentStation() {
        Station station1 = new Station(1);
        Station station2 = new Station(2);
        Detective detective = new Detective("Test", station1);
        detective.setCurrentStation(station2);
        assertEquals(station2, detective.getCurrentStation());
    }
}

