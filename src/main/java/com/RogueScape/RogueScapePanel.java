package com.RogueScape;

import javax.swing.*;
import net.runelite.client.ui.PluginPanel;
import java.awt.*;
import java.util.List;

import static com.RogueScape.UnlockType.*;


public class RogueScapePanel extends PluginPanel {
    private final RogueScapePlugin plugin;

    public RogueScapePanel(RogueScapePlugin plugin) {
        this.plugin = plugin;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Add reset button at the top
        JButton resetButton = new JButton("Reset Unlocks");
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.addActionListener(e -> plugin.resetUnlocks());
        add(resetButton);

        add(Box.createVerticalStrut(10));
    }

    public void refreshUnlockedList(RogueScapeState state) {
        // Remove everything except the reset button
        removeAll();
        addResetButton();

        // Title
        JLabel title = new JLabel("Unlocked Items:");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(title);
        add(Box.createVerticalStrut(5));

        // Add unlocked items
        for (String itemId : state.unlockedItems) {
            ItemData data = ItemRepository.get(itemId);
            if (data != null) {
                JLabel label = new JLabel(data.getName());
                label.setAlignmentX(Component.CENTER_ALIGNMENT);

                try {
                    ImageIcon icon = new ImageIcon(new java.net.URL(data.getImageUrl()));
                    Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                    label.setIcon(new ImageIcon(img));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                label.setToolTipText(data.getDescription());
                add(label);
            }
        }

        revalidate();
        repaint();
    }

    private void addResetButton() {
        JButton resetButton = new JButton("Reset Unlocks");
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.addActionListener(e -> plugin.resetUnlocks());
        add(resetButton);
        add(Box.createVerticalStrut(10));
    }
}