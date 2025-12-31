package ru.cs.vsu.oop.task2.lopatin_n.ui;

import ru.cs.vsu.oop.task2.lopatin_n.components.Detective;
import ru.cs.vsu.oop.task2.lopatin_n.components.Game;
import ru.cs.vsu.oop.task2.lopatin_n.core.TicketType;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class PlayersPanel extends JPanel {
    private Game game;
    private MainWindow mainWindow;
    private JPanel playersList;
    
    public PlayersPanel(Game game) {
        this.game = game;
        this.mainWindow = null;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
            "👥 Игроки",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font(Font.SANS_SERIF, Font.BOLD, 14),
            new Color(50, 100, 150)
        ));
        setPreferredSize(new Dimension(280, 350));
        setBackground(new Color(250, 250, 255));
        
        playersList = new JPanel();
        playersList.setLayout(new BoxLayout(playersList, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(playersList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        add(scrollPane, BorderLayout.CENTER);
        
        updatePlayers();
    }
    
    public void updatePlayers() {
        playersList.removeAll();
        
        if (game.getMrX() != null && game.getMrX().getCurrentStation() != null) {
            // Мистер Икс всегда видит свою позицию
            // Детективы видят позицию только когда Мистер Икс раскрыт
            boolean isMrXTurn = mainWindow != null && mainWindow.isMrXTurnForUI();
            boolean shouldShowPosition = isMrXTurn || 
                                        game.getMrX().shouldReveal() || 
                                        game.getMrX().getMoveCount() < 3;
            
            int stationNumber = shouldShowPosition ? 
                game.getMrX().getCurrentStation().getNumber() : -1;
            
            JPanel mrXPanel = createPlayerPanel("Мистер Икс", 
                stationNumber,
                game.getMrX().getTickets(),
                Color.RED);
            playersList.add(mrXPanel);
        }
        
        Color[] colors = {Color.BLUE, Color.GREEN, Color.MAGENTA, Color.CYAN, Color.ORANGE};
        int index = 0;
        
        for (Detective detective : game.getDetectives()) {
            if (detective.getCurrentStation() != null) {
                JPanel detPanel = createPlayerPanel(detective.getName(),
                    detective.getCurrentStation().getNumber(),
                    detective.getTickets(),
                    colors[index % colors.length]);
                playersList.add(detPanel);
                index++;
            }
        }
        
        playersList.revalidate();
        playersList.repaint();
    }
    
    private JPanel createPlayerPanel(String name, int station, Map<TicketType, Integer> tickets, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.setPreferredSize(new Dimension(260, 110));
        panel.setMaximumSize(new Dimension(260, 110));
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        nameLabel.setForeground(color);
        
        JLabel stationLabel;
        if (station == -1) {
            stationLabel = new JLabel("📍 Позиция: скрыта");
            stationLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 11));
            stationLabel.setForeground(new Color(150, 150, 150));
        } else {
            stationLabel = new JLabel("📍 Станция: " + station);
            stationLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
            stationLabel.setForeground(new Color(60, 60, 60));
        }
        
        JLabel ticketsLabel = new JLabel("🎫 Билеты:");
        ticketsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        ticketsLabel.setForeground(new Color(80, 80, 80));
        
        JPanel ticketsPanel = new JPanel();
        ticketsPanel.setLayout(new BoxLayout(ticketsPanel, BoxLayout.Y_AXIS));
        
        for (Map.Entry<TicketType, Integer> entry : tickets.entrySet()) {
                if (entry.getKey() != TicketType.DOUBLE_MOVE && entry.getValue() > 0) {
                    JLabel ticket = new JLabel("  • " + getTicketName(entry.getKey()) + ": " + entry.getValue());
                    ticket.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
                    ticket.setForeground(new Color(50, 50, 50));
                    ticketsPanel.add(ticket);
                }
        }
        
        panel.add(nameLabel);
        panel.add(stationLabel);
        panel.add(ticketsLabel);
        panel.add(ticketsPanel);
        
        return panel;
    }
    
    private String getTicketName(TicketType type) {
        switch (type) {
            case TAXI: return "Такси";
            case BUS: return "Автобус";
            case UNDERGROUND: return "Метро";
            case BLACK: return "Черный";
            default: return type.name();
        }
    }
    
    public void updateGame(Game newGame) {
        this.game = newGame;
        updatePlayers();
    }
    
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }
}

