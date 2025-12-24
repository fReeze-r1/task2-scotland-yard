package ru.cs.vsu.oop.task2.lopatin_n.components;

import ru.cs.vsu.oop.task2.lopatin_n.core.Station;
import ru.cs.vsu.oop.task2.lopatin_n.core.TicketType;

public class Detective extends Player {
    public Detective(String name, Station startStation) {
        super(name, startStation);
        addTicket(TicketType.TAXI, 10);
        addTicket(TicketType.BUS, 8);
        addTicket(TicketType.UNDERGROUND, 4);
    }

    @Override
    public boolean canMove(Station target, TicketType transport) {
        if (transport == TicketType.BLACK || transport == TicketType.DOUBLE_MOVE) {
            return false;
        }
        return hasTicket(transport) &&
               currentStation.getAvailableTransport().contains(transport) &&
               currentStation.canReach(target, transport);
    }
}

