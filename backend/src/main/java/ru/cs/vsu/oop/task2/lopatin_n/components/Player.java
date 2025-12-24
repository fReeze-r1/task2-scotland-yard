package ru.cs.vsu.oop.task2.lopatin_n.components;

import ru.cs.vsu.oop.task2.lopatin_n.core.Station;
import ru.cs.vsu.oop.task2.lopatin_n.core.Ticket;
import ru.cs.vsu.oop.task2.lopatin_n.core.TicketType;

import java.util.*;

public abstract class Player {
    protected Station currentStation;
    protected final Map<TicketType, Integer> tickets;
    protected final String name;

    public Player(String name, Station startStation) {
        this.name = name;
        this.currentStation = startStation;
        this.tickets = new HashMap<>();
    }

    public Station getCurrentStation() {
        return currentStation;
    }

    public void setCurrentStation(Station station) {
        this.currentStation = station;
    }

    public String getName() {
        return name;
    }

    public void addTicket(TicketType type, int count) {
        tickets.put(type, tickets.getOrDefault(type, 0) + count);
    }

    public void addTicket(Ticket ticket) {
        addTicket(ticket.getType(), 1);
    }

    public boolean hasTicket(TicketType type) {
        return tickets.getOrDefault(type, 0) > 0;
    }

    public int getTicketCount(TicketType type) {
        return tickets.getOrDefault(type, 0);
    }

    public boolean useTicket(TicketType type) {
        int count = tickets.getOrDefault(type, 0);
        if (count > 0) {
            tickets.put(type, count - 1);
            return true;
        }
        return false;
    }

    public Map<TicketType, Integer> getTickets() {
        return Collections.unmodifiableMap(tickets);
    }

    public abstract boolean canMove(Station target, TicketType transport);
}

