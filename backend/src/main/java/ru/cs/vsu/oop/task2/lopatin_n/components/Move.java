package ru.cs.vsu.oop.task2.lopatin_n.components;

import ru.cs.vsu.oop.task2.lopatin_n.core.TicketType;

public class Move {
    private final int stationNumber;
    private final TicketType ticketType;
    private final boolean isDoubleMove;
    private final Integer secondStationNumber;
    private final TicketType secondTicketType;

    public Move(int stationNumber, TicketType ticketType) {
        this.stationNumber = stationNumber;
        this.ticketType = ticketType;
        this.isDoubleMove = false;
        this.secondStationNumber = null;
        this.secondTicketType = null;
    }

    public Move(int firstStationNumber, TicketType firstTicketType,
                int secondStationNumber, TicketType secondTicketType) {
        this.stationNumber = firstStationNumber;
        this.ticketType = firstTicketType;
        this.isDoubleMove = true;
        this.secondStationNumber = secondStationNumber;
        this.secondTicketType = secondTicketType;
    }

    public int getStationNumber() {
        return stationNumber;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public boolean isDoubleMove() {
        return isDoubleMove;
    }

    public Integer getSecondStationNumber() {
        return secondStationNumber;
    }

    public TicketType getSecondTicketType() {
        return secondTicketType;
    }
}

