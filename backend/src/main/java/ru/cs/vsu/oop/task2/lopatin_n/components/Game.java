package ru.cs.vsu.oop.task2.lopatin_n.components;

import ru.cs.vsu.oop.task2.lopatin_n.core.GameBoard;
import ru.cs.vsu.oop.task2.lopatin_n.core.Station;
import ru.cs.vsu.oop.task2.lopatin_n.core.Ticket;
import ru.cs.vsu.oop.task2.lopatin_n.core.TicketType;

import java.util.*;

public class Game {
    private final GameBoard board;
    private MrX mrX;
    private final List<Detective> detectives;
    private final List<Integer> startTiles;
    private GameState state;
    private int currentDetectiveIndex;

    public enum GameState {
        SETUP,
        IN_PROGRESS,
        MR_X_WON,
        DETECTIVES_WON
    }

    public Game() {
        this.board = new GameBoard();
        this.detectives = new ArrayList<>();
        this.startTiles = new ArrayList<>();
        this.state = GameState.SETUP;
        this.currentDetectiveIndex = 0;
        initializeStartTiles();
    }

    private void initializeStartTiles() {
        for (int i = 1; i <= 18; i++) {
            startTiles.add(i);
        }
        Collections.shuffle(startTiles);
    }

    public void addDetective(String name) {
        if (state != GameState.SETUP) {
            throw new IllegalStateException("Cannot add detectives after game started");
        }
        detectives.add(new Detective(name, null));
    }

    public void startGame() {
        if (state != GameState.SETUP) {
            throw new IllegalStateException("Game already started");
        }
        if (detectives.isEmpty()) {
            throw new IllegalStateException("Need at least one detective");
        }

        List<Integer> shuffledTiles = new ArrayList<>(startTiles);
        Collections.shuffle(shuffledTiles);

        Station mrXStart = board.getStation(shuffledTiles.get(0));
        mrX = new MrX(mrXStart, detectives.size());

        for (int i = 0; i < detectives.size(); i++) {
            Station detectiveStart = board.getStation(shuffledTiles.get(i + 1));
            detectives.get(i).setCurrentStation(detectiveStart);
        }

        state = GameState.IN_PROGRESS;
    }

    public boolean makeMrXMove(Move move) {
        if (state != GameState.IN_PROGRESS) {
            return false;
        }

        if (move.isDoubleMove()) {
            return makeMrXDoubleMove(move);
        } else {
            return makeMrXSingleMove(move);
        }
    }

    private boolean makeMrXSingleMove(Move move) {
        Station target = board.getStation(move.getStationNumber());
        if (target == null) {
            return false;
        }

        TicketType transport = move.getTicketType();
        if (!mrX.canMove(target, transport)) {
            return false;
        }

        if (transport == TicketType.BLACK) {
            if (!mrX.useTicket(TicketType.BLACK)) {
                return false;
            }
        } else {
            if (!mrX.useTicket(transport)) {
                if (!mrX.useTicket(TicketType.BLACK)) {
                    return false;
                }
            }
        }

        mrX.setCurrentStation(target);
        mrX.recordMove(move.getStationNumber(), transport);

        if (checkDetectivesCaughtMrX()) {
            state = GameState.DETECTIVES_WON;
            return true;
        }

        if (mrX.getMoveCount() >= 22) {
            state = GameState.MR_X_WON;
            return true;
        }

        return true;
    }

    private boolean makeMrXDoubleMove(Move move) {
        if (!mrX.canUseDoubleMove()) {
            return false;
        }

        Station firstTarget = board.getStation(move.getStationNumber());
        Station secondTarget = board.getStation(move.getSecondStationNumber());

        if (firstTarget == null || secondTarget == null) {
            return false;
        }

        TicketType firstTransport = move.getTicketType();
        TicketType secondTransport = move.getSecondTicketType();

        if (!mrX.canMove(firstTarget, firstTransport)) {
            return false;
        }

        mrX.setCurrentStation(firstTarget);
        if (firstTransport == TicketType.BLACK) {
            mrX.useTicket(TicketType.BLACK);
        } else {
            if (!mrX.useTicket(firstTransport)) {
                mrX.useTicket(TicketType.BLACK);
            }
        }

        if (checkDetectivesCaughtMrX()) {
            state = GameState.DETECTIVES_WON;
            mrX.recordMove(move.getStationNumber(), firstTransport);
            mrX.useDoubleMove();
            return true;
        }

        if (!firstTarget.canReach(secondTarget, secondTransport)) {
            mrX.setCurrentStation(mrX.getCurrentStation());
            return false;
        }

        if (secondTransport == TicketType.BLACK) {
            if (!mrX.useTicket(TicketType.BLACK)) {
                return false;
            }
        } else {
            if (!mrX.useTicket(secondTransport)) {
                if (!mrX.useTicket(TicketType.BLACK)) {
                    return false;
                }
            }
        }

        mrX.setCurrentStation(secondTarget);
        mrX.recordMove(move.getStationNumber(), firstTransport);
        mrX.recordMove(move.getSecondStationNumber(), secondTransport);
        mrX.useDoubleMove();

        if (checkDetectivesCaughtMrX()) {
            state = GameState.DETECTIVES_WON;
            return true;
        }

        if (mrX.getMoveCount() >= 22) {
            state = GameState.MR_X_WON;
            return true;
        }

        return true;
    }

    public boolean makeDetectiveMove(Detective detective, Move move) {
        if (state != GameState.IN_PROGRESS) {
            return false;
        }

        Station target = board.getStation(move.getStationNumber());
        if (target == null) {
            return false;
        }

        TicketType transport = move.getTicketType();
        if (!detective.canMove(target, transport)) {
            return false;
        }

        if (isStationOccupied(target, detective)) {
            return false;
        }

        if (!detective.useTicket(transport)) {
            return false;
        }

        detective.setCurrentStation(target);
        mrX.addTicket(new Ticket(transport));

        if (checkDetectivesCaughtMrX()) {
            state = GameState.DETECTIVES_WON;
            return true;
        }

        return true;
    }

    private boolean isStationOccupied(Station station, Detective currentDetective) {
        for (Detective detective : detectives) {
            if (detective != currentDetective && 
                detective.getCurrentStation() != null &&
                detective.getCurrentStation().equals(station)) {
                return true;
            }
        }
        return false;
    }

    public void nextDetective() {
        currentDetectiveIndex = (currentDetectiveIndex + 1) % detectives.size();
    }

    public Detective getCurrentDetective() {
        if (detectives.isEmpty()) {
            return null;
        }
        return detectives.get(currentDetectiveIndex);
    }

    private boolean checkDetectivesCaughtMrX() {
        if (mrX == null || mrX.getCurrentStation() == null) {
            return false;
        }

        for (Detective detective : detectives) {
            if (detective.getCurrentStation() != null &&
                detective.getCurrentStation().equals(mrX.getCurrentStation())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkDetectivesCanMove() {
        for (Detective detective : detectives) {
            Station current = detective.getCurrentStation();
            if (current == null) {
                continue;
            }

            for (TicketType transport : Arrays.asList(TicketType.TAXI, TicketType.BUS, TicketType.UNDERGROUND)) {
                if (detective.hasTicket(transport)) {
                    Set<Station> connected = current.getConnectedStations(transport);
                    for (Station target : connected) {
                        if (!isStationOccupied(target, detective)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void checkGameEnd() {
        if (state != GameState.IN_PROGRESS) {
            return;
        }

        if (checkDetectivesCaughtMrX()) {
            state = GameState.DETECTIVES_WON;
            return;
        }

        if (mrX.getMoveCount() >= 22) {
            state = GameState.MR_X_WON;
            return;
        }

        if (!checkDetectivesCanMove()) {
            state = GameState.MR_X_WON;
        }
    }

    public GameBoard getBoard() {
        return board;
    }

    public MrX getMrX() {
        return mrX;
    }

    public List<Detective> getDetectives() {
        return Collections.unmodifiableList(detectives);
    }

    public GameState getState() {
        return state;
    }
}

