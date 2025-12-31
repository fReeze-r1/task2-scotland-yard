package ru.cs.vsu.oop.task2.lopatin_n.ui;

import javax.swing.*;
import java.awt.*;

public class RulesPanel extends JPanel {
    
    public RulesPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
            "Правила игры",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font(Font.SANS_SERIF, Font.BOLD, 14),
            new Color(50, 100, 150)
        ));
        setPreferredSize(new Dimension(280, 250));
        setBackground(new Color(250, 250, 255));
        
        JTextArea rulesText = new JTextArea();
        rulesText.setEditable(false);
        rulesText.setOpaque(false);
        rulesText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        rulesText.setLineWrap(true);
        rulesText.setWrapStyleWord(true);
        rulesText.setText(
            "🎯 Цель игры:\n" +
            "• Детективы: поймать Мистера Икс\n" +
            "• Мистер Икс: сделать 22 хода\n\n" +
            
            "📋 Порядок ходов:\n" +
            "1. Мистер Икс ходит первым\n" +
            "2. Затем все детективы по очереди\n" +
            "3. Цикл повторяется\n\n" +
            
            "🔍 Раскрытие Мистера Икс:\n" +
            "Показывает позицию на ходах:\n" +
            "3, 8, 13, 18\n\n" +
            
            "🎫 Особые возможности:\n" +
            "• Мистер Икс: черный билет (любой транспорт)\n" +
            "• Мистер Икс: двойной ход (2 раза за игру)\n" +
            "• Детективы: не могут быть на одной станции"
        );
        rulesText.setForeground(new Color(40, 40, 40));
        
        JScrollPane scrollPane = new JScrollPane(rulesText);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        add(scrollPane, BorderLayout.CENTER);
    }
}

