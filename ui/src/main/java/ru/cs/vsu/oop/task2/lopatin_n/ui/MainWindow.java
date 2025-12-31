package ru.cs.vsu.oop.task2.lopatin_n.ui;

import ru.cs.vsu.oop.task2.lopatin_n.components.Detective;
import ru.cs.vsu.oop.task2.lopatin_n.components.Game;
import ru.cs.vsu.oop.task2.lopatin_n.components.Move;
import ru.cs.vsu.oop.task2.lopatin_n.core.TicketType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame {
    private Game game;
    private GameInfoPanel gameInfoPanel;
    private PlayersPanel playersPanel;
    private MovePanel movePanel;
    private GamePanel gamePanel;
    private RulesPanel rulesPanel;
    private boolean mrXJustMoved;
    
    public MainWindow() {
        setTitle("🎯 Scotland Yard - Охота на Мистера Икс");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setBackground(new Color(240, 245, 250));

        initializeGame();
        setupUI();
        updateUI();
    }
    
    private void initializeGame() {
        game = new Game();
        String numDetectivesStr = JOptionPane.showInputDialog(this, "Введите количество детективов (1-5):", "Настройка игры", JOptionPane.QUESTION_MESSAGE);
        int numDetectives = 3;
        try {
            numDetectives = Integer.parseInt(numDetectivesStr);
            if (numDetectives < 1 || numDetectives > 5) {
                numDetectives = 3;
            }
        } catch (Exception e) {
            numDetectives = 3;
        }

        for (int i = 0; i < numDetectives; i++) {
            String name = JOptionPane.showInputDialog(this, "Введите имя детектива " + (i + 1) + ":", "Настройка игры", JOptionPane.QUESTION_MESSAGE);
            if (name == null || name.trim().isEmpty()) {
                name = "Detective " + (i + 1);
            }
            game.addDetective(name);
        }
        game.startGame();
        mrXJustMoved = false;
    }
    
    private void setupUI() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(300, getHeight()));
        leftPanel.setBackground(new Color(245, 250, 255));

        gameInfoPanel = new GameInfoPanel(game);
        gameInfoPanel.setMainWindow(this);
        playersPanel = new PlayersPanel(game);
        playersPanel.setMainWindow(this);
        rulesPanel = new RulesPanel();
        movePanel = new MovePanel(game, this);
        gamePanel = new GamePanel(game);
        gamePanel.setMainWindow(this);

        leftPanel.add(gameInfoPanel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(playersPanel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(rulesPanel);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(250, 250, 255));
        centerPanel.add(gamePanel, BorderLayout.CENTER);
        centerPanel.add(movePanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    public void handleMove(int stationNumber, TicketType transportType, boolean isDoubleMove, int secondStationNumber, TicketType secondTransportType) {
        if (game.getState() != Game.GameState.IN_PROGRESS) {
            return;
        }

        boolean moveSuccessful = false;
        boolean isMrXTurn = isMrXTurn();
        
        if (isMrXTurn) {
            if (isDoubleMove) {
                Move move = new Move(stationNumber, transportType, secondStationNumber, secondTransportType);
                moveSuccessful = game.makeMrXMove(move);
            } else {
                Move move = new Move(stationNumber, transportType);
                moveSuccessful = game.makeMrXMove(move);
            }
            if (moveSuccessful) {
                mrXJustMoved = true;
            }
        } else {
            Detective currentDetective = game.getCurrentDetective();
            if (currentDetective != null) {
                Move move = new Move(stationNumber, transportType);
                moveSuccessful = game.makeDetectiveMove(currentDetective, move);
                if (moveSuccessful) {
                    game.nextDetective();
                    Detective nextDetective = game.getCurrentDetective();
                    if (nextDetective == null || game.getDetectives().indexOf(nextDetective) == 0) {
                        mrXJustMoved = false;
                    }
                }
            }
        }

        if (moveSuccessful) {
            updateUI();
            checkGameEnd();
        } else {
            JOptionPane.showMessageDialog(this, "Невозможный ход! Проверьте наличие билетов и доступность станции.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean isMrXTurn() {
        // Мистер Икс ходит:
        // 1. В начале игры (moveCount == 0) - первый ход
        // 2. Когда все детективы походили (currentDetectiveIndex вернулся к 0 после последнего детектива)
        if (game.getMrX().getMoveCount() == 0) {
            return true;
        }
        
        // Если только что Мистер Икс походил, то теперь ходят детективы
        if (mrXJustMoved) {
            return false;
        }
        
        Detective currentDetective = game.getCurrentDetective();
        if (currentDetective == null) {
            return true;
        }
        
        // Если currentDetective - это первый детектив (index 0) И Мистер Икс еще не ходил в этом раунде,
        // значит все детективы походили - снова ход Мистера Икс
        int currentIndex = game.getDetectives().indexOf(currentDetective);
        return currentIndex == 0;
    }
    
    public boolean isMrXTurnForUI() {
        return isMrXTurn();
    }
    
    private void updateUI() {
        gameInfoPanel.updateStatus();
        playersPanel.updatePlayers();
        movePanel.updateMoveOptions();
        gamePanel.updateGame(game);
        repaint();
    }
    
    private void checkGameEnd() {
        game.checkGameEnd();
        if (game.getState() != Game.GameState.IN_PROGRESS) {
            String message;
            if (game.getState() == Game.GameState.MR_X_WON) {
                message = "Мистер Икс выиграл!";
            } else {
                message = "Детективы выиграли!";
            }
            JOptionPane.showMessageDialog(this, message, "Игра окончена", JOptionPane.INFORMATION_MESSAGE);
            int choice = JOptionPane.showConfirmDialog(this, "Начать новую игру?", "Игра окончена", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                restartGame();
            } else {
                System.exit(0);
            }
        }
    }
    
    public Game getGame() {
        return game;
    }
    
    public void restartGame() {
        game = new Game();
        mrXJustMoved = false;
        initializeGame();
        gameInfoPanel.updateGame(game);
        playersPanel.updateGame(game);
        movePanel.updateGame(game);
        gamePanel.updateGame(game);
        updateUI();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainWindow().setVisible(true);
        });
    }
}

