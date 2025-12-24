package ru.cs.vsu.oop.task2.lopatin_n.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TicketTest {
    @Test
    void testTicketCreation() {
        Ticket ticket = new Ticket(TicketType.TAXI);
        assertEquals(TicketType.TAXI, ticket.getType());
    }

    @Test
    void testTicketEquality() {
        Ticket ticket1 = new Ticket(TicketType.BUS);
        Ticket ticket2 = new Ticket(TicketType.BUS);
        Ticket ticket3 = new Ticket(TicketType.TAXI);

        assertEquals(ticket1, ticket2);
        assertNotEquals(ticket1, ticket3);
    }

    @Test
    void testTicketHashCode() {
        Ticket ticket1 = new Ticket(TicketType.UNDERGROUND);
        Ticket ticket2 = new Ticket(TicketType.UNDERGROUND);
        assertEquals(ticket1.hashCode(), ticket2.hashCode());
    }
}

