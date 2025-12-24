package ru.cs.vsu.oop.task2.lopatin_n.core;

public class Ticket {
    private final TicketType type;

    public Ticket(TicketType type) {
        this.type = type;
    }

    public TicketType getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ticket ticket = (Ticket) obj;
        return type == ticket.type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}

