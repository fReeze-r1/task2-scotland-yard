package ru.cs.vsu.oop.task2.lopatin_n.components;

import ru.cs.vsu.oop.task2.lopatin_n.core.Station;
import ru.cs.vsu.oop.task2.lopatin_n.core.TicketType;

import java.util.ArrayList;
import java.util.List;

public class MrX extends Player {
    private final List<Integer> moveHistory;
    private final List<TicketType> usedTickets;
    private int moveCount;
    private int doubleMoveCount;

    public MrX(Station startStation, int detectiveCount) {
        super("Mr. X", startStation);
        this.moveHistory = new ArrayList<>();
        this.usedTickets = new ArrayList<>();
        this.moveCount = 0;
        this.doubleMoveCount = 0;

        addTicket(TicketType.TAXI, 4);
        addTicket(TicketType.BUS, 3);
        addTicket(TicketType.UNDERGROUND, 3);
        addTicket(TicketType.DOUBLE_MOVE, 2);
        addTicket(TicketType.BLACK, detectiveCount);
    }

    public void recordMove(int stationNumber, TicketType ticketType) {
        moveHistory.add(stationNumber);
        usedTickets.add(ticketType);
        moveCount++;
    }

    public List<Integer> getMoveHistory() {
        return new ArrayList<>(moveHistory);
    }

    public List<TicketType> getUsedTickets() {
        return new ArrayList<>(usedTickets);
    }

    public int getMoveCount() {
        return moveCount;
    }

    public boolean shouldReveal() {
        return moveCount == 3 || moveCount == 8 || moveCount == 13 || moveCount == 18;
    }

    public boolean useDoubleMove() {
        if (doubleMoveCount < 2 && hasTicket(TicketType.DOUBLE_MOVE)) {
            useTicket(TicketType.DOUBLE_MOVE);
            doubleMoveCount++;
            return true;
        }
        return false;
    }

    public boolean canUseDoubleMove() {
        return doubleMoveCount < 2 && hasTicket(TicketType.DOUBLE_MOVE);
    }

    @Override
    public boolean canMove(Station target, TicketType transport) {
        if (transport == TicketType.BLACK) {
            return hasTicket(TicketType.BLACK) || 
                   (currentStation.getAvailableTransport().contains(transport) && 
                    currentStation.canReach(target, transport));
        }
        return (hasTicket(transport) || hasTicket(TicketType.BLACK)) &&
               currentStation.getAvailableTransport().contains(transport) &&
               currentStation.canReach(target, transport);
    }
}

