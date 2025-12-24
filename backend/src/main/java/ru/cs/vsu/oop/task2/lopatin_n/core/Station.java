package ru.cs.vsu.oop.task2.lopatin_n.core;

import java.util.*;

public class Station {
    private final int number;
    private final Set<TicketType> availableTransport;
    private final Map<TicketType, Set<Station>> connections;

    public Station(int number) {
        this.number = number;
        this.availableTransport = new HashSet<>();
        this.connections = new HashMap<>();
    }

    public int getNumber() {
        return number;
    }

    public void addConnection(TicketType transport, Station station) {
        availableTransport.add(transport);
        connections.computeIfAbsent(transport, k -> new HashSet<>()).add(station);
    }

    public Set<Station> getConnectedStations(TicketType transport) {
        return connections.getOrDefault(transport, Collections.emptySet());
    }

    public Set<TicketType> getAvailableTransport() {
        return Collections.unmodifiableSet(availableTransport);
    }

    public boolean canReach(Station target, TicketType transport) {
        return connections.getOrDefault(transport, Collections.emptySet()).contains(target);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Station station = (Station) obj;
        return number == station.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return "Station{" + number + "}";
    }
}

