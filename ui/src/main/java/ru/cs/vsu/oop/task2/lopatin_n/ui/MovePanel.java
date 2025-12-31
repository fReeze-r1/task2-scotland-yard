package ru.cs.vsu.oop.task2.lopatin_n.ui;

import ru.cs.vsu.oop.task2.lopatin_n.components.Detective;
import ru.cs.vsu.oop.task2.lopatin_n.components.Game;
import ru.cs.vsu.oop.task2.lopatin_n.components.MrX;
import ru.cs.vsu.oop.task2.lopatin_n.components.Player;
import ru.cs.vsu.oop.task2.lopatin_n.core.Station;
import ru.cs.vsu.oop.task2.lopatin_n.core.TicketType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MovePanel extends JPanel {
    private Game game;
    private MainWindow mainWindow;

    private JComboBox<TicketType> transportCombo;
    private JCheckBox doubleMoveCheckBox;
    private JList<String> stationsList;
    private DefaultListModel<String> stationsModel;
    private JButton makeMoveButton;

    private JComboBox<TicketType> secondTransportCombo;
    private JList<String> secondStationsList;
    private DefaultListModel<String> secondStationsModel;

    public MovePanel(Game game, MainWindow mainWindow) {
        this.game = game;
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
            "🎮 Выбор хода",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font(Font.SANS_SERIF, Font.BOLD, 14),
            new Color(50, 100, 150)
        ));
        setBackground(new Color(250, 250, 255));

        setupMoveControls();
        updateMoveOptions();
    }

    private void setupMoveControls() {
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Выберите ход", TitledBorder.CENTER, TitledBorder.TOP));

        JPanel firstMovePanel = new JPanel(new BorderLayout(5, 5));
        firstMovePanel.setBorder(BorderFactory.createTitledBorder("Первый ход"));

        JPanel transportSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        transportSelectionPanel.add(new JLabel("Транспорт:"));
        transportCombo = new JComboBox<>(new TicketType[]{
                TicketType.TAXI, TicketType.BUS, TicketType.UNDERGROUND, TicketType.BLACK
        });
        transportCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TicketType) {
                    setText(getTransportName((TicketType) value));
                }
                return this;
            }
        });
        transportCombo.addActionListener(e -> updateStationsList());
        transportSelectionPanel.add(transportCombo);
        firstMovePanel.add(transportSelectionPanel, BorderLayout.NORTH);

        stationsModel = new DefaultListModel<>();
        stationsList = new JList<>(stationsModel);
        stationsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        stationsList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        firstMovePanel.add(new JScrollPane(stationsList), BorderLayout.CENTER);

        topPanel.add(firstMovePanel, BorderLayout.CENTER);

        JPanel doubleMovePanel = new JPanel(new BorderLayout(5, 5));
        doubleMovePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(150, 180, 220), 1),
            "Двойной ход (только для Mr. X)",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font(Font.SANS_SERIF, Font.BOLD, 11),
            new Color(80, 120, 160)
        ));
        doubleMovePanel.setBackground(new Color(250, 250, 255));

        doubleMoveCheckBox = new JCheckBox("Использовать двойной ход");
        doubleMoveCheckBox.addActionListener(e -> toggleDoubleMoveControls());
        doubleMovePanel.add(doubleMoveCheckBox, BorderLayout.NORTH);

        JPanel secondTransportSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        secondTransportSelectionPanel.add(new JLabel("Второй транспорт:"));
        secondTransportCombo = new JComboBox<>(new TicketType[]{
                TicketType.TAXI, TicketType.BUS, TicketType.UNDERGROUND, TicketType.BLACK
        });
        secondTransportCombo.setRenderer(transportCombo.getRenderer());
        secondTransportCombo.addActionListener(e -> updateSecondStationsList());
        secondTransportSelectionPanel.add(secondTransportCombo);
        doubleMovePanel.add(secondTransportSelectionPanel, BorderLayout.NORTH);

        secondStationsModel = new DefaultListModel<>();
        secondStationsList = new JList<>(secondStationsModel);
        secondStationsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        secondStationsList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        doubleMovePanel.add(new JScrollPane(secondStationsList), BorderLayout.CENTER);

        topPanel.add(doubleMovePanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.CENTER);

        makeMoveButton = new JButton("✅ Сделать ход");
        makeMoveButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        makeMoveButton.setBackground(new Color(70, 130, 180));
        makeMoveButton.setForeground(Color.WHITE);
        makeMoveButton.setFocusPainted(false);
        makeMoveButton.setBorderPainted(false);
        makeMoveButton.setPreferredSize(new Dimension(0, 40));
        makeMoveButton.addActionListener(e -> handleMakeMove());
        add(makeMoveButton, BorderLayout.SOUTH);
    }

    private void toggleDoubleMoveControls() {
        boolean enabled = doubleMoveCheckBox.isSelected();
        secondTransportCombo.setEnabled(enabled);
        secondStationsList.setEnabled(enabled);
        if (!enabled) {
            secondStationsModel.clear();
        } else {
            updateSecondStationsList();
        }
    }

    public void updateMoveOptions() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return;
        }

        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
            "🎯 Ход: " + currentPlayer.getName(),
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font(Font.SANS_SERIF, Font.BOLD, 13),
            new Color(50, 100, 150)
        ));

        transportCombo.removeAllItems();
        List<TicketType> availableTransports = new ArrayList<>();
        if (currentPlayer instanceof MrX) {
            availableTransports.add(TicketType.TAXI);
            availableTransports.add(TicketType.BUS);
            availableTransports.add(TicketType.UNDERGROUND);
            availableTransports.add(TicketType.BLACK);
        } else {
            if (currentPlayer.hasTicket(TicketType.TAXI)) availableTransports.add(TicketType.TAXI);
            if (currentPlayer.hasTicket(TicketType.BUS)) availableTransports.add(TicketType.BUS);
            if (currentPlayer.hasTicket(TicketType.UNDERGROUND)) availableTransports.add(TicketType.UNDERGROUND);
        }
        for (TicketType type : availableTransports) {
            transportCombo.addItem(type);
        }

        if (currentPlayer instanceof MrX && game.getMrX().getTicketCount(TicketType.DOUBLE_MOVE) > 0 && game.getMrX().canUseDoubleMove()) {
            doubleMoveCheckBox.setVisible(true);
            doubleMoveCheckBox.setEnabled(true);
        } else {
            doubleMoveCheckBox.setVisible(false);
            doubleMoveCheckBox.setEnabled(false);
            doubleMoveCheckBox.setSelected(false);
        }
        toggleDoubleMoveControls();

        updateStationsList();
    }

    private Player getCurrentPlayer() {
        if (game.getMrX().getMoveCount() == 0) {
            return game.getMrX();
        }
        
        if (mainWindow != null && mainWindow.isMrXTurnForUI()) {
            return game.getMrX();
        }
        
        Detective currentDetective = game.getCurrentDetective();
        if (currentDetective != null) {
            return currentDetective;
        }
        
        return game.getMrX();
    }
    
    private void updateStationsList() {
        stationsModel.clear();
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null || currentPlayer.getCurrentStation() == null) {
            return;
        }

        TicketType selectedTransport = (TicketType) transportCombo.getSelectedItem();
        if (selectedTransport == null) {
            return;
        }

        Set<Station> reachableStations = currentPlayer.getCurrentStation().getConnectedStations(selectedTransport);
        for (Station station : reachableStations) {
            if (currentPlayer.canMove(station, selectedTransport)) {
                if (currentPlayer instanceof Detective) {
                    boolean occupied = false;
                    for (Detective d : game.getDetectives()) {
                        if (d != currentPlayer && d.getCurrentStation() != null && d.getCurrentStation().equals(station)) {
                            occupied = true;
                            break;
                        }
                    }
                    if (!occupied) {
                        stationsModel.addElement("Станция " + station.getNumber());
                    }
                } else {
                    stationsModel.addElement("Станция " + station.getNumber());
                }
            }
        }
    }

    private void updateSecondStationsList() {
        secondStationsModel.clear();
        if (!doubleMoveCheckBox.isSelected() || game.getMrX() == null || game.getMrX().getCurrentStation() == null) {
            return;
        }

        int selectedFirstStationIndex = stationsList.getSelectedIndex();
        if (selectedFirstStationIndex == -1) {
            return;
        }
        String firstStationString = stationsModel.getElementAt(selectedFirstStationIndex);
        int firstStationNumber = Integer.parseInt(firstStationString.split(" ")[1]);
        Station firstTargetStation = game.getBoard().getStation(firstStationNumber);

        if (firstTargetStation == null) {
            return;
        }

        TicketType selectedSecondTransport = (TicketType) secondTransportCombo.getSelectedItem();
        if (selectedSecondTransport == null) {
            return;
        }

        Set<Station> reachableStations = firstTargetStation.getConnectedStations(selectedSecondTransport);
        for (Station station : reachableStations) {
            if (game.getMrX().canMove(station, selectedSecondTransport)) {
                secondStationsModel.addElement("Станция " + station.getNumber());
            }
        }
    }

    private void handleMakeMove() {
        int selectedStationIndex = stationsList.getSelectedIndex();
        if (selectedStationIndex == -1) {
            JOptionPane.showMessageDialog(this, "Выберите станцию для хода!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selectedStationString = stationsModel.getElementAt(selectedStationIndex);
        int stationNumber = Integer.parseInt(selectedStationString.split(" ")[1]);
        TicketType transportType = (TicketType) transportCombo.getSelectedItem();

        if (doubleMoveCheckBox.isSelected()) {
            int secondSelectedStationIndex = secondStationsList.getSelectedIndex();
            if (secondSelectedStationIndex == -1) {
                JOptionPane.showMessageDialog(this, "Выберите вторую станцию для двойного хода!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String secondSelectedStationString = secondStationsModel.getElementAt(secondSelectedStationIndex);
            int secondStationNumber = Integer.parseInt(secondSelectedStationString.split(" ")[1]);
            TicketType secondTransportType = (TicketType) secondTransportCombo.getSelectedItem();

            mainWindow.handleMove(stationNumber, transportType, true, secondStationNumber, secondTransportType);
        } else {
            mainWindow.handleMove(stationNumber, transportType, false, 0, null);
        }
    }

    private String getTransportName(TicketType type) {
        switch (type) {
            case TAXI: return "Такси";
            case BUS: return "Автобус";
            case UNDERGROUND: return "Метро";
            case BLACK: return "Черный билет";
            case DOUBLE_MOVE: return "Двойной ход";
            default: return type.toString();
        }
    }

    public void updateGame(Game newGame) {
        this.game = newGame;
        updateMoveOptions();
    }
}

