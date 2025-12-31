package ru.cs.vsu.oop.task2.lopatin_n.ui;

import ru.cs.vsu.oop.task2.lopatin_n.components.Detective;
import ru.cs.vsu.oop.task2.lopatin_n.components.Game;

import javax.swing.*;
import java.awt.*;

public class GameInfoPanel extends JPanel {
    private Game game;
    private MainWindow mainWindow;
    private JLabel currentPlayerLabel;
    private JLabel moveCountLabel;
    private JLabel statusLabel;
    
    public GameInfoPanel(Game game) {
        this.game = game;
        this.mainWindow = null;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
            "📊 Информация об игре",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font(Font.SANS_SERIF, Font.BOLD, 14),
            new Color(50, 100, 150)
        ));
        setPreferredSize(new Dimension(280, 140));
        setBackground(new Color(250, 250, 255));
        
        currentPlayerLabel = new JLabel();
        currentPlayerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        currentPlayerLabel.setForeground(new Color(30, 80, 150));
        
        moveCountLabel = new JLabel();
        moveCountLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        moveCountLabel.setForeground(new Color(60, 60, 60));
        
        statusLabel = new JLabel();
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        
        add(Box.createVerticalStrut(5));
        add(currentPlayerLabel);
        add(Box.createVerticalStrut(3));
        add(moveCountLabel);
        add(Box.createVerticalStrut(3));
        add(statusLabel);
        add(Box.createVerticalStrut(5));
        
        updateStatus();
    }
    
    public void updateStatus() {
        if (game.getState() == Game.GameState.IN_PROGRESS) {
            boolean isMrXTurn = false;
            if (mainWindow != null) {
                isMrXTurn = mainWindow.isMrXTurnForUI();
            } else {
                isMrXTurn = game.getMrX().getMoveCount() == 0 || 
                           (game.getCurrentDetective() != null && 
                            game.getDetectives().indexOf(game.getCurrentDetective()) == 0);
            }
            
            if (isMrXTurn) {
                currentPlayerLabel.setText("🎯 Ход: Мистер Икс");
                currentPlayerLabel.setForeground(new Color(150, 0, 0));
            } else {
                Detective currentDetective = game.getCurrentDetective();
                if (currentDetective != null) {
                    currentPlayerLabel.setText("👮 Ход: " + currentDetective.getName());
                    currentPlayerLabel.setForeground(new Color(30, 80, 150));
                } else {
                    currentPlayerLabel.setText("🎯 Ход: Мистер Икс");
                    currentPlayerLabel.setForeground(new Color(150, 0, 0));
                }
            }
            
            if (game.getMrX() != null) {
                moveCountLabel.setText("📊 Ход номер: " + (game.getMrX().getMoveCount() + 1) + " / 22");
                boolean mrXTurn = mainWindow != null && mainWindow.isMrXTurnForUI();
                
                if (game.getMrX().shouldReveal()) {
                    statusLabel.setText("🔍 Мистер Икс показывает местоположение!");
                    statusLabel.setForeground(new Color(200, 0, 0));
                } else if (mrXTurn) {
                    statusLabel.setText("👻 Мистер Икс скрыт (вы видите свою позицию)");
                    statusLabel.setForeground(new Color(100, 100, 100));
                } else {
                    statusLabel.setText("👻 Мистер Икс скрыт (позиция неизвестна)");
                    statusLabel.setForeground(new Color(100, 100, 100));
                }
            }
        } else {
            currentPlayerLabel.setText("🏁 Игра окончена");
            moveCountLabel.setText("");
            if (game.getState() == Game.GameState.MR_X_WON) {
                statusLabel.setText("🎉 Мистер Икс выиграл!");
                statusLabel.setForeground(new Color(200, 0, 0));
            } else {
                statusLabel.setText("🎉 Детективы выиграли!");
                statusLabel.setForeground(new Color(0, 100, 200));
            }
        }
    }
    
    public void updateGame(Game newGame) {
        this.game = newGame;
        updateStatus();
    }
    
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }
}

