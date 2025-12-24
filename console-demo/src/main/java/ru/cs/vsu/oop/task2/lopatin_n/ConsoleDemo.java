package ru.cs.vsu.oop.task2.lopatin_n;

import ru.cs.vsu.oop.task2.lopatin_n.components.*;
import ru.cs.vsu.oop.task2.lopatin_n.core.Station;
import ru.cs.vsu.oop.task2.lopatin_n.core.TicketType;

import java.util.*;

public class ConsoleDemo {
    private static final Scanner scanner = new Scanner(System.in);
    private static Game game;

    public static void main(String[] args) {
        System.out.println("=== Scotland Yard ===");
        System.out.println();

        game = new Game();
        setupGame();
        game.startGame();

        System.out.println("Игра началась!");
        System.out.println("Мистер Икс начинает на станции " + 
            game.getMrX().getCurrentStation().getNumber());
        System.out.println();

        while (game.getState() == Game.GameState.IN_PROGRESS) {
            playRound();
            game.checkGameEnd();
        }

        printGameResult();
    }

    private static void setupGame() {
        System.out.print("Введите количество детективов (1-5): ");
        int detectiveCount = Integer.parseInt(scanner.nextLine());

        for (int i = 1; i <= detectiveCount; i++) {
            System.out.print("Введите имя детектива " + i + ": ");
            String name = scanner.nextLine();
            if (name.trim().isEmpty()) {
                name = "Детектив " + i;
            }
            game.addDetective(name);
        }
    }

    private static void playRound() {
        System.out.println("--- Ход " + (game.getMrX().getMoveCount() + 1) + " ---");
        
        if (game.getMrX().shouldReveal()) {
            System.out.println("Мистер Икс показывает свое местоположение!");
            System.out.println("Станция: " + game.getMrX().getCurrentStation().getNumber());
        }

        makeMrXMove();
        
        for (Detective detective : game.getDetectives()) {
            makeDetectiveMove(detective);
        }

        System.out.println();
    }

    private static void makeMrXMove() {
        System.out.println("Ход Мистера Икс");
        System.out.println("Текущая станция: " + game.getMrX().getCurrentStation().getNumber());
        printAvailableTickets(game.getMrX());

        System.out.print("Использовать двойной ход? (y/n): ");
        String useDouble = scanner.nextLine().toLowerCase();

        if (useDouble.equals("y") && game.getMrX().canUseDoubleMove()) {
            makeMrXDoubleMove();
        } else {
            makeMrXSingleMove();
        }
    }

    private static void makeMrXSingleMove() {
        Station current = game.getMrX().getCurrentStation();
        List<Station> availableStations = getAvailableStations(current, game.getMrX());

        if (availableStations.isEmpty()) {
            System.out.println("Нет доступных ходов!");
            return;
        }

        System.out.println("Доступные станции:");
        for (int i = 0; i < availableStations.size(); i++) {
            Station station = availableStations.get(i);
            System.out.println((i + 1) + ". Станция " + station.getNumber());
        }

        System.out.print("Выберите станцию (1-" + availableStations.size() + "): ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;

        if (choice < 0 || choice >= availableStations.size()) {
            System.out.println("Неверный выбор!");
            return;
        }

        Station target = availableStations.get(choice);
        TicketType transport = findTransportType(current, target);

        System.out.print("Использовать черный билет? (y/n): ");
        String useBlack = scanner.nextLine().toLowerCase();
        if (useBlack.equals("y") && game.getMrX().hasTicket(TicketType.BLACK)) {
            transport = TicketType.BLACK;
        }

        Move move = new Move(target.getNumber(), transport);
        if (game.makeMrXMove(move)) {
            System.out.println("Мистер Икс переместился на станцию " + target.getNumber());
        } else {
            System.out.println("Неверный ход!");
        }
    }

    private static void makeMrXDoubleMove() {
        Station current = game.getMrX().getCurrentStation();
        List<Station> availableStations1 = getAvailableStations(current, game.getMrX());

        System.out.println("Первый ход - доступные станции:");
        for (int i = 0; i < availableStations1.size(); i++) {
            System.out.println((i + 1) + ". Станция " + availableStations1.get(i).getNumber());
        }

        System.out.print("Выберите первую станцию: ");
        int choice1 = Integer.parseInt(scanner.nextLine()) - 1;
        Station target1 = availableStations1.get(choice1);
        TicketType transport1 = findTransportType(current, target1);

        Station current2 = target1;
        List<Station> availableStations2 = getAvailableStations(current2, game.getMrX());

        System.out.println("Второй ход - доступные станции:");
        for (int i = 0; i < availableStations2.size(); i++) {
            System.out.println((i + 1) + ". Станция " + availableStations2.get(i).getNumber());
        }

        System.out.print("Выберите вторую станцию: ");
        int choice2 = Integer.parseInt(scanner.nextLine()) - 1;
        Station target2 = availableStations2.get(choice2);
        TicketType transport2 = findTransportType(current2, target2);

        Move move = new Move(target1.getNumber(), transport1, target2.getNumber(), transport2);
        if (game.makeMrXMove(move)) {
            System.out.println("Мистер Икс сделал двойной ход: " + 
                target1.getNumber() + " -> " + target2.getNumber());
        } else {
            System.out.println("Неверный ход!");
        }
    }

    private static void makeDetectiveMove(Detective detective) {
        System.out.println("Ход " + detective.getName());
        System.out.println("Текущая станция: " + detective.getCurrentStation().getNumber());
        printAvailableTickets(detective);

        Station current = detective.getCurrentStation();
        List<Station> availableStations = getAvailableStations(current, detective);

        if (availableStations.isEmpty()) {
            System.out.println("Нет доступных ходов!");
            return;
        }

        System.out.println("Доступные станции:");
        for (int i = 0; i < availableStations.size(); i++) {
            Station station = availableStations.get(i);
            System.out.println((i + 1) + ". Станция " + station.getNumber());
        }

        System.out.print("Выберите станцию (1-" + availableStations.size() + "): ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;

        if (choice < 0 || choice >= availableStations.size()) {
            System.out.println("Неверный выбор!");
            return;
        }

        Station target = availableStations.get(choice);
        TicketType transport = findTransportType(current, target);

        Move move = new Move(target.getNumber(), transport);
        if (game.makeDetectiveMove(detective, move)) {
            System.out.println(detective.getName() + " переместился на станцию " + target.getNumber());
        } else {
            System.out.println("Неверный ход!");
        }
    }

    private static List<Station> getAvailableStations(Station current, Player player) {
        List<Station> available = new ArrayList<>();
        Set<TicketType> transports = current.getAvailableTransport();

        for (TicketType transport : transports) {
            if (transport == TicketType.BLACK && player instanceof Detective) {
                continue;
            }
            if (transport == TicketType.DOUBLE_MOVE && player instanceof Detective) {
                continue;
            }

            Set<Station> connected = current.getConnectedStations(transport);
            for (Station station : connected) {
                if (player.canMove(station, transport) && !available.contains(station)) {
                    if (player instanceof Detective) {
                        boolean occupied = false;
                        for (Detective d : game.getDetectives()) {
                            if (d != player && d.getCurrentStation() != null &&
                                d.getCurrentStation().equals(station)) {
                                occupied = true;
                                break;
                            }
                        }
                        if (!occupied) {
                            available.add(station);
                        }
                    } else {
                        available.add(station);
                    }
                }
            }
        }

        return available;
    }

    private static TicketType findTransportType(Station from, Station to) {
        for (TicketType transport : from.getAvailableTransport()) {
            if (from.canReach(to, transport)) {
                return transport;
            }
        }
        return TicketType.TAXI;
    }

    private static void printAvailableTickets(Player player) {
        System.out.print("Доступные билеты: ");
        Map<TicketType, Integer> tickets = player.getTickets();
        List<String> ticketList = new ArrayList<>();
        for (Map.Entry<TicketType, Integer> entry : tickets.entrySet()) {
            if (entry.getValue() > 0) {
                ticketList.add(entry.getKey().name() + "(" + entry.getValue() + ")");
            }
        }
        System.out.println(String.join(", ", ticketList));
    }

    private static void printGameResult() {
        System.out.println();
        System.out.println("=== Игра окончена ===");
        if (game.getState() == Game.GameState.MR_X_WON) {
            System.out.println("Мистер Икс выиграл!");
        } else if (game.getState() == Game.GameState.DETECTIVES_WON) {
            System.out.println("Детективы выиграли!");
        }
    }
}

