package com.RogueScape;

import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.ui.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RogueScapeChatboxPanel extends JPanel{
    public RogueScapeChatboxPanel(
            RogueScapePlugin plugin,
            List<UnlockOption> options,
            ChatboxPanelManager manager) {

        // Main layout
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARKER_GRAY_COLOR);

        // Title
        JLabel title = new JLabel("Choose an Unlock");
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // Panel for buttons
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(0, 1, 0, 4));
        optionsPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        // Add a button for each unlock option
        for (UnlockOption option : options) {
            JButton button = new JButton(option.getName());
            button.setFocusPainted(false);
            button.setBackground(ColorScheme.DARK_GRAY_COLOR);
            button.setForeground(Color.WHITE);
            button.setToolTipText(option.getDescription());

            // When clicked: apply unlock, save state, and close chatbox
            button.addActionListener(e -> {
                plugin.selectUnlock(option);
                this.removeAll();              // clear choices
                this.revalidate();
                this.repaint();
            });

            optionsPanel.add(button);
        }

        add(optionsPanel, BorderLayout.CENTER);
    }
}