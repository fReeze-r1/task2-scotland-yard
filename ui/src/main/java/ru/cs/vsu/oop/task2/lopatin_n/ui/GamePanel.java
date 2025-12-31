package ru.cs.vsu.oop.task2.lopatin_n.ui;

import ru.cs.vsu.oop.task2.lopatin_n.components.Detective;
import ru.cs.vsu.oop.task2.lopatin_n.components.Game;
import ru.cs.vsu.oop.task2.lopatin_n.core.Station;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends JPanel {
    private Game game;
    private MainWindow mainWindow;
    private Map<Integer, Point> stationPositions;
    private static final int MAP_WIDTH = 800;
    private static final int MAP_HEIGHT = 600;
    
    private static class Point {
        int x, y;
        Point(int x, int y) { this.x = x; this.y = y; }
    }
    
    public GamePanel(Game game) {
        this.game = game;
        this.mainWindow = null;
        this.stationPositions = new HashMap<>();
        initializeStationPositions();
        setPreferredSize(new Dimension(MAP_WIDTH, MAP_HEIGHT));
        setBackground(new Color(245, 250, 255));
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
            "🗺️ Карта Лондона",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font(Font.SANS_SERIF, Font.BOLD, 14),
            new Color(50, 100, 150)
        ));
    }
    
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }
    
    private boolean shouldShowMrX() {
        if (game.getMrX() == null || game.getMrX().getCurrentStation() == null) {
            return false;
        }
        
        int moveCount = game.getMrX().getMoveCount();
        
        // Первые 3 хода - виден всем (начало игры)
        if (moveCount < 3) {
            return true;
        }
        
        // Определяем, чей сейчас ход
        boolean isMrXTurn = false;
        if (mainWindow != null) {
            isMrXTurn = mainWindow.isMrXTurnForUI();
        } else {
            // Fallback: если mainWindow не установлен, считаем что ход Мистера Икс только в начале
            isMrXTurn = moveCount == 0;
        }
        
        // Мистер Икс всегда видит свою позицию
        if (isMrXTurn) {
            return true;
        }
        
        // Если это НЕ ход Мистера Икс, значит ход детектива
        // Детективы НЕ должны видеть Мистера Икс на карте (после первых 3 ходов)
        // НЕ показываем его на карте
        return false;
    }
    
    private void initializeStationPositions() {
        int cols = 14;
        int rows = 15;
        int cellWidth = MAP_WIDTH / cols;
        int cellHeight = MAP_HEIGHT / rows;
        int padding = 30;
        
        for (int i = 1; i <= 200; i++) {
            int row = (i - 1) / cols;
            int col = (i - 1) % cols;
            int x = padding + col * cellWidth + (i % 3) * 5;
            int y = padding + row * cellHeight + (i % 5) * 3;
            
            x = Math.max(15, Math.min(MAP_WIDTH - 15, x));
            y = Math.max(15, Math.min(MAP_HEIGHT - 15, y));
            
            stationPositions.put(i, new Point(x, y));
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawStations(g2d);
        drawPlayers(g2d);
    }
    
    private void drawStations(Graphics2D g) {
        g.setStroke(new BasicStroke(1.2f));
        
        for (Map.Entry<Integer, Point> entry : stationPositions.entrySet()) {
            Point pos = entry.getValue();
            int stationNum = entry.getKey();
            
            g.setColor(new Color(180, 200, 220));
            g.fillOval(pos.x - 6, pos.y - 6, 12, 12);
            g.setColor(new Color(100, 130, 160));
            g.setStroke(new BasicStroke(1.5f));
            g.drawOval(pos.x - 6, pos.y - 6, 12, 12);
            
            g.setColor(new Color(40, 60, 80));
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 9));
            String label = String.valueOf(stationNum);
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(label);
            g.drawString(label, pos.x - textWidth / 2, pos.y - 8);
        }
    }
    
    private void drawPlayers(Graphics2D g) {
        // Рисуем Мистера Икс только если он должен быть виден
        if (game.getMrX() != null && game.getMrX().getCurrentStation() != null) {
            boolean showMrX = shouldShowMrX();
            
            // ВАЖНО: Рисуем Мистера Икс ТОЛЬКО если showMrX == true
            if (showMrX) {
                Point pos = stationPositions.get(game.getMrX().getCurrentStation().getNumber());
                if (pos != null) {
                    // Рисуем тень
                    g.setColor(new Color(0, 0, 0, 50));
                    g.fillOval(pos.x - 14, pos.y - 11, 30, 30);
                    
                    // Рисуем Мистера Икс
                    g.setColor(new Color(150, 0, 0));
                    g.fillOval(pos.x - 15, pos.y - 15, 30, 30);
                    g.setColor(new Color(200, 0, 0));
                    g.setStroke(new BasicStroke(3f));
                    g.drawOval(pos.x - 15, pos.y - 15, 30, 30);
                    g.setColor(Color.WHITE);
                    g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
                    FontMetrics fm = g.getFontMetrics();
                    int textWidth = fm.stringWidth("X");
                    g.drawString("X", pos.x - textWidth / 2, pos.y + 7);
                }
            }
            // Если showMrX == false, НЕ рисуем Мистера Икс вообще
        }
        
        Color[] detectiveColors = {
            new Color(50, 100, 200),    // Синий
            new Color(50, 150, 50),     // Зеленый
            new Color(200, 50, 150),     // Розовый
            new Color(50, 200, 200),    // Голубой
            new Color(255, 140, 0)       // Оранжевый
        };
        String[] detectiveLabels = {"H", "W", "M", "L", "J"};
        int colorIndex = 0;
        
        for (Detective detective : game.getDetectives()) {
            if (detective.getCurrentStation() != null) {
                Point pos = stationPositions.get(detective.getCurrentStation().getNumber());
                if (pos != null) {
                    Color detColor = detectiveColors[colorIndex % detectiveColors.length];
                    
                    // Рисуем тень
                    g.setColor(new Color(0, 0, 0, 50));
                    g.fillRect(pos.x - 12, pos.y - 9, 26, 26);
                    
                    // Рисуем детектива
                    g.setColor(detColor);
                    g.fillRect(pos.x - 13, pos.y - 13, 26, 26);
                    g.setColor(new Color(detColor.getRed() + 30, detColor.getGreen() + 30, detColor.getBlue() + 30));
                    g.setStroke(new BasicStroke(3f));
                    g.drawRect(pos.x - 13, pos.y - 13, 26, 26);
                    g.setColor(Color.WHITE);
                    g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
                    FontMetrics fm = g.getFontMetrics();
                    String label = detectiveLabels[colorIndex % detectiveLabels.length];
                    int textWidth = fm.stringWidth(label);
                    g.drawString(label, pos.x - textWidth / 2, pos.y + 7);
                    
                    colorIndex++;
                }
            }
        }
    }
    
    public void updateGame(Game newGame) {
        this.game = newGame;
        repaint();
    }
}

